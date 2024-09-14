package in.spring.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobMonitoringListener implements JobExecutionListener {

	public void beforeJob(JobExecution jobExecution)
	{
		System.out.println("JobMonitoringListener.beforeJob()");
	  System.out.println("Job status "+jobExecution.getStatus());
	  System.out.println("Job Start Time "+jobExecution.getStartTime());
	}
	
	public void afterJob(JobExecution jobExecution)
	{
		System.out.println("JobMonitoringListener.afterJob()");
		System.out.println("Job status "+jobExecution.getStatus());
		System.out.println("Job End Time "+jobExecution.getEndTime());
		System.out.println("Job Exit Status "+jobExecution.getExitStatus());
	}
	
}
