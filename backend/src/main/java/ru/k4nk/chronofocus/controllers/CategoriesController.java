package ru.k4nk.chronofocus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.k4nk.chronofocus.data.Category;
import ru.k4nk.chronofocus.http_api.ApiBody;
import ru.k4nk.chronofocus.jwt.JwtProvider;
import ru.k4nk.chronofocus.services.ChronoFocusService;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
@CrossOrigin(origins = "http://localhost:5173")
@Log4j2
public class CategoriesController extends AbstractController {
    private final ChronoFocusService chronoFocusService;

    public CategoriesController(ChronoFocusService chronoFocusService, JwtProvider jwtProvider) {
        super(jwtProvider);
        this.chronoFocusService = chronoFocusService;
    }


    @Operation(summary = "Получить категории", description = "Возвращает список категорий")
    @ApiResponse(responseCode = "200", description = "Категории успешно получены")
    @GetMapping
    public ResponseEntity<ApiBody<Category>> getCategories(
            @Parameter(description = "Список ID категорий (необязательно)")
            @RequestParam(name = "id", required = false) List<Integer> ids) {
        try {
            return ResponseEntity.ok(new ApiBody<>(chronoFocusService.getCategories(getUserLoginFromJwt(), ids)));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(summary = "Создать категории", description = "Создает новые категории")
    @ApiResponse(responseCode = "201", description = "Категории успешно созданы")
    @ApiResponse(responseCode = "400", description = "Некорректный формат JSON")
    @PostMapping
    public ResponseEntity<ApiBody<Category>> postCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Тело запроса с данными категорий", required = true,
                    content = @Content(schema = @Schema(implementation = ApiBody.class)))
            @RequestBody ApiBody<Category> body) {
        List<Category> payload = body.getPayload();
        String login = getUserLoginFromJwt();
        List<Category> categories = chronoFocusService.saveAllCategories(
                chronoFocusService.injectUserByLogin(login, payload));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiBody<>(categories));
    }
}
