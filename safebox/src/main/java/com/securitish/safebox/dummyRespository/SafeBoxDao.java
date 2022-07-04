package com.securitish.safebox.dummyRespository;

import com.securitish.safebox.models.ItemList;
import com.securitish.safebox.models.SafeBox;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class SafeBoxDao {
    private static ArrayList<String> items = new ArrayList<>(
            Arrays.asList("Safebox content 01", "Safebox content 02", "Safebox content 03"));

    private static ArrayList<SafeBox> safeBoxes = new ArrayList<>(
            Arrays.asList(
                new SafeBox("1", "safebox1", "safebox1_123456", new ItemList(items)),
                new SafeBox("2", "safebox2", "safebox2_123456", new ItemList()),
                new SafeBox("3", "safebox3", "safebox3_123456", new ItemList())
            )
    );

    public List<SafeBox> getSafeBoxes() {
        return safeBoxes;
    }

    public SafeBox findSafeBox(String id, String name) {
        for (SafeBox box : safeBoxes) {
            if (box.getId().equals(id) && box.getName().equals(name)) {
                return box;
            }
        }
        return null;
    }

    public SafeBox findSafeBoxById(String id) {
        for (SafeBox box : safeBoxes) {
            if (box.getId().equals(id)) {
                return box;
            }
        }
        return null;
    }

    public SafeBox findSafeBoxByName(String name) {
        for (SafeBox box : safeBoxes) {
            if (box.getName().equals(name)) {
                return box;
            }
        }
        return null;
    }

    public void addSafeBox(SafeBox safeBox) {
        safeBoxes.add(safeBox);
    }
}
