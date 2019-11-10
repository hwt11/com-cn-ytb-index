package com.cn.red.point.nats;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;

public class NatsConnection {
    private static final String NATS_BUSINESS[] = {"nats://192.168.1.22:4222"};
    private static Connection connection;

    public static Connection getConnection() throws IOException, InterruptedException {
        synchronized (NatsConnection.class) {
            if (connection == null) {
                Options options = new Options.Builder().servers(NATS_BUSINESS).maxReconnects(-1).build();
                connection = Nats.connect(options);
            }
            return connection;
        }
    }
}
