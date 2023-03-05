package cn.chenruifeng.flinkquickstart;

import org.junit.Test;

public class DataStreamJobTests {

  @Test
  public void testMain() {
    DataStreamJob.main(new String[]{"-profile", "home"});

  }
}
