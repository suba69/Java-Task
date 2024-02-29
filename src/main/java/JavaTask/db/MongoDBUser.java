package JavaTask.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBUser {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("java-test");
            MongoCollection<Document> collection = database.getCollection("user");

            System.out.println("Connection to MongoDB");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
