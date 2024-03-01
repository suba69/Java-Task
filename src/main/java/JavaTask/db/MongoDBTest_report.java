package JavaTask.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class MongoDBTest_report {

    public static void connectToMongoDBTask() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("java-test");
            MongoCollection<Document> collection = database.getCollection("task");

            // Вивести всі документи з колекції
            collection.find().forEach(document -> System.out.println(document.toJson()));

            System.out.println("Connection to MongoDB");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
