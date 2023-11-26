package com.greatit.demo.helper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.UUID;    

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.protocol.types.Field.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greatit.demo.avro.HappinessItem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import static net.sourceforge.argparse4j.impl.Arguments.store;

public class ProduceData {

    private static final Logger logger = LoggerFactory.getLogger(ProduceData.class);

    public static void main(String[] args) {

        Config config = ConfigFactory.load();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(config.getString("item.datetime.pattern"));
        boolean cycle = true;

        Properties properties = new Properties();
        ArgumentParser argParser = argParser();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                config.getString("bootstrap.servers"));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        properties.put("schema.registry.url",
                config.getString("schema.registry.url"));

        KafkaProducer producer = new KafkaProducer(properties);

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(HappinessItem.SCHEMA$.toString());
        GenericRecord avroRecord = new GenericData.Record(schema);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                //cycle = false;
                logger.info("Exiting...");
        }));

        try {
            Namespace res = argParser.parseArgs(args);
            final String happinessType  = res.getString("type");
            final Integer happinessInterval = res.getInt("interval");
            final Integer happinessMinVal = res.getInt("min");
            final Integer happinessMaxVal = res.getInt("max");

            while (cycle) {
                logger.info("starting...");
                avroRecord.put(HappinessItem.SCHEMA$.getFields().get(0).name(), happinessType.toString());
                avroRecord.put(HappinessItem.SCHEMA$.getFields().get(1).name(), UUID.randomUUID().toString());
                avroRecord.put(HappinessItem.SCHEMA$.getFields().get(2).name(), ZonedDateTime.now().format(formatter));
                avroRecord.put(HappinessItem.SCHEMA$.getFields().get(3).name(), (int) ((Math.random() * (happinessMaxVal - happinessMinVal)) + happinessMinVal));

                ProducerRecord<Object, Object> record = new ProducerRecord<>(config.getString("input.topic.name"), happinessType, avroRecord);
                producer.send(record);

                Thread.sleep(happinessInterval);
            }
        } catch (SerializationException e) {
            // may need to do something with it
            e.printStackTrace();
        } catch (ArgumentParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }

    private static ArgumentParser argParser() {
                ArgumentParser parser = ArgumentParsers
                        .newFor("Produce-Data").build()
                        .defaultHelp(true)
                        .description("Produce happiness data to KStream");

                parser.addArgument("--type")
                        .action(store())
                        .required(true)
                        .type(String.class)
                        .metavar("TYPE")
                        .help("Type of entry happiness data is made");

                parser.addArgument("--interval")
                        .action(store())
                        .required(false)
                        .type(Integer.class)
                        .metavar("INTERVAL")
                        .setDefault(100)
                        .help("Interval between produced happiness data");
                
                parser.addArgument("--max")
                        .action(store())
                        .required(false)
                        .type(Integer.class)
                        .metavar("MAX")
                        .setDefault(10)
                        .help("MAX value of generated happiness");
                
                parser.addArgument("--min")
                        .action(store())
                        .required(false)
                        .type(Integer.class)
                        .metavar("MIN")
                        .setDefault(100)
                        .help("MIN value of generated happiness");
                return parser;
        }
}
