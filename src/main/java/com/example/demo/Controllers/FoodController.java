package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.ResponseData;
import com.example.demo.Models.Food;
import com.example.demo.Services.FoodService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @PostMapping
    public ResponseEntity<ResponseData<Food>> create(@Valid @RequestBody Food food, Errors errors) {
        ResponseData<Food> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        responseData.setStatus(true);
        responseData.setPayload(foodService.create(food));
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Food>> update(@Valid @RequestBody Food food, @PathVariable Long id,
            Errors errors) {
        ResponseData<Food> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        responseData.setStatus(true);
        responseData.setPayload(foodService.update(food, id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Food>> findById(@PathVariable("id") Long id) {
        ResponseData<Food> responseData = new ResponseData<>();

        Food food = foodService.findById(id);
        if (food == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("Food not found");
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }

        responseData.setStatus(true);
        responseData.getMessage().add("Food found successfully");
        responseData.setPayload(food);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping
    public ResponseEntity<ResponseData<Iterable<Food>>> findAll() {
        ResponseData<Iterable<Food>> responseData = new ResponseData<>();
        responseData.setStatus(true);
        responseData.setPayload(foodService.findAll());
        responseData.getMessage().add("Foods retrieved successfully");
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> removeById(@PathVariable("id") Long id) {
        ResponseData<Void> responseData = new ResponseData<>();

        try {
            foodService.removeById(id);
            responseData.setStatus(true);
            responseData.getMessage().add("Food removed successfully");
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessage().add("Food not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> removeAll() {
        ResponseData<Void> responseData = new ResponseData<>();
        foodService.removeAll();
        responseData.setStatus(true);
        responseData.getMessage().add("All foods removed successfully");
        return ResponseEntity.ok(responseData);
    }
}
