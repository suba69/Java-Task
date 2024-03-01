package JavaTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import JavaTask.db.MongoDBUser;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class JavaTaskApplication {
	public static void main(String[] args) {

		// Запускаем Spring Boot приложение
		SpringApplication.run(JavaTaskApplication.class, args);
	}
}
