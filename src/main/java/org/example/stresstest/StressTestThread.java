package org.example.stresstest;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class StressTestThread implements Runnable {

    private MongoCollection<Document> collection;
    private long value;

    public StressTestThread(MongoCollection<Document> collection, long value){
        this.collection = collection;
        this.value = value;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start = " + value);
        /*
        Get data from database using collection
        */
        Document document = collection.find(eq("id", "DocumentId: " + value)).first();
        System.out.println("Data: " + document.toJson());
        System.out.println(Thread.currentThread().getName() + " End = " + value);
    }
}
