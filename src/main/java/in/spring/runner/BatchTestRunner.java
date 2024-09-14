package in.spring.runner;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class BatchTestRunner implements CommandLineRunner {

	@Autowired
	private JobLauncher launcher;
	@Autowired
	private Job job;
	
	@Override
	public void run(String... args) throws Exception {

       JobParameters params = new JobParametersBuilder().addDate("sysDate", new Date()).toJobParameters();
	   JobExecution execution = launcher.run(job, params);
	   System.out.println("Job Exection Ststus "+execution.getExitStatus());
	}

}
