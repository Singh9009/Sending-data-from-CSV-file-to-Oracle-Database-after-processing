package in.spring.model;

import lombok.Data;

@Data
public class Employee {

	private Integer empno;
	private String empname;
	private String empaddress;
	private Float salary;
	private Float grossSalary;
	private Float netSalary;
	
}
