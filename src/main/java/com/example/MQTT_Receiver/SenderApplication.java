package com.example.MQTT_Receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

public class SenderApplication {
    private final static String QUEUE_NAME = "timestamp";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPassword("admin");
        factory.setUsername("admin");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            int count = 1;
            while (count <= 900) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String messageString = timestamp.toString();
                channel.basicPublish("", QUEUE_NAME, null, messageString.getBytes(StandardCharsets.UTF_8));
                System.out.println(count + " published.");
                Thread.sleep(600);
                count++;
            }
        }
    }
}