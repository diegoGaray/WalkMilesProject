package com.mileston.walk.walkmilestonandroid.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class DataSteps {

    public String date;
    public String steps;
    public String kmrecorridos;
    public String wm;

    public DataSteps() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getKmrecorridos() {
        return kmrecorridos;
    }

    public void setKmrecorridos(String kmrecorridos) {
        this.kmrecorridos = kmrecorridos;
    }

    public String getWm() {
        return wm;
    }

    public void setWm(String wm) {
        this.wm = wm;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("data", date);
        result.put("steps", steps);
        result.put("kmrecorridos", kmrecorridos);
        result.put("wm", wm);

        return result;
    }
    //
}
