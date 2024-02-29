package JavaTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import JavaTask.db.MongoDBUser;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class JavaTaskApplication {

	public static void main(String[] args) {
		// Вызываем метод подключения к MongoDB
		MongoDBUser.connectToMongoDB();

		// Запускаем Spring Boot приложение
		SpringApplication.run(JavaTaskApplication.class, args);
	}
}
