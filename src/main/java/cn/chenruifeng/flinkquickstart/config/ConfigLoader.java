package cn.chenruifeng.flinkquickstart.config;

import cn.chenruifeng.flinkquickstart.exception.ConfigException;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ConfigLoader {

  public YamlConfig load(String filename) {
    Yaml yaml = new Yaml(new Constructor(YamlConfig.class));
    try (InputStream inputStream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(filename)) {
      YamlConfig yamlConfig = yaml.load(inputStream);
      log.info("Flink App Config {} loaded! ");
      return yamlConfig;
    } catch(IOException ioe) {
      log.error(ioe.getMessage());
      throw new ConfigException("CONFIG_ERROR", "读取Yaml配置文件失败!", ioe);
    }
  }



}
