package ru.k4nk.chronofocus.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.k4nk.chronofocus.data.Category;
import ru.k4nk.chronofocus.data.ReportSummary;
import ru.k4nk.chronofocus.data.Tracker;
import ru.k4nk.chronofocus.http_api.ApiBody;
import ru.k4nk.chronofocus.http_api.ApiHelper;
import ru.k4nk.chronofocus.http_api.ApiVersion;
import ru.k4nk.chronofocus.http_api.JsonMapper;
import ru.k4nk.chronofocus.services.WorkTrackerService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@Log4j2
public class ChronoTrackerController {
    private final WorkTrackerService workTrackerService;
    private final JsonMapper jsonMapper;

    private static LocalDateTime parseDateTimeOrNow(String dateTimeStr, LocalDateTime now) {
        LocalDateTime dateTime;
        if (dateTimeStr == null) {
            dateTime = now;
        } else {
            dateTime = ApiHelper.parseStringDateTime(dateTimeStr, false);
        }
        return dateTime;
    }

    @GetMapping("/current_tracker")
    public ResponseEntity<ApiBody> getCurrentTracker() {
        Optional<Tracker> trackerOpt = workTrackerService.findCurrentTracker();

        final JsonNode payload = jsonMapper.listToJson(trackerOpt.stream().toList());
        return ResponseEntity.ok(ApiBody.builder()
                .apiVer(ApiVersion.VERSION_1.toString())
                .status(ApiBody.Status.OK)
                .payload(payload)
                .build());
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiBody> getSummary(@RequestParam(name = "from") String fromStr,
                                              @RequestParam(name = "to", required = false) String toStr) {

        LocalDateTime from = ApiHelper.parseStringDate(fromStr, true);
        LocalDateTime to;
        if (toStr != null) {
            to = ApiHelper.parseStringDate(toStr, false);
        } else {
            to = LocalDate.now().atTime(23, 59, 59);
        }

        ReportSummary reportSummary = workTrackerService.getSummary(from, to);

        final JsonNode payload = jsonMapper.listToJson(List.of(reportSummary));
        return ResponseEntity.ok(ApiBody.builder()
                .apiVer(ApiVersion.VERSION_1.toString())
                .status(ApiBody.Status.OK)
                .payload(payload)
                .build());
    }

    @GetMapping("/category")
    public ResponseEntity<ApiBody> getCategories(@RequestParam(name = "id", required = false) List<Integer> ids) {
        List<Category> resultList = workTrackerService.getCategories(ids);

        final JsonNode payload = jsonMapper.listToJson(List.of(resultList));
        return ResponseEntity.ok(ApiBody.builder()
                .apiVer(ApiVersion.VERSION_1.toString())
                .status(ApiBody.Status.OK)
                .payload(payload)
                .build());
    }

    @PostMapping("/category")
    public ResponseEntity<ApiBody> postCategory(@RequestBody ApiBody body) {
        if (!body.apiVer().equals(ApiVersion.VERSION_1.toString())) {
            return ResponseEntity.badRequest().body(
                    ApiBody.builder()
                            .apiVer(ApiVersion.VERSION_1.toString())
                            .status(ApiBody.Status.OK)
                            .payload(jsonMapper.emptyArray())
                            .build());
        }
        List<Category> categories = jsonMapper.jsonToList((ArrayNode) body.payload(), Category.class);
        List<Category> result = workTrackerService.saveAllCategories(categories);
        return ResponseEntity.ok(ApiBody.builder()
                .apiVer(ApiVersion.VERSION_1.toString())
                .status(ApiBody.Status.OK)
                .payload(jsonMapper.listToJson(result))
                .build());
    }

    @GetMapping("/start")
    public ResponseEntity<ApiBody> startTracking(
            @RequestParam(name = "category_id") Integer categoryId,
            @RequestParam(name = "date_time", required = false) String fromStr) {
        LocalDateTime from = parseDateTimeOrNow(fromStr, LocalDateTime.now());

        Tracker tracker = workTrackerService.startTracking(categoryId, from);

        final JsonNode payload = jsonMapper.listToJson(List.of(tracker));
        return ResponseEntity.ok(ApiBody.builder()
                .apiVer(ApiVersion.VERSION_1.toString())
                .status(ApiBody.Status.OK)
                .payload(payload)
                .build());
    }

    @GetMapping("/stop")
    public ResponseEntity<ApiBody> stopTracking(
            @RequestParam(name = "category_id") Integer categoryId,
            @RequestParam(name = "date_time", required = false) String dateTimeStr) {
        LocalDateTime dateTime = parseDateTimeOrNow(dateTimeStr, LocalDateTime.now());

        Tracker tracker = workTrackerService.stopTracking(categoryId, dateTime);

        final JsonNode payload = jsonMapper.listToJson(List.of(tracker));
        return ResponseEntity.ok(ApiBody.builder()
                .apiVer(ApiVersion.VERSION_1.toString())
                .status(ApiBody.Status.OK)
                .payload(payload)
                .build());
    }

    private LocalDateTime parseDateOrNow(String dateStr) {
        LocalDateTime dateTime = ApiHelper.parseStringDate(dateStr, false);
        return Objects.requireNonNullElseGet(dateTime, () -> LocalDate.now().atTime(23, 59, 59));
    }

}
