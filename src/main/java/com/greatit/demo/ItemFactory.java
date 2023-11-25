package com.greatit.demo;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.greatit.demo.avro.HappinessAverage;
import com.greatit.demo.avro.HappinessItem;
import com.greatit.demo.rest.HappinessResource;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static java.util.Collections.singletonMap;

public class ItemFactory {

        private static final Logger logger = LoggerFactory.getLogger(ItemFactory.class);

        public static final String BASE_URI = "http://localhost:8080/";

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
                        SpecificAvroSerde<HappinessAverage> happinessAverageSerde,
                        DateTimeFormatter formatter) {

                StreamsBuilder builder = new StreamsBuilder();

                String inputTopic = config.getString("input.topic.name");
                String outputTopic = config.getString("output.topic.name");
                String tableOutputTopic = config.getString("output.table.topic.name");

                Consumed<String, HappinessItem> consumedItem = Consumed
                                .with(Serdes.String(), happinessItemSerde)
                                .withTimestampExtractor(new ItemDatetimeExtractor(config));

                final Serde<String> stringSerde = Serdes.String();

                final KStream<String, HappinessAverage> stream = builder
                                .stream(inputTopic, consumedItem)
                                .groupByKey()
                                .windowedBy(windows)
                                .aggregate(HappinessAverage::new, (k, tr, ave) -> {
                                        ave.setNumOfProduced(ave.getNumOfProduced() + 1);
                                        ave.setTotal(ave.getTotal() + tr.getLevel());
                                        return ave;
                                }, Materialized.with(Serdes.String(), happinessAverageSerde))
                                .toStream()
                                .map((Windowed<String> key, HappinessAverage finalHappinessAverageSerde) -> {
                                        double aveNoFormat = finalHappinessAverageSerde.getTotal()
                                                        / (double) finalHappinessAverageSerde.getNumOfProduced();
                                        finalHappinessAverageSerde.setAvgOfProduced(
                                                        Double.parseDouble(String.format("%.2f", aveNoFormat)));
                                        finalHappinessAverageSerde.setDatetime(ZonedDateTime.now().format(formatter));
                                        return new KeyValue<>(key.key(), finalHappinessAverageSerde);
                                });

                final KTable<String, HappinessAverage> convertedTable = stream
                                .toTable(Materialized
                                                .<String, HappinessAverage, KeyValueStore<Bytes, byte[]>>as(
                                                                "stream-converted-to-table")
                                                .withKeySerde(stringSerde)
                                                .withValueSerde(happinessAverageSerde));

                stream
                                .to(outputTopic, Produced.with(Serdes.String(), happinessAverageSerde));

                convertedTable
                                .toStream().to(tableOutputTopic, Produced.with(Serdes.String(), happinessAverageSerde));

                return builder.build();
        }

        // Starts Grizzly HTTP server
        public static HttpServer startServer(KafkaStreams streams, String apiURL) {

                HappinessResource happinessResource = new HappinessResource(streams);

                // scan packages
                final ResourceConfig config = new ResourceConfig();
                // config.packages(true, "com.mkyong");
                //config.register(HappinessResource.class);
                config.register(happinessResource);

                // enable auto scan @Contract and @Service
                config.register(AutoScanFeature.class);

                logger.info("Starting Server........");

                final HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(apiURL), config);

                return httpServer;

        }

        public static void main(String[] args) {

                final Config config = ConfigFactory.load();

                final Properties properties = buildProperties(config);

                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(config.getString("item.datetime.pattern"));

                Map<String, Object> serdeConfig = singletonMap(SCHEMA_REGISTRY_URL_CONFIG,
                                config.getString("schema.registry.url"));

                SpecificAvroSerde<HappinessItem> happinessItemSerde = new SpecificAvroSerde<>();
                SpecificAvroSerde<HappinessAverage> happinessAverageSerde = new SpecificAvroSerde<>();

                happinessItemSerde.configure(serdeConfig, false);
                happinessAverageSerde.configure(serdeConfig, false);

                TimeWindows windows = TimeWindows
                                .ofSizeAndGrace(config.getDuration("window.size"),
                                                config.getDuration("window.grace.period"))
                                .advanceBy(config.getDuration("window.size"));

                Topology topology = buildTopology(
                        config, 
                        windows, 
                        happinessItemSerde, 
                        happinessAverageSerde, 
                        formatter
                );

                final KafkaStreams streams = new KafkaStreams(topology, properties);

                //
                //
                //
                final HttpServer httpServer = startServer(streams, config.getString("rest.api.url"));

                // add jvm shutdown hook
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try {
                                System.out.println("Shutting down the application...");

                                httpServer.shutdownNow();

                                System.out.println("Done, exit.");
                        } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                        }
                }));

                Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

                streams.cleanUp();
                streams.start();
        }
}
