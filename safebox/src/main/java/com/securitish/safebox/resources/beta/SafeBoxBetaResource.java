package com.securitish.safebox.resources.beta;

import com.securitish.safebox.DTOs.ItemListDTO;
import com.securitish.safebox.DTOs.SafeBoxDTO;
import com.securitish.safebox.Service.SafeBoxService;
import com.securitish.safebox.models.SafeBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/beta/safebox")
public class SafeBoxBetaResource {
    @Autowired
    private SafeBoxService safeBoxService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createSafeBox(@Valid @RequestBody SafeBoxDTO safeBoxDTO) {
        SafeBox safeBox =
        safeBoxService.createSafeBox(safeBoxDTO.getName(), safeBoxDTO.getPassword());
        return new ResponseEntity<SafeBoxDTO>(SafeBoxDTO.fromEntityOnlyId(safeBox.getId()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getContent(@PathVariable("id") String id, @AuthenticationPrincipal User user) {
        SafeBox safeBox1 = safeBoxService.findSafeBox(id, user.getUsername());
        return new ResponseEntity<ItemListDTO>(ItemListDTO.fromEntity(safeBox1.getItems()), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addContent(@PathVariable("id") String id, @AuthenticationPrincipal User user, @Valid @RequestBody ItemListDTO itemListDTO) {
        safeBoxService.addContent(id, user.getUsername(), itemListDTO.getItems());
        return new ResponseEntity(HttpStatus.OK);
    }
}