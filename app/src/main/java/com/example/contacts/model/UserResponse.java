package com.example.contacts.model;

import java.util.List;

public class UserResponse {
    public List<USerDetailsPojo> getData() {
        return data;
    }

    public void setData(List<USerDetailsPojo> data) {
        this.data = data;
    }

    private List<USerDetailsPojo> data;
}
