package com.CM.CookingMenu.Ingridient.controllers;

import com.CM.CookingMenu.Ingridient.entities.Ingridient;
import com.CM.CookingMenu.Ingridient.entities.IngridientDTO;
import com.CM.CookingMenu.Ingridient.services.IngridientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/ingridients")
public class IngridientController {
    private final IngridientService ingridientService;
    @GetMapping
    public ResponseEntity<List<IngridientDTO>> getAllIngridients(){
        return ResponseEntity.ok(ingridientService.getAllIngridients());
    }
    @PostMapping
    public ResponseEntity<String> addIngridient(@RequestBody IngridientDTO ingridientDTO){
        ingridientService.addIngridient(ingridientDTO);
        return ResponseEntity.ok("Ingridient Added.");
    }
}
