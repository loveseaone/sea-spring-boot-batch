package org.sea.spring.boot.batch.job;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

import org.sea.spring.boot.batch.model.StudentEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

@Configuration
@EnableBatchProcessing
@Log4j2
public class DataBatchConfiguration {

	@Resource
	private JobBuilderFactory jobBuilderFactory; // 用于构建JOB

	@Resource
	private StepBuilderFactory stepBuilderFactory; // 用于构建Step

	@Resource
	private EntityManagerFactory emf; // 注入实例化Factory 访问数据

	@Resource
	private JobListener jobListener; // 简单的JOB listener

	/**
	 * 一个简单基础的Job通常由一个或者多个Step组成
	 */
	@Bean
	public Job dataHandleJob() {
		return jobBuilderFactory.get("dataHandleJob").
				incrementer(new RunIdIncrementer()).start(handleDataStep()). // start是JOB执行的第一个step
				listener(jobListener). // 设置了一个简单JobListener
				build();
	}

	/**
	 * 一个简单基础的Step主要分为三个部分 ItemReader : 用于读取数据 ItemProcessor : 用于处理数据 ItemWriter :
	 * 用于写数据
	 */
	@Bean
	public Step handleDataStep() {
		return stepBuilderFactory.get("getData").<StudentEntity, StudentEntity>chunk(100).
				// <输入,输出> 。chunk通俗的讲类似于SQL的commit;
				// 这里表示处理(processor)100条后写入(writer)一次。
				faultTolerant().retryLimit(3).retry(Exception.class).skipLimit(100).skip(Exception.class). // 捕捉到异常就重试,重试100次还是异常,JOB就停止并标志失败
				reader(getDataReader()). // 指定ItemReader
				processor(getDataProcessor()). // 指定ItemProcessor
				writer(getDataWriter()). // 指定ItemWriter
				build();
	}

	@Bean
	public ItemReader<? extends StudentEntity> getDataReader() {
		// 读取数据,这里可以用JPA,JDBC,JMS 等方式 读入数据
		JpaPagingItemReader<StudentEntity> reader = new JpaPagingItemReader<>();
		// 这里选择JPA方式读数据 一个简单的 native SQL
		String sqlQuery = "SELECT * FROM T_WX_NOTIFY";
		try {
			JpaNativeQueryProvider<StudentEntity> queryProvider = new JpaNativeQueryProvider<>();
			queryProvider.setSqlQuery(sqlQuery);
			queryProvider.setEntityClass(StudentEntity.class);
			queryProvider.afterPropertiesSet();
			reader.setEntityManagerFactory(emf);
			reader.setPageSize(3);
			reader.setQueryProvider(queryProvider);
			reader.afterPropertiesSet();
			// 所有ItemReader和ItemWriter实现都会在ExecutionContext提交之前将其当前状态存储在其中,如果不希望这样做,可以设置setSaveState(false)
			reader.setSaveState(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reader;
	}

	@Bean
	public ItemProcessor<StudentEntity, StudentEntity> getDataProcessor() {
		return new ItemProcessor<StudentEntity, StudentEntity>() {
			@Override
			public StudentEntity process(StudentEntity student) throws Exception {
				log.info("processor data : " + student.getId()); // 模拟 假装处理数据,这里处理就是打印一下
				return student;
			}
		};

	}

	@Bean
	public ItemWriter<StudentEntity> getDataWriter() {
		return list -> {
			for (StudentEntity student : list) {
				log.info("write data : " + student.getId()); // 模拟 假装写数据 ,这里写真正写入数据的逻辑
			}
		};
	}
}
