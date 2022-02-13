package com.example.MQTT_Receiver;

import com.example.MQTT_Receiver.mongo.IMongoAPI;
import com.example.MQTT_Receiver.mongo.MongoAPI;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

public class ConsumerApplication {
    private final static String QUEUE_NAME = "timestamp";

    private static MongoDatabase mongoDatabase;
    private static IMongoAPI mongoAPI;
    private static Processor processor;

    public static void main(String[] argv) throws Exception {


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        MongoClient mongoClient = new MongoClient("localhost", 27017);

        mongoDatabase = mongoClient.getDatabase("EventProcessing");
        mongoAPI = new MongoAPI(mongoDatabase);
        processor = new Processor(mongoDatabase, mongoAPI);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
            Timestamp timestampStart = Timestamp.valueOf(message);
            Long timeTaken = timestampEnd.getTime() - timestampStart.getTime();
            TimeObject timeObject = new TimeObject(timestampStart, timestampEnd);

            processor.processTimeObject(timeObject);
            System.out.println(" [x] Received '" + message + "' Diff: " + timeObject.getTimeTaken());
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    public static void messageArrived(String message) throws Exception {
        Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
        Timestamp timestampStart = Timestamp.valueOf(message);
        Long timeTaken = timestampEnd.getTime() - timestampStart.getTime();

        TimeObject timeObject = new TimeObject(timestampStart, timestampEnd);
        processor.processTimeObject(timeObject);
        System.out.println("Timedifference: " + timeObject.getTimeTaken());
    }
}
