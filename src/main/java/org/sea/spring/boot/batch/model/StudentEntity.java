package org.sea.spring.boot.batch.model;

 
public class StudentEntity implements java.io.Serializable {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -2385409861150253667L;
	private Integer id;
	private String name;
    private int  age;

     

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

	
 
}
