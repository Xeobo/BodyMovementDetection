package com.vzbiljic.bodymovementdetection;

import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.nio.Buffer;

/**
 * Created by Xeobo on 5/27/2017.
 */

public class MongoDBUtils {
    private static MongoDBUtils instance;
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> axis;
    private MongoDBUtils(){

        MongoClientURI uri  = new MongoClientURI("mongodb://xeobo:xeobo@ds155841.mlab.com:55841/body-movement-detection");
        client = new MongoClient(uri);
        db = client.getDatabase(uri.getDatabase());
        axis = db.getCollection("axis-difference");
    }

    public static MongoDBUtils getInstance() {
        if (null == instance) {
            instance = new MongoDBUtils();
        }
        return instance;
    }

    public void logToDB(AxisDiffereceData dt){
        final AxisDiffereceData data = dt;
        Thread task = new Thread() {
            @Override
            public void run() {
                try {
                    axis.insertOne(new Document("x", data.getX())
                            .append("y", data.getY())
                            .append("z", data.getZ())
                            .append("label", data.getLabel())
                    );
                } catch (Throwable t) {
                    Log.i("MongoDBUtils", "Not able to insert data");
                }
            }
        };

        task.start();

    }

    public void deleteAll(){
        Thread task = new Thread() {
            @Override
            public void run() {
                try{
                    axis.drop();
                }catch(Throwable t){
                    Log.i("MongoDBUtils", "Not able to erase database data");
                }
            }
        };

        task.start();
    }


    public static void deinit() {
        if(null != instance) {
            instance.client.close();
            instance = null;
        }
    }
}
