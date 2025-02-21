package ru.k4nk.chronofocus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.k4nk.chronofocus.data.Tracker;
import ru.k4nk.chronofocus.services.ChronoTrackerService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/trackers")
@RequiredArgsConstructor
@Tag(name = "Трекеры", description = "Запуск, остановка и управление трекерами")
public class TrackerController {
    private final ChronoTrackerService chronoTrackerService;

//    @Operation(summary = "Запустить трекер для указанной категории")
//    @PostMapping("/start/{categoryId}")
//    public ResponseEntity<Tracker> startTracker(@PathVariable Integer categoryId) {
//        return ResponseEntity.ok(chronoTrackerService.start(categoryId));
//    }
//
//    @Operation(summary = "Остановить трекер по идентификатору")
//    @PostMapping("/stop/{trackerId}")
//    public ResponseEntity<Tracker> stopTracker(@PathVariable Integer trackerId) {
//        return ResponseEntity.ok(chronoTrackerService.stop(trackerId));
//    }
//
//    @Operation(summary = "Получить список трекеров за период")
//    @GetMapping
//    public ResponseEntity<List<Tracker>> getTrackers(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
//        return ResponseEntity.ok(chronoTrackerService.findByPeriod(from, to));
//    }
//
//    @Operation(summary = "Получить текущий активный трекер")
//    @GetMapping("/active")
//    public ResponseEntity<Tracker> getActiveTracker() {
//        return ResponseEntity.ok(chronoTrackerService.getActiveTracker());
//    }
}

