package com.securitish.safebox.DTOs;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.securitish.safebox.models.ItemList;
import com.securitish.safebox.models.SafeBox;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SafeBoxDTO {
    private String id;

    @NotNull(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Password is mandatory")
    private String password;

    private ItemList item;

    public SafeBoxDTO() {}

    public static SafeBoxDTO fromEntity(SafeBox safeBox) {
        SafeBoxDTO safeBoxDTO = new SafeBoxDTO();
        if(safeBox.getId() != null) safeBoxDTO.setId(safeBox.getId());
        if(safeBox.getName() != null) safeBoxDTO.setName(safeBox.getName());
        if(safeBox.getPassword() != null) safeBoxDTO.setPassword(safeBox.getPassword());
        if(safeBox.getItems() != null) safeBoxDTO.setItem(safeBox.getItems());

        return safeBoxDTO;
    }

    public static SafeBoxDTO fromEntityOnlyId(String id) {
        SafeBoxDTO safeBoxDTO = new SafeBoxDTO();
        safeBoxDTO.setId(id);

        return safeBoxDTO;
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

    public ItemList getItem() {
        return item;
    }

    public void setItem(ItemList item) {
        this.item = item;
    }
}
