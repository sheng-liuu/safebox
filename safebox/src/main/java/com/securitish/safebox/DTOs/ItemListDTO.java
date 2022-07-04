package com.securitish.safebox.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.securitish.safebox.models.ItemList;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemListDTO {
    @NotNull(message = "Items is mandatory")
    private ArrayList<String> items;

    public ItemListDTO() {
    }

    public static ItemListDTO fromEntity(ItemList itemList) {
        ItemListDTO itemListDTO = new ItemListDTO();
        itemListDTO.setItems(itemList.getItems());

        return itemListDTO;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }
}
