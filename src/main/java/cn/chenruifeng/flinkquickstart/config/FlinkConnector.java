package cn.chenruifeng.flinkquickstart.config;

import lombok.Data;

@Data
public class FlinkConnector {

  private KafkaConnector kafka;

  private RedisConnector redis;

}
