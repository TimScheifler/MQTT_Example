package com.example.MQTT_Receiver.mongo;

import com.example.MQTT_Receiver.TimeObject;
import com.mongodb.client.*;
import org.bson.Document;

public class MongoAPI implements IMongoAPI {

    private final MongoCollection<Document> timeObject_collection;

    public MongoAPI(MongoDatabase mongoDatabase) {
        this.timeObject_collection = mongoDatabase.getCollection("lag_0_drop_0");

    }

    @Override
    public Document writeTs(TimeObject timeObject) {
        return new Document("start", timeObject.getStart())
                .append("end", timeObject.getEnd())
                .append("timeTaken", timeObject.getTimeTaken());
    }
}