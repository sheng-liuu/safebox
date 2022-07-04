package com.securitish.safebox.Service;

import com.securitish.safebox.dummyRespository.SafeBoxDao;
import com.securitish.safebox.models.SafeBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static com.securitish.safebox.ApplicationConstants.*;

@Service
public class SafeBoxServiceImpl implements SafeBoxService{
    @Autowired
    private SafeBoxDao safeBoxDao;

    @Override
    public SafeBox createSafeBox(String name, String password) {
        // check either safebox exists or not
        SafeBox safeBox = safeBoxDao.findSafeBoxByName(name);
        if(safeBox != null) throw new ResponseStatusException(HttpStatus.CONFLICT, SAFEBOX_ALREADY_EXISTS);
        // ensure the password is safe enough
        checkPasswordSecurity(password);
        // generate a new id
        String id = String.valueOf(safeBoxDao.getSafeBoxes().size() + 1);
        // create a new safebox
        safeBox = new SafeBox();
        safeBox.setId(id);
        safeBox.setName(name);
        safeBox.setPassword(password);
        safeBoxDao.addSafeBox(safeBox);
        return safeBox;
    }

    @Override
    public SafeBox findSafeBox(String id, String name) {
        SafeBox safeBox = safeBoxDao.findSafeBoxById(id);
        if(safeBox == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, SAFEBOX_NOT_EXISTS);
        // check if safe box is locked
        if(safeBox.getAttempt() >= 3)
            throw new ResponseStatusException(HttpStatus.LOCKED, SAFEBOX_LOCKED);
        if(!safeBox.getName().equals(name))
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, WRONG_COMBINATION_OF_ID_AND_PASSWORD);

        return safeBox;
    }

    @Override
    public void openSafeBox(String id, String name) {
        SafeBox safeBox = safeBoxDao.findSafeBoxById(id);
        if(safeBox == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, SAFEBOX_NOT_EXISTS);
        // check if safe box is locked
        if(safeBox.getAttempt() >= 3)
            throw  new ResponseStatusException(HttpStatus.LOCKED, SAFEBOX_LOCKED);
        // block the safe box when attempt as a wrong combination between safebox id and password
        if(!safeBox.getName().equals(name)) {
            safeBox.addAttempt();
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, WRONG_COMBINATION_OF_ID_AND_PASSWORD);
        }
    }

    @Override
    public void addContent(String id, String name, ArrayList<String> newContents) {
        SafeBox safeBox = findSafeBox(id, name);
        safeBox.getItems().addItems(newContents);
    }

    @Override
    public SafeBox findSafeBoxByUser(String username) {
        return safeBoxDao.findSafeBoxByName(username);
    }

    private void checkPasswordSecurity(String password) {
        // number
        String REG_NUMBER = ".*\\d+.*";
        //uppercase characters
        String REG_UPPERCASE = ".*[A-Z]+.*";
        //lowercase characters
        String REG_LOWERCASE = ".*[a-z]+.*";
        //special characters
        String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";

        if(password.length() < 8
            || !password.matches(REG_NUMBER)
            || !password.matches(REG_LOWERCASE)
            || !password.matches(REG_UPPERCASE)
            || !password.matches(REG_SYMBOL))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PASSWORD_UNSAFE);
    }
}
