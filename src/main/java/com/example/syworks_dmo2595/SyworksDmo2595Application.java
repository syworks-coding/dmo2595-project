package com.example.syworks_dmo2595;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.HiddenHttpMethodFilter;



//TODO : 대댓글
@SpringBootApplication
@EnableScheduling
public class SyworksDmo2595Application {

	public static void main(String[] args) {
		SpringApplication.run(SyworksDmo2595Application.class, args);
	}

	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
