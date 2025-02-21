package ru.k4nk.chronofocus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.k4nk.chronofocus.data.ReportSummary;
import ru.k4nk.chronofocus.services.ChronoTrackerService;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Tag(name = "Отчёты", description = "Генерация отчётов о времени")
public class ReportController {
    private final ChronoTrackerService reportService;

//    @Operation(summary = "Получить отчёт за период")
//    @GetMapping
//    public ResponseEntity<ReportSummary> getReport(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
//        return ResponseEntity.ok(reportService.generateReport(from, to));
//    }
}

