package com.greatit.demo;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.greatit.demo.avro.HappinessAverage;
import com.greatit.demo.avro.HappinessItem;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.protocol.types.Field.Int32;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.IntegerSerde;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.WindowedSerdes.TimeWindowedSerde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static java.util.Collections.singletonMap;
import static org.apache.kafka.streams.kstream.Suppressed.BufferConfig.unbounded;
import static org.apache.kafka.streams.kstream.WindowedSerdes.TimeWindowedSerde;

public class ItemFactory {

    private static final Logger logger = LoggerFactory.getLogger(ItemFactory.class);

    public static Properties buildProperties(Config config) {
        Properties properties = new Properties();

        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("bootstrap.servers"));
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, config.getString("application.id"));
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StringSerde.class);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, StringSerde.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(StreamsConfig.STATESTORE_CACHE_MAX_BYTES_CONFIG, 0);

        return properties;
    }

    public static Topology buildTopology(Config config,
                                         TimeWindows windows,
                                         SpecificAvroSerde<HappinessItem> happinessItemSerde,
                                         SpecificAvroSerde<HappinessAverage> happinessAverageSerde) {

        StreamsBuilder builder = new StreamsBuilder();

        String inputTopic = config.getString("input.topic.name");
        String outputTopic = config.getString("output.topic.name");

        Produced<Windowed<String>, Long> producedCount = Produced
                .with(new TimeWindowedSerde<>(Serdes.String(), Long.MAX_VALUE), Serdes.Long());

        Produced<Windowed<String>, Integer> producedSum = Produced
                .with(new TimeWindowedSerde<>(Serdes.String(), Long.MAX_VALUE), Serdes.Integer());

        Consumed<String, HappinessItem> consumedItem = Consumed
                .with(Serdes.String(), happinessItemSerde)
                .withTimestampExtractor(new ItemDatetimeExtractor(config));

        Grouped<String, HappinessItem> groupedPressure = Grouped.with(Serdes.String(), happinessItemSerde);

        builder
                .stream(inputTopic, consumedItem)
                //.map((k, v) -> new KeyValue<>((String) v.getId(), v.getLevel()))
                //.groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .groupByKey()
                .windowedBy(windows)
                .aggregate(HappinessAverage::new, (k, tr, ave) -> {
                        ave.setNumOfProduced(ave.getNumOfProduced() + 1);
                        ave.setTotal(ave.getTotal() + tr.getLevel());
                        return ave;
                }, Materialized.with(Serdes.String(), happinessAverageSerde))
                .toStream()
                .map((Windowed<String> key, HappinessAverage finalHappinessAverageSerde) -> {
                        double aveNoFormat = finalHappinessAverageSerde.getTotal()/(double)finalHappinessAverageSerde.getNumOfProduced();
                        finalHappinessAverageSerde.setAvgOfProduced(Double.parseDouble(String.format("%.2f", aveNoFormat)));
                        return new KeyValue<>(key.key(),finalHappinessAverageSerde) ;
                })
                .to(outputTopic, Produced.with(Serdes.String(), happinessAverageSerde));
                //.reduce(Integer::sum)
                //.toStream().mapValues(v -> v.toString() + " total sales").to(outputTopic, Produced.with(new TimeWindowedSerde<>(Serdes.String(), Long.MAX_VALUE), Serdes.String()));
        
        return builder.build();
    }
    
    public static void main(String[] args) {

        final Config config = ConfigFactory.load();

        final Properties properties = buildProperties(config);

        Map<String, Object> serdeConfig =
                singletonMap(SCHEMA_REGISTRY_URL_CONFIG, config.getString("schema.registry.url"));

        SpecificAvroSerde<HappinessItem> happinessItemSerde = new SpecificAvroSerde<>();
        SpecificAvroSerde<HappinessAverage> happinessAverageSerde = new SpecificAvroSerde<>();

        happinessItemSerde.configure(serdeConfig, false);
        happinessAverageSerde.configure(serdeConfig, false);

        TimeWindows windows = TimeWindows

                .ofSizeAndGrace(config.getDuration("window.size"), config.getDuration("window.grace.period"))

                .advanceBy(config.getDuration("window.size"));

        Topology topology = buildTopology(config, windows, happinessItemSerde, happinessAverageSerde);

        logger.debug(topology.describe().toString());

        final KafkaStreams streams = new KafkaStreams(topology, properties);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        streams.cleanUp();
        streams.start();
    }
}
