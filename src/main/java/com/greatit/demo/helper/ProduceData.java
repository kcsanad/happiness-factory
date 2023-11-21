package com.greatit.demo.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greatit.demo.avro.HappinessItem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ProduceData {

    private static final Logger logger = LoggerFactory.getLogger(ProduceData.class);

    public static void main(String[] args) {

        Config config = ConfigFactory.load();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(config.getString("item.datetime.pattern"));

        Properties properties = new Properties();

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

        try {
            avroRecord.put(HappinessItem.SCHEMA$.getFields().get(0).name(), "101");
            avroRecord.put(HappinessItem.SCHEMA$.getFields().get(1).name(), ZonedDateTime.now().format(formatter));
            avroRecord.put(HappinessItem.SCHEMA$.getFields().get(2).name(), 30);

            ProducerRecord<Object, Object> record = new ProducerRecord<>(config.getString("input.topic.name"), "KEY1",
                    avroRecord);

            logger.info(producer.send(record).get().toString());

        } catch (SerializationException e) {
            // may need to do something with it
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
