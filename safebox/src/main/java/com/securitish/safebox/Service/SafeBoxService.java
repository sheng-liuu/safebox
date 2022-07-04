package com.securitish.safebox.Service;

import com.securitish.safebox.models.SafeBox;

import java.util.ArrayList;

public interface SafeBoxService {
    SafeBox createSafeBox(String name, String password);
    SafeBox findSafeBox(String id, String name);
    void openSafeBox(String id, String name);
    void addContent(String id, String name, ArrayList<String> newContents);
    SafeBox findSafeBoxByUser(String username);
}
