package org.sea.spring.boot.batch.dao;
import org.sea.spring.boot.batch.model.StudentEntity;

/**
 * WxNotify Edit Dao
 * @author system
 */
public interface StudentEdt {
	/**
     * add record
     * @author system
     * @param wxNotify wxNotify
     * @return int
     */
    public int add (StudentEntity student);
    
     
}