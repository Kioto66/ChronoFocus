package ru.k4nk.chronofocus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.k4nk.chronofocus.data.ReportSummary;
import ru.k4nk.chronofocus.http_api.ApiBody;
import ru.k4nk.chronofocus.http_api.ApiHelper;
import ru.k4nk.chronofocus.jwt.JwtProvider;
import ru.k4nk.chronofocus.services.ChronoFocusService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
public class ReportsController extends AbstractController {
    private final ChronoFocusService chronoFocusService;

    public  ReportsController(ChronoFocusService chronoFocusService, JwtProvider jwtProvider) {
        super(jwtProvider);
        this.chronoFocusService = chronoFocusService;
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
        try {

            final LocalDateTime from = ApiHelper.parseStringDate(fromStr, true);
            final LocalDateTime to = (toStr != null) ? ApiHelper.parseStringDate(toStr, false)
                    : LocalDate.now().atTime(23, 59, 59);

            ReportSummary summary = chronoFocusService.getSummary(getUserLoginFromJwt(), from, to);
            return ResponseEntity.ok(new ApiBody<>(summary));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }
}
