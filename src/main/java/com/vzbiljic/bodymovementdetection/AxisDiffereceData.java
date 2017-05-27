package com.vzbiljic.bodymovementdetection;

import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.orm.SugarRecord;

/**
 * Created by vzbiljic on 10.4.17..
 */

public class AxisDiffereceData extends SugarRecord {
    private float x;
    private float y;
    private float z;
    private int label = Label.STANDING;

    private void logToMongo() {
        Log.i("AxisDiffereceData","Logged to mongo");
        MongoDBUtils.getInstance().logToDB(this);
    }
    public AxisDiffereceData(){}

    public AxisDiffereceData(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AxisDiffereceData(float x, float y, float z, int label) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.label = label;
    }



    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "AxisDiffereceData{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", label=" + label +
                '}';
    }

    @Override
    public long save() {
        logToMongo();
        return super.save();
    }

    public static void deleteAllData(){
        SugarRecord.deleteAll(AxisDiffereceData.class);
        MongoDBUtils.getInstance().deleteAll();
    }


    public static class Label{
        public static final  int STANDING = 0;
        public static final  int WALKING = 1;
        public static final  int RUNNING = 2;
    }
}
