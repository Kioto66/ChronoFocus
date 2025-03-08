package ru.k4nk.chronofocus.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.k4nk.chronofocus.data.*;
import ru.k4nk.chronofocus.data.sys.User;
import ru.k4nk.chronofocus.data.sys.UserRepository;
import ru.k4nk.chronofocus.exceptions.AuthException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ChronoFocusService {
    private final CategoryRepository categoryRepository;
    private final TimeSegmentRepository timeSegmentRepository;
    private final UserRepository userRepository;

    public ReportSummary getSummary(String login, LocalDateTime from, LocalDateTime to) {
        List<TimeSegment> segments = timeSegmentRepository.findSegmentsByLoginAndTimestamps(login, from, to);
        final Map<Integer, ReportSummary.ReportEntry> entries = new HashMap<>();
        for (TimeSegment timeSegment : segments) {
            final Integer categoryId = timeSegment.getCategory().getId();

            ReportSummary.ReportEntry reportEntry = entries.get(categoryId);
            if (reportEntry == null) {
                reportEntry = new ReportSummary.ReportEntry(timeSegment.getCategory(), Duration.ZERO);
                entries.put(categoryId, reportEntry);
            }
            final Duration newPiece = Duration.between(timeSegment.getStartTimestamp(),
                    timeSegment.getEndTimestamp() != null ? timeSegment.getEndTimestamp() : LocalDateTime.now());
            reportEntry.setDuration(reportEntry.getDuration().plus(newPiece));
        }

        return new ReportSummary(from, to, LocalDateTime.now(), new ArrayList<>(entries.values()));
    }

    public List<Category> saveAllCategories(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    @Transactional
    public TimeSegment startTracking(String login, Integer categoryId, @NonNull LocalDateTime dateTime) throws IllegalArgumentException, EntityNotFoundException {
        Category category = categoryRepository.findAllByLoginAndId(login, categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find category with id = " + categoryId));

        TimeSegment timeSegment = new TimeSegment(category, dateTime);
        return timeSegmentRepository.save(timeSegment);
    }

    @Transactional
    public TimeSegment stopTracking(String login, Integer categoryId, @NonNull LocalDateTime dateTime) throws IllegalArgumentException, EntityNotFoundException {

        Category category = categoryRepository.findAllByLoginAndId(login, categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find category with id = " + categoryId
                                                                + " and user login = " + login));

        TimeSegment timeSegment = timeSegmentRepository
                .findByCategoryAndStartTimestampBeforeAndEndTimestampIsNull(category, dateTime)
                .orElseThrow(() ->
                        new EntityNotFoundException("Запущенный трекер в категории " + categoryId + " не найден"));

        timeSegment.setEndTimestamp(dateTime);
        return timeSegmentRepository.save(timeSegment);
    }

    public List<Category> getCategories(String userLogin, List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return categoryRepository.findAllByLogin(userLogin);
        }
        return categoryRepository.findAllByLoginAndId(userLogin, ids);
    }

    public List<TimeSegment> findAllSegmentsBetween(String login, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        return timeSegmentRepository.findSegmentsByLoginAndTimestamps(login, fromDateTime, toDateTime);
    }

    public List<Category> injectUserByLogin(String login, List<Category> categories) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AuthException("Пользователь " + login + "не найден!"));
        categories.forEach(category -> category.setUser(user));
        return categories;
    }

    @Transactional
    public List<TimeSegment> saveAllSegments(String login, List<TimeSegment> payload) throws IllegalArgumentException {
        //проверить, что все пришедшие с id сегменты принадлежат пользователю
        List<TimeSegment> payloadCopy = new ArrayList<>(payload);
        final List<Category> categories = new ArrayList<>(payloadCopy.size());
        for (final TimeSegment timeSegment : payload) {
            categories.add(timeSegment.getCategory());
        }
        Set<TimeSegment> invalidCategories = categoryRepository.findAllCategoriesBelongToOtherLogin(login, categories);
        if (!invalidCategories.isEmpty()) {
            throw new IllegalArgumentException("Некоторые отрезки принадлежат другому пользователю");
        }

        for (TimeSegment timeSegment : payload) {
            timeSegmentRepository.saveAndFlush(timeSegment);
        }
        return timeSegmentRepository.saveAll(payload);
    }

    public List<TimeSegment> findActiveSegments(String login) {
        return timeSegmentRepository.findAllByLoginAndEndTimestampIsNull(login);
    }
}
