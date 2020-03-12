package org.sea.spring.boot.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

 
 
@SpringBootApplication
@MapperScan("org.sea.spring.boot.batch.dao")
@EnableTransactionManagement
public class SpringBootBathApplication {

	 public static void main(String[] args) {
	        SpringApplication.run(SpringBootBathApplication.class, args);
	 }
	 
	  
}
