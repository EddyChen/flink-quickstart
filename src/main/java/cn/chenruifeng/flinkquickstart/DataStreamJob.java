/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.chenruifeng.flinkquickstart;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Skeleton for a Flink DataStream Job.
 *
 * <p>For a tutorial how to write a Flink application, check the
 * tutorials and examples on the <a href="https://flink.apache.org">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class DataStreamJob {

	private static final Logger log = LoggerFactory.getLogger(DataStreamJob.class);

	public static void main(String[] args) throws Exception {
		// Sets up the execution environment, which is the main entry point
		// to building Flink applications.
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// DataStream<String> stream = env.socketTextStream("192.168.12.128", 9999);

		KafkaSource<String> source = KafkaSource.<String>builder()
				.setBootstrapServers("192.168.12.128:9092")
				.setTopics("lines")
				.setGroupId("group1")
				.setStartingOffsets(OffsetsInitializer.earliest())
				.setValueOnlyDeserializer(new SimpleStringSchema())
				.build();

		DataStream<String> stream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

		KafkaSink<String> sink = KafkaSink.<String>builder()
				.setBootstrapServers("192.168.12.128:9092")
				.setRecordSerializer(KafkaRecordSerializationSchema.builder()
						.setTopic("words")
						.setKeySerializationSchema(new SimpleStringSchema())
						.setValueSerializationSchema(new SimpleStringSchema())
						.build()
				)
				.setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
				.build();

		stream.flatMap(new Tokenizer())
				.keyBy(r -> r.f0)
				.sum(1)
				.map(new TupleSerialize())
				.sinkTo(sink);

		// Execute program, beginning computation.
		env.execute("Flink Java API Skeleton");
	}

	public static class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {

		@Override
		public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
			String[] stringList = value.split("\\s");
			for (String s : stringList) {
				// 使用out.collect方法向下游发送数据
				out.collect(new Tuple2(s, 1));
				log.info(s);
			}
		}
	}

	public static class TupleSerialize implements MapFunction<Tuple2<String, Integer>, String> {

		@Override
		public String map(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
			log.info(stringIntegerTuple2.toString());
			return stringIntegerTuple2.toString();
		}
	}
}
