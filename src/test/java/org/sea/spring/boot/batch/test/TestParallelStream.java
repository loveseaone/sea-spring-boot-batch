package org.sea.spring.boot.batch.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sea.spring.boot.batch.dao.StudentEdt;
import org.sea.spring.boot.batch.model.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringRunner.class)
@SpringBootTest 
@Log4j2
public class  TestParallelStream {

	@Autowired StudentEdt studentdao;
	
	 
	
	 
	@Test
	public void test() {
		
		//构造
		StopWatch watch = new StopWatch("testAdd1");
		watch.start("构造");
		List<StudentEntity> list =new ArrayList<StudentEntity>(10000);
		for(int i=0;i<10000;i++) {
			list.add(init(i));
		}
		watch.stop();
	 
		//保存
	 
		watch.start("保存");
		list.parallelStream().forEach(e->{
			studentdao.add(e);
		} );
		watch.stop();
		log.info(watch.prettyPrint());
	}
	 
	private StudentEntity init(int i) {
	
		StudentEntity student=new StudentEntity();
		student.setName("name"+i);
		student.setAge(i);
	
		return student;
		
	}
}
