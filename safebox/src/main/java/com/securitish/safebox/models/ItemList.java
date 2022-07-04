package com.securitish.safebox.models;


import java.util.ArrayList;

public class ItemList {
    private ArrayList<String> items = new ArrayList<>();

    public ItemList() {
    }

    public ItemList(ArrayList<String> items) {
        this.items = items;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public void addItems(ArrayList<String> items) {
        this.items.addAll(items);
    }
}
