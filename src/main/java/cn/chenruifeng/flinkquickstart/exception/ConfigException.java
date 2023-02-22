package cn.chenruifeng.flinkquickstart.exception;

public class ConfigException extends GeneralException {

  public ConfigException(String errorCode, String errorMsg) {
    super(errorCode, errorMsg);
  }

  public ConfigException(String errorCode, String errorMsg, Throwable e) {
    super(errorCode, errorMsg, e);
  }
}
