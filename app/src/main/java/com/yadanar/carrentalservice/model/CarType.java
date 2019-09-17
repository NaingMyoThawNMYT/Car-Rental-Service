package com.yadanar.carrentalservice.model;

import com.google.firebase.database.DataSnapshot;

public class CarType {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static CarType parseCarType(DataSnapshot snapshot) {
        CarType type = new CarType();
        type.setId(snapshot.getKey());
        type.setName(snapshot.getValue().toString());
        return type;
    }
}
