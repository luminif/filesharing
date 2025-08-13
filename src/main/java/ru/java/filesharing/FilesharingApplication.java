package ru.java.filesharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.java.filesharing.config.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class FilesharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilesharingApplication.class, args);
	}

}
