package org.sea.spring.boot.batch.job;

import java.util.Iterator;
import java.util.List;

import org.sea.spring.boot.batch.model.StudentEntity;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class InputStudentItemReader implements ItemReader<StudentEntity>{
	
	private final Iterator<StudentEntity> iterator;

	public InputStudentItemReader(List<StudentEntity> data) {
	        this.iterator = data.iterator();
	}

	@Override
	public StudentEntity read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		 
		 if (iterator.hasNext()) {
	            return this.iterator.next();
	        } else {
	            return null;
	        }
	}

}
