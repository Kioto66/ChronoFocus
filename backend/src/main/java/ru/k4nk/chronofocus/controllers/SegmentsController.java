package ru.k4nk.chronofocus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.k4nk.chronofocus.data.TimeSegment;
import ru.k4nk.chronofocus.http_api.ApiBody;
import ru.k4nk.chronofocus.http_api.ApiHelper;
import ru.k4nk.chronofocus.jwt.JwtProvider;
import ru.k4nk.chronofocus.services.ChronoFocusService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/segments")
public class SegmentsController extends AbstractController {
    private final ChronoFocusService chronoFocusService;

    public  SegmentsController(ChronoFocusService chronoFocusService, JwtProvider jwtProvider) {
        super(jwtProvider);
        this.chronoFocusService = chronoFocusService;
    }

    @Operation(summary = "Получить отрезки времени", description = "Возвращает список отрезков времени за указанный период")
    @ApiResponse(responseCode = "200", description = "Отрезки времени успешно получены")
    @ApiResponse(responseCode = "400", description = "Некорректный формат даты")
    @GetMapping
    public ResponseEntity<ApiBody<TimeSegment>> segmentsGet(
            @Parameter(description = "Начальная дата в формате yyyy-MM-dd", required = true)
            @RequestParam(name = "from") String fromStr,
            @Parameter(description = "Конечная дата в формате yyyy-MM-dd", required = true)
            @RequestParam(name = "to") String toStr
    ) {
        final LocalDateTime from = ApiHelper.parseStringDate(fromStr, true);
        final LocalDateTime to = (toStr != null) ?
                ApiHelper.parseStringDate(toStr, false)
                : LocalDate.now().atTime(23, 59, 59);
        String login = getUserLoginFromJwt();
        List<TimeSegment> timeSegments = chronoFocusService
                .findAllSegmentsBetween(login, from, to);
        return ResponseEntity.ok(new ApiBody<>(timeSegments));
    }


    @Operation(summary = "Создать отрезки времени", description = "Создает новые отрезки времени")
    @ApiResponse(responseCode = "200", description = "Отрезки времени успешно созданы")
    @ApiResponse(responseCode = "400", description = "Некорректный формат JSON")
    @ApiResponse(responseCode = "403", description = "Данные принадлежат другому пользователю")
    @PostMapping
    public ResponseEntity<ApiBody<TimeSegment>> segmentsPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Тело запроса с данными отрезков", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ApiBody.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "payload": [
                                    {
                                      "id": 1,
                                      "startTimestamp": "2025-03-07_09:34:37",
                                      "endTimestamp": "2025-03-07_17:42:26",
                                      "category": {
                                        "id": 1,
                                        "name": "Работа",
                                        "color": "#ffffff",
                                        "icon": "some.ico"
                                      }
                                    }
                                  ],
                                  "details": "Запрос выполнен успешно"
                                }
                                """
                            )
                    )
            )
            @RequestBody ApiBody<TimeSegment> body) {
        List<TimeSegment> payload = body.getPayload();
        String login = getUserLoginFromJwt();
        try {
            List<TimeSegment> timeSegments = chronoFocusService
                    .saveAllSegments(login, payload);

            return ResponseEntity.ok(new ApiBody<>(timeSegments));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ApiBody<TimeSegment> responseBody = new ApiBody<>(new ArrayList<>());
            responseBody.setDetails(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
    }


    @Operation(summary = "Получить текущие активные трекеры", description = "Возвращает список текущих активных трекеров")
    @ApiResponse(responseCode = "200", description = "Трекеры найдены")
    @ApiResponse(responseCode = "204", description = "Активные трекеры не найдены")
    @GetMapping("/active")
    public ResponseEntity<ApiBody<TimeSegment>> getActiveSegments() {
        final String login = getUserLoginFromJwt();
        List<TimeSegment> timeSegments = chronoFocusService.findActiveSegments(login);
        return ResponseEntity.status(timeSegments.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .body(new ApiBody<>((timeSegments)));
    }

    @Operation(summary = "Начать отслеживание", description = "Начинает отслеживание времени для указанной категории")
    @ApiResponse(responseCode = "200", description = "Отслеживание успешно начато")
    @ApiResponse(responseCode = "400", description = "Некорректный формат даты")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @GetMapping("/start")
    public ResponseEntity<ApiBody<TimeSegment>> startTracking(
            @Parameter(description = "ID категории", required = true)
            @RequestParam(name = "category_id") Integer categoryId,
            @Parameter(description = "Дата и время начала в формате yyyy-MM-dd_HH:mm (необязательно)")
            @RequestParam(name = "date_time", required = false) String fromStr) {

        try {
            final String login = getUserLoginFromJwt();
            TimeSegment timeSegment = chronoFocusService.startTracking(login, categoryId,
                    parseDateTimeOrNow(fromStr, LocalDateTime.now()));
            return ResponseEntity.ok(new ApiBody<>(timeSegment));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(summary = "Остановить отслеживание", description = "Останавливает отслеживание времени для указанной категории")
    @ApiResponse(responseCode = "200", description = "Отслеживание успешно остановлено")
    @ApiResponse(responseCode = "400", description = "Некорректный формат даты")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @ApiResponse(responseCode = "204", description = "Нет активного трекера для указанной категории")
    @GetMapping("/stop")
    public ResponseEntity<ApiBody<TimeSegment>> stopTracking(
            @Parameter(description = "ID категории", required = true)
            @RequestParam(name = "category_id") Integer categoryId,
            @Parameter(description = "Дата и время окончания в формате yyyy-MM-dd_HH:mm (необязательно)")
            @RequestParam(name = "date_time", required = false) String dateTimeStr) {
        LocalDateTime dateTime = parseDateTimeOrNow(dateTimeStr, LocalDateTime.now());

        try {
            final String login = getUserLoginFromJwt();
            TimeSegment timeSegment = chronoFocusService.stopTracking(login, categoryId, dateTime);
            return ResponseEntity.ok(new ApiBody<>(timeSegment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiBody<>(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiBody<>(e.getMessage()));
        }
    }

}
