package cn.chenruifeng.flinkquickstart.exception;

public class GeneralException extends RuntimeException {

  private String errorCode;
  private String errorMsg;

  public GeneralException(String errorCode, String errorMsg) {
    super(String.format("[%s] - [%s]", errorCode, errorMsg));
  }

  public GeneralException(String errorCode, String errorMsg, Throwable e) {
    super(String.format("[%s] - [%s]", errorCode, errorMsg), e);
  }

}
