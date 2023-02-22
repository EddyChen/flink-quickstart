package cn.chenruifeng.flinkquickstart.config;

import lombok.Data;

@Data
public class FlinkConfig {

  private FlinkApplication application;

  private FlinkConnector source;

  private FlinkConnector destination;

}
