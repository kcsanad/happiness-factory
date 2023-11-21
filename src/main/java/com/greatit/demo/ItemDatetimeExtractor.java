package com.greatit.demo;

import com.jasongoodwin.monads.Try;
import com.typesafe.config.Config;
import com.greatit.demo.avro.HappinessItem;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ItemDatetimeExtractor implements TimestampExtractor {

    private final DateTimeFormatter formatter;
    private static final Logger logger = LoggerFactory.getLogger(TimestampExtractor.class);

    public ItemDatetimeExtractor(Config config) {
        this.formatter = DateTimeFormatter.ofPattern(config.getString("item.datetime.pattern"));
    }

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {
        return Try

                .ofFailable(() -> ((HappinessItem) record.value()).getDatetime())

                .onFailure((ex) -> logger.error("fail to cast the HappinessItem: ", ex))

                .map((stringDatetimeString) ->  ZonedDateTime.parse(stringDatetimeString, this.formatter))

                .onFailure((ex) -> logger.error("fail to parse the event datetime due to: ", ex))

                .map((zonedDatetime) -> zonedDatetime.toInstant().toEpochMilli())

                .onFailure((ex) -> logger.error("fail to convert the datetime to instant due to: ", ex))

                .orElse(-1L);
    }
}
