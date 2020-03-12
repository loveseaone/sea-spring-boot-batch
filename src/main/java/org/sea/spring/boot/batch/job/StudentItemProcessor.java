package org.sea.spring.boot.batch.job;

import org.sea.spring.boot.batch.model.StudentEntity;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

public class StudentItemProcessor extends ValidatingItemProcessor<StudentEntity> {
	 @Override
	    public StudentEntity process(StudentEntity item) throws ValidationException {
	        super.process(item);
	        return item;
	    }
}
