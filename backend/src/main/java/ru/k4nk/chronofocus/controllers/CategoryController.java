package ru.k4nk.chronofocus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.k4nk.chronofocus.data.Category;
import ru.k4nk.chronofocus.services.ChronoTrackerService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Категории", description = "CRUD-операции для категорий")
public class CategoryController {
    private final ChronoTrackerService chronoTrackerService;
//
//    @Operation(summary = "Создать новую категорию")
//    @PostMapping
//    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
//        return ResponseEntity.ok(chronoTrackerService.create(category));
//    }
//
//    @Operation(summary = "Получить список всех категорий")
//    @GetMapping
//    public ResponseEntity<List<Category>> getAllCategories() {
//        return ResponseEntity.ok(chronoTrackerService.findAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
//        Category category = chronoTrackerService.getCategoryById(id);
//        if (category != null) {
//            return new ResponseEntity<>(category, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Operation(summary = "Удалить категорию")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
//        chronoTrackerService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
}
