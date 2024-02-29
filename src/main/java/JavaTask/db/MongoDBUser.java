package JavaTask.db;

import JavaTask.entity.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class MongoDBUser {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    public MongoDBUser(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        // Хешируем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        mongoTemplate.save(user, "user"); // "user" - это имя коллекции в MongoDB
    }

    public static void connectToMongoDB() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("java-test");
            MongoCollection<Document> collection = database.getCollection("user");

            System.out.println("Connection to MongoDB");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
