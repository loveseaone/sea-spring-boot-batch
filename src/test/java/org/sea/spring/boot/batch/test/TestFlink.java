package org.sea.spring.boot.batch.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sea.spring.boot.batch.FLink2Mysql;
import org.sea.spring.boot.batch.model.StudentEntity;
import org.springframework.util.StopWatch;

import lombok.extern.log4j.Log4j2;
@Log4j2
public class TestFlink {

	@Test
	public void test() {
		
		//构造
		StopWatch watch = new StopWatch("testAdd1");
		watch.start("构造");
		List<StudentEntity> list =new ArrayList<StudentEntity>(10000);
		for(int i=0;i<10000;i++) {
			list.add(init(i+60000));
		}
		watch.stop();
	 
		//保存
		watch.start("保存");
		FLink2Mysql.add(list);
		watch.stop();
		log.info(watch.prettyPrint());
	}
	 
	private StudentEntity init(int i) {
		StudentEntity student=new StudentEntity();
		 
		
		student.setId(i);
		student.setName("name"+i);
		student.setAge(i);
		return student;
		
	}
}
