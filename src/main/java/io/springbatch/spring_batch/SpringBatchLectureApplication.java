package io.springbatch.spring_batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBatchLectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchLectureApplication.class, args);
	}

}
