package com.example.MQTT_Receiver.mongo;

import com.example.MQTT_Receiver.TimeObject;
import org.bson.Document;

public interface IMongoAPI {

    Document writeTs(TimeObject timeObject);

}
