package com.securitish.safebox.models;

public class SafeBox {
    private String id;

    private String name;

    private String password;

    private ItemList items = new ItemList();

    private int attempt = 0;

    public SafeBox() {

    }

    public SafeBox(String id, String name, String password, ItemList items) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.items = items;
        this.attempt = 0;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ItemList getItems() {
        return items;
    }

    public void setItems(ItemList items) {
        this.items = items;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public void addAttempt() {
        this.attempt++;
    }
}
