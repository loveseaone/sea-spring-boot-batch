package org.sea.spring.boot.batch.job;

import java.util.ArrayList;
import java.util.List;

import org.sea.spring.boot.batch.model.StudentEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.validator.Validator;
import org.springframework.batch.support.DatabaseType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@EnableBatchProcessing
public class StudentBatchConfig {

	/**
     * ItemReader定义,用来读取数据
     * @return FlatFileItemReader
     */
    @Bean
    @StepScope
    public InputStudentItemReader reader() {
    	List<StudentEntity> list =new ArrayList<StudentEntity>(10000);
		for(int i=0;i<10000;i++) {
			list.add(init(i));
		}
        return new InputStudentItemReader(list);
        
    }
    private StudentEntity init(int i) {
		StudentEntity student=new StudentEntity();
		student.setName("name"+i);
		student.setAge(i);
		return student;
		
	}
    /**
     * ItemProcessor定义，用来处理数据
     *
     * @return
     */
    @Bean
    public ItemProcessor<StudentEntity, StudentEntity> processor() {
        StudentItemProcessor processor = new StudentItemProcessor();
        processor.setValidator(studentBeanValidator());
        return processor;
    }
    
    @Bean
    public Validator<StudentEntity> studentBeanValidator() {
        return new StudentBeanValidator<>();
    }
    /**
     * ItemWriter定义，用来输出数据
     * spring能让容器中已有的Bean以参数的形式注入，Spring Boot已经为我们定义了dataSource
     *
     * @param dataSource
     * @return
     */
    @Bean
    public ItemWriter<StudentEntity> writer(DruidDataSource dataSource) {
    	
        JdbcBatchItemWriter<StudentEntity> writer = new JdbcBatchItemWriter<>();
        //我们使用JDBC批处理的JdbcBatchItemWriter来写数据到数据库
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        String sql="INSERT INTO student (name,age) values(:name,:age)"  ;
        //在此设置要执行批处理的SQL语句
      
        writer.setSql(sql);
        writer.setDataSource(dataSource);
        return writer;
    }

    /**
     *
     * @param dataSource
     * @param transactionManager
     * @return
     * @throws Exception
     */
    @Bean
    public JobRepository jobRepository(DruidDataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {

        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDatabaseType(String.valueOf(DatabaseType.MYSQL));
        // 下面事务隔离级别的配置是针对Oracle的
        jobRepositoryFactoryBean.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        jobRepositoryFactoryBean.afterPropertiesSet();
        return jobRepositoryFactoryBean.getObject();
    }

    /**
     * JobLauncher定义，用来启动Job的接口
     *
     * @param dataSource
     * @param transactionManager
     * @return
     * @throws Exception
     */
    @Bean
    public SimpleJobLauncher jobLauncher(DruidDataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
        return jobLauncher;
    }

    /**
     * Job定义，我们要实际执行的任务，包含一个或多个Step
     *
     * @param jobBuilderFactory
     * @param s1
     * @return
     */
    @Bean
    public Job importJob(JobBuilderFactory jobBuilderFactory, Step s1) {
        return jobBuilderFactory.get("importJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)//为Job指定Step
                .end()
                .build();
    }

    /**
     * step步骤，包含ItemReader，ItemProcessor和ItemWriter
     *
     * @param stepBuilderFactory
     * @param reader
     * @param writer
     * @param processor
     * @return
     */
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<StudentEntity> reader, ItemWriter<StudentEntity> writer,
                      ItemProcessor<StudentEntity, StudentEntity> processor) {
        return stepBuilderFactory
                .get("step1")
                .<StudentEntity, StudentEntity>chunk(1000)//批处理每次提交65000条数据
                .reader(reader)//给step绑定reader
                .processor(processor)//给step绑定processor
                .writer(writer)//给step绑定writer
                .build();
    }

    

    
}
