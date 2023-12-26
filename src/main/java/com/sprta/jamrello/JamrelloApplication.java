package com.sprta.jamrello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)  // 인증기능 구현이 끝날떄까지 비활성화
@EnableJpaAuditing
public class JamrelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(JamrelloApplication.class, args);
	}

}
