package ec.edu.arquitectura.server.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ec.edu.arquitectura.server.process.ReadAndInsertTask;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class JobConfig {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    protected Step readAndInsertTask() {
        return steps
                .get("readAndInsertTask")
                .tasklet(new ReadAndInsertTask())
                .build();
    }

    @Bean
    public Job processTextFileJob() {
        return jobs
                .get("processTextFileJob")
                .start(readAndInsertTask())
                .build();
    }
}
