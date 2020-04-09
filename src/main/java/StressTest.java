import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.stresstest.StressTestThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mongodb.client.model.Filters.eq;

public class StressTest {
    public static void main(String[] args) {

        /*
        initializing mongo client
        */
        MongoClient client = new MongoClient(
                new ServerAddress("localhost", 27017));

        /*
        initialize database
        */
        MongoDatabase database = client.getDatabase("StressDatabase");

        /*
        initialize collection
        */
        MongoCollection<Document> collection = database.getCollection("dummyData");

        /*
        loop for adding documents in collection
        */
        for (int i=0; i<100000; i++) {
            Document document = new Document();
            document.put("id", "DocumentId: " + i);
            collection.insertOne(document);
        }

        /*
        Initialize thread pool executor with fixed threads that is 16
        */
        ExecutorService threadPool = Executors.newFixedThreadPool(16);

        System.out.println("*********data added**********  " + collection.countDocuments());

        /*
        Loop for retrieve data from database using collection
        for this we are creating threads and assign task to each thread
        */
        for (long i=0; i<collection.countDocuments(); i++) {
            Runnable worker = new StressTestThread(collection, i);
            threadPool.execute(worker);
        }

        threadPool.shutdown();

        System.out.println("*********data read**********");
    }
}
