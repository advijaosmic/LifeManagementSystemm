package financeapp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "financeTrackerDB";

    private static MongoClient client = null;
    private static MongoDatabase database = null;

    public static MongoDatabase getDatabase() {
        if (client == null) {
            client = MongoClients.create(URI);
        }
        if (database == null) {
            database = client.getDatabase(DB_NAME);
            System.out.println("Konekcija na bazu: " + DB_NAME);
        }
        return client.getDatabase(DB_NAME);
    }
    }


