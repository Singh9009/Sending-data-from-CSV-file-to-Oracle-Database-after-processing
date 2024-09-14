package in.spring.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import in.spring.model.Employee;

@Component
public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee employee) throws Exception {
		
		if(employee.getSalary() > 20000)
		{
			employee.setGrossSalary(employee.getSalary()+employee.getSalary()*0.3f);
			employee.setNetSalary(employee.getGrossSalary()-employee.getGrossSalary());
			return employee;
		}
		return null;
	}

}
