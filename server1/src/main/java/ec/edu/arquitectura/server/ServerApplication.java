package ec.edu.arquitectura.server;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class ServerApplication {

	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("processTextFileJob")
    Job job1;
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	//@Scheduled(fixedDelay = 10000, initialDelay = 5000)
	@Scheduled(cron = "*/20 * * * * *")
    public void perform() throws Exception {
        System.out.println("Iniciando el Job");
        JobParameters params = new JobParametersBuilder()
                .addString("processTextFile", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job1, params);
    }

}
