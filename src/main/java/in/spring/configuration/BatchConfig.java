package in.spring.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import in.spring.listener.JobMonitoringListener;
import in.spring.model.Employee;
import in.spring.processor.EmployeeItemProcessor;

@SuppressWarnings("removal")
@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private JobMonitoringListener jmListener;
	@Autowired
	private EmployeeItemProcessor processor;
	@Autowired
	private DataSource dataSource;
	
	
	//First approach to develop reader object
	
	/*public FlatFileItemReader<Employee> createReader(){
		//create object for FlatFileItemReader
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		//Specify the src of csv file
	    reader.setResource(new ClassPathResource("employeeInfo.csv"));
	    //Create LineReader/LineMapper object
	    DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<Employee>();
	    //create LineTokenizer object
	    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
	    tokenizer.setDelimiter(",");
	    tokenizer.setNames("empno", "empname", "empaddress", "salary");
	    //create FieldSerMapper to map each line content to Model class obj properties
	    BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();
	    fieldSetMapper.setTargetType(Employee.class);
	    //Add LineTokenizer, FieldSetMapper to LineMapper
	    lineMapper.setLineTokenizer(tokenizer);
	    lineMapper.setFieldSetMapper(fieldSetMapper);
	    //add LineMapper to FlatFileItemReader
		reader.setLineMapper(lineMapper);		
		return reader;
	}*/
	
	//Second approach to develop reader object
	
	/*public FlatFileItemReader<Employee> createReader(){
	//create obj for FlatFileItemReader
	FlatFileItemReader<Employee> reader=new FlatFileItemReader<Employee>();
	//specify the source of csv file
	reader.setResource(new ClassPathResource("Employeeslnfo.csv"));
	// set LineMapper
	reader.setLineMapper(new DefaultLineMapper<Employee>() {{
		setLineTokenizer(new DelimitedLineTokenizer() {{
		setDelimiter(",");
		setNames("empno", "empname", "empaddress", "salary");
		}});
		setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
			setTargetType(Employee.class);
		}});
	}});
	  return reader;
	}*/
	
	//Thirds approach to develop reader object
	
	@Bean(name="reader")
	public FlatFileItemReader<Employee> createReader(){
	                 return new FlatFileItemReaderBuilder<Employee>()
	                            .name("file-reader")
	                            .resource(new ClassPathResource("employeelnfo.csv"))
	                            .delimited().delimiter(",")
	                            .names("empno", "empname", "empaddress", "salary")
	                            .targetType(Employee.class)
	                            .build();
	}
	
	
	//create writer object(approach 1)
	/*@Bean(name="writer")
	public JdbcBatchItemWriter<Employee> createWriter()
	{
	  //create JdbcBatchItemWriter object
		JdbcBatchItemWriter<Employee> itemWriter = new JdbcBatchItemWriter<Employee>();
	  //set datasource
		itemWriter.setDataSource(dataSource);
	  // Set SQL Query	
		itemWriter.setSql("INSERT INTO BATCH_EMPLOYEE VALUES(:empno, :empname, :empaddress, :salary)");
	 //specify the source provider(The names of named params in SQL Query must match with Model class property names)
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
		return itemWriter;
	}*/
	
	
	//create writer object(approach 2)
	@Bean(name="writer")
	public JdbcBatchItemWriter<Employee> createWriter()
	{
		return new JdbcBatchItemWriterBuilder<Employee>()
				   .dataSource(dataSource)
				   .sql("INSERT INTO BATCH_EMPLOYEE VALUES(:empno, :empname, :salary, :empaddress, :grossSalary, :netSalary)")
				   .beanMapped()
				   .build();
	}
	
	
	//create Step object
	@Bean
	 Step createStep1()
	{
		return stepBuilderFactory.get("step1")
				.<Employee, Employee>chunk(3)
				.reader(createReader())
				.writer(createWriter())
				.processor(processor)
				.build();
	}
	
	//create Job object
	@Bean
	 Job createJob1()
	{
		return jobBuilderFactory.get("job1")
		          .incrementer(new RunIdIncrementer())
		          .listener(jmListener)
		          .start(createStep1())
		          .build();
	}
}
