package com.greatit.demo.service;

import org.apache.kafka.streams.KafkaStreams;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface StreamService {
    String getHello(String name, KafkaStreams streams);
}
