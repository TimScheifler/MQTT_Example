FROM openjdk:latest
COPY ./target/MQTTConsumer.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "MQTTConsumer.jar"]