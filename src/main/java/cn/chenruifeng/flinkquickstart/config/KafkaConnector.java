package cn.chenruifeng.flinkquickstart.config;

import lombok.Data;

@Data
public class KafkaConnector {

  private String bootstrapServer;
  private String topic;
  private String groupId;
}
