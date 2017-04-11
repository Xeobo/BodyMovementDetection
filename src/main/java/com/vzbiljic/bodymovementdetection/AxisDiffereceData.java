package com.vzbiljic.bodymovementdetection;

import com.orm.SugarRecord;

/**
 * Created by vzbiljic on 10.4.17..
 */

public class AxisDiffereceData extends SugarRecord {
    private float x;
    private float y;
    private float z;
    private int label = Label.STANDING;

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

    public static class Label{
        public static final  int STANDING = 0;
        public static final  int WALKING = 1;
        public static final  int RUNNING = 2;
    }
}