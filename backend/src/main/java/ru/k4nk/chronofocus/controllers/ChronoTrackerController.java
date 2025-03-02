package ru.k4nk.chronofocus.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.k4nk.chronofocus.data.Category;
import ru.k4nk.chronofocus.data.ReportSummary;
import ru.k4nk.chronofocus.data.Tracker;
import ru.k4nk.chronofocus.http_api.ApiBody;
import ru.k4nk.chronofocus.http_api.ApiHelper;
import ru.k4nk.chronofocus.services.ChronoTrackerService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@Log4j2
@Tag(name = "ChronoTracker API", description = "API для управления трекерами времени и категориями")
public class ChronoTrackerController {
    private final ChronoTrackerService chronoTrackerService;

    @Operation(summary = "Получить текущий трекер", description = "Возвращает текущий активный трекер")
    @ApiResponse(responseCode = "200", description = "Трекер найден")
    @ApiResponse(responseCode = "204", description = "Активный трекер не найден")
    @GetMapping("/current_tracker")

    public ResponseEntity<ApiBody<Tracker>> getCurrentTracker() {
        return chronoTrackerService.findCurrentTracker()
                .map(tracker -> ResponseEntity.ok(new ApiBody<>((tracker))))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Получить сводку", description = "Возвращает сводку за указанный период")
    @ApiResponse(responseCode = "200", description = "Сводка успешно получена")
    @ApiResponse(responseCode = "400", description = "Некорректный формат даты")
    @GetMapping(value = "/summary", produces = "application/json")
    public ResponseEntity<ApiBody<ReportSummary>> getSummary(
            @Parameter(description = "Начальная дата в формате yyyy-MM-dd", required = true)
            @RequestParam(name = "from") String fromStr,
            @Parameter(description = "Конечная дата в формате yyyy-MM-dd (необязательно)")
            @RequestParam(name = "to", required = false) String toStr) {
        final LocalDateTime from = ApiHelper.parseStringDate(fromStr, true);
        final LocalDateTime to = (toStr != null) ? ApiHelper.parseStringDate(toStr, false)
                : LocalDate.now().atTime(23, 59, 59);
        ReportSummary summary = chronoTrackerService.getSummary(from, to);
        return ResponseEntity.ok(new ApiBody<>(summary));

    }

    @Operation(summary = "Получить категории", description = "Возвращает список категорий")
    @ApiResponse(responseCode = "200", description = "Категории успешно получены")
    @GetMapping("/category")

    public ResponseEntity<ApiBody<Category>> getCategories(
            @Parameter(description = "Список ID категорий (необязательно)")
            @RequestParam(name = "id", required = false) List<Integer> ids) {
        return ResponseEntity.ok(new ApiBody<>(chronoTrackerService.getCategories(ids)));
    }

    @Operation(summary = "Создать категории", description = "Создает новые категории")
    @ApiResponse(responseCode = "201", description = "Категории успешно созданы")
    @ApiResponse(responseCode = "400", description = "Некорректный формат JSON")
    @PostMapping("/category")

    public ResponseEntity<ApiBody<Category>> postCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Тело запроса с данными категорий", required = true,
                    content = @Content(schema = @Schema(implementation = ApiBody.class)))
            @RequestBody ApiBody<Category> body) {
        List<Category> payload = body.getPayload();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiBody<>(chronoTrackerService.saveAllCategories(payload)));
    }

    @Operation(summary = "Начать отслеживание", description = "Начинает отслеживание времени для указанной категории")
    @ApiResponse(responseCode = "200", description = "Отслеживание успешно начато")
    @ApiResponse(responseCode = "400", description = "Некорректный формат даты")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @GetMapping("/start")
    public ResponseEntity<ApiBody<Tracker>> startTracking(
            @Parameter(description = "ID категории", required = true)
            @RequestParam(name = "category_id") Integer categoryId,
            @Parameter(description = "Дата и время начала в формате yyyy-MM-dd_HH:mm (необязательно)")
            @RequestParam(name = "date_time", required = false) String fromStr) {

        Tracker tracker = chronoTrackerService.startTracking(categoryId, parseDateTimeOrNow(fromStr, LocalDateTime.now()));
        return ResponseEntity.ok(new ApiBody<>(tracker));
    }

    @Operation(summary = "Остановить отслеживание", description = "Останавливает отслеживание времени для указанной категории")
    @ApiResponse(responseCode = "200", description = "Отслеживание успешно остановлено")
    @ApiResponse(responseCode = "400", description = "Некорректный формат даты")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @ApiResponse(responseCode = "204", description = "Нет активного трекера для указанной категории")
    @GetMapping("/stop")
    public ResponseEntity<ApiBody<Tracker>> stopTracking(
            @Parameter(description = "ID категории", required = true)
            @RequestParam(name = "category_id") Integer categoryId,
            @Parameter(description = "Дата и время окончания в формате yyyy-MM-dd_HH:mm (необязательно)")
            @RequestParam(name = "date_time", required = false) String dateTimeStr) {
        LocalDateTime dateTime = parseDateTimeOrNow(dateTimeStr, LocalDateTime.now());

        try {
            Tracker tracker = chronoTrackerService.stopTracking(categoryId, dateTime);
            return ResponseEntity.ok(new ApiBody<>(tracker));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiBody<>(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiBody<>(e.getMessage()));
        }
    }

    private LocalDateTime parseDateTimeOrNow(String dateTimeStr, LocalDateTime now) {
        return (dateTimeStr == null) ? now : ApiHelper.parseStringDateTime(dateTimeStr, false);
    }
}
