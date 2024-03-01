package JavaTask.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
@Component
public class MongoDBCache {

    private static final String FILE_PATH = "C:\\Java-Task\\test_report.json";

    @Scheduled(fixedRate = 3600000)
    public void updateStatisticsFromJson() {
        try {
            String jsonContent = String.join("", Files.readAllLines(Paths.get(FILE_PATH)));
            updateMongoDB(jsonContent);
            System.out.println("Статистика успешно обновлена Collection cache");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка чтения файла JSON");
        }
    }

    private void updateMongoDB(String jsonContent) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("java-test");
            MongoCollection<Document> collection = database.getCollection("cache");

            Document document = Document.parse(jsonContent);
            String uniqueIdentifier = document.getString("uniqueIdentifier");

            collection.replaceOne(new Document("uniqueIdentifier", uniqueIdentifier), document);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка обновления MongoDB");
        }
    }
}
