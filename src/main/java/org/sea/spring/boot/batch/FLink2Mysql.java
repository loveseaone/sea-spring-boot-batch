package org.sea.spring.boot.batch;

import java.util.List;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.io.jdbc.JDBCAppendTableSink;
import org.apache.flink.api.java.io.jdbc.JDBCInputFormat;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;
import org.sea.spring.boot.batch.model.StudentEntity;

public class FLink2Mysql {

	private static String driverClass = "com.mysql.jdbc.Driver";
	private static String dbUrl = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	private static String userName = "root";
	private static String passWord = "mysql";

	public static void add(List<StudentEntity> students) {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStreamSource<StudentEntity> input = env.fromCollection(students);

		DataStream<Row> ds = input.map(new RichMapFunction<StudentEntity, Row>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Row map(StudentEntity student) throws Exception {
				return Row.of(student.getId(), student.getName(), student.getAge());
			}
		});
		TypeInformation<?>[] fieldTypes = new TypeInformation<?>[] { BasicTypeInfo.INT_TYPE_INFO ,BasicTypeInfo.STRING_TYPE_INFO,
				BasicTypeInfo.INT_TYPE_INFO };

		JDBCAppendTableSink sink = JDBCAppendTableSink.builder().setDrivername(driverClass).setDBUrl(dbUrl)
				.setUsername(userName).setPassword(passWord).setParameterTypes(fieldTypes)
				.setQuery("insert into student values(?,?,?)").build();

		sink.emitDataStream(ds);

		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void query() {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		TypeInformation<?>[] fieldTypes = new TypeInformation<?>[] { BasicTypeInfo.STRING_TYPE_INFO,
				BasicTypeInfo.INT_TYPE_INFO };

		RowTypeInfo rowTypeInfo = new RowTypeInfo(fieldTypes);
		// 查询mysql
		JDBCInputFormat jdbcInputFormat = JDBCInputFormat.buildJDBCInputFormat().setDrivername(driverClass)
				.setDBUrl(dbUrl).setUsername(userName).setPassword(passWord).setQuery("select * from student")
				.setRowTypeInfo(rowTypeInfo).finish();
		DataStreamSource<Row> input1 = env.createInput(jdbcInputFormat);
		input1.print();
		try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
