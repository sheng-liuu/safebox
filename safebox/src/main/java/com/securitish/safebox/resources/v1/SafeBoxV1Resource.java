package com.securitish.safebox.resources.v1;

import com.securitish.safebox.DTOs.ItemListDTO;
import com.securitish.safebox.DTOs.SafeBoxDTO;
import com.securitish.safebox.DTOs.TokenDTO;
import com.securitish.safebox.Service.SafeBoxService;
import com.securitish.safebox.Service.TokenService;
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
@RequestMapping("/api/v1/safebox")
public class SafeBoxV1Resource {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SafeBoxService safeBoxService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SafeBoxDTO> createSafeBox(@Valid @RequestBody SafeBoxDTO safeBoxDTO) {
        SafeBox safeBox1 =
                safeBoxService.createSafeBox(safeBoxDTO.getName(), safeBoxDTO.getPassword());
        return new ResponseEntity<SafeBoxDTO>(SafeBoxDTO.fromEntityOnlyId(safeBox1.getId()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/open", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity openSafeBox(@PathVariable("id") String id, @AuthenticationPrincipal User user) {
        safeBoxService.openSafeBox(id, user.getUsername());
        String token = tokenService.generateToken(user.getUsername());
        return new ResponseEntity<TokenDTO>(TokenDTO.fromEntity(token), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemListDTO> getContent(@PathVariable("id") String id, @AuthenticationPrincipal User user) {
        SafeBox safeBox1 = safeBoxService.findSafeBox(id, user.getUsername());
        if(safeBox1 == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity<ItemListDTO>(ItemListDTO.fromEntity(safeBox1.getItems()), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addContent(@PathVariable("id") String id, @AuthenticationPrincipal User user, @Valid @RequestBody ItemListDTO itemListDTO) {
        safeBoxService.addContent(id, user.getUsername(), itemListDTO.getItems());
        return new ResponseEntity(HttpStatus.OK);
    }
}