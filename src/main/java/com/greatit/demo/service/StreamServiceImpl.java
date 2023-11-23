package com.greatit.demo.service;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsMetadata;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.jvnet.hk2.annotations.Service;
import org.rocksdb.MutableOptionKey.ValueType;

import com.google.gson.Gson;
import com.greatit.demo.avro.HappinessAverage;

import jakarta.ws.rs.NotFoundException;

@Service
public class StreamServiceImpl implements StreamService {

    @Override
    public String getHello(String name, KafkaStreams streams) {
         ReadOnlyKeyValueStore<String, HappinessAverage> store = streams.store(StoreQueryParameters.fromNameAndType("stream-converted-to-table", QueryableStoreTypes.keyValueStore()));
        if (store == null) {
            throw new NotFoundException();
        }

        final HappinessAverage value = store.get("KEY1");
        if (value == null) {
            throw new NotFoundException();
        }
        return new Gson().toJson(value);
    }
}
