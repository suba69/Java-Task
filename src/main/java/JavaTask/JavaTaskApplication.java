package JavaTask;

import JavaTask.db.MongoDBTest_report;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import JavaTask.db.MongoDBUser;
import org.springframework.context.annotation.ComponentScan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static JavaTask.db.MongoDBTest_report.connectToMongoDBTask;

@SpringBootApplication
public class JavaTaskApplication {

	public static void main(String[] args) {
		// Вызываем метод подключения к MongoDB
		connectToMongoDBTask();

		// Запускаем Spring Boot приложение
		SpringApplication.run(JavaTaskApplication.class, args);
	}
}
