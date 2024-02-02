package com.example.notes;


public class DataStoreModel {
    String title;
    String message;
    String id;

    public DataStoreModel(String title, String message, String id) {
        this.title = title;
        this.message = message;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public DataStoreModel() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataStoreModel(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
