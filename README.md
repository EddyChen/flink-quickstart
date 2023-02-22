
## app generate

```bash
mvn archetype:generate \
    -DarchetypeGroupId=org.apache.flink \
    -DarchetypeArtifactId=flink-quickstart-java \
    -DarchetypeVersion=1.16.0 \
    -DgroupId=cn.chenruifeng \
    -DartifactId=flink-quickstart \
    -Dversion=1.0.0 \
    -Dpackage=cn.chenruifeng.flinkquickstart \
    -DinteractiveMode=false
```

## kafka commands

```bash

bin/kafka-topic.sh --list --bootstrap-server localhost:9092

bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic topic-name --property "parse.key=true" --property "key.separator=:"

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic-name --from-beginning

```

## kafka docker compose

```yaml
version: "2"

services:
  zookeeper:
    image: docker.io/bitnami/zookeeper:3.8
    ports:
      - "2181:2181"
    volumes:
      - "zookeeper_data:/home/k8s/kafka/zk_data"
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    image: docker.io/bitnami/kafka:3.4
    ports:
      - "9092:9092"
    volumes:
      - "kafka_data:/home/k8s/kafka/kafka_data"
    environment:
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.12.128:9092
    depends_on:
      - zookeeper

volumes:
  zookeeper_data:
    driver: local
  kafka_data:
    driver: local
```

## Mac Kafka Local

```bash
brew install kafka

brew services start zookeeper
brew services start kafka

zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties 
kafka-server-start /usr/local/etc/kafka/server.properties


 
```

## Upload Flink Dist jars for Local Debug
```xml
<dependencies>
  
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    
    <dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-dist</artifactId>
      <version>1.16.1</version>
      <scope>test</scope>
    </dependency>
    
    <dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-shaded-zookeeper-3</artifactId>
      <version>3.5.9-15.0</version>
      <scope>test</scope>
    </dependency>
    
    <dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-shaded-guava</artifactId>
      <version>30.1.1-jre-15.0</version>
      <scope>test</scope>
    </dependency>
  
</dependencies>
```
