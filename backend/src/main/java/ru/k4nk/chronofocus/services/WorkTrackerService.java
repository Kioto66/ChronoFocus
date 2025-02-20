package ru.k4nk.chronofocus.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.k4nk.chronofocus.data.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WorkTrackerService {
    private final CategoryRepository categoryRepository;
    private final TrackersRepository trackersRepository;

    public ReportSummary getSummary(LocalDateTime from, LocalDateTime to) {
        List<Tracker> summary = trackersRepository.getSummary(from, to);
        HashMap<Category, ReportSummary.ReportEntry> entries = new HashMap<>();
        summary.forEach(tracker -> {
            ReportSummary.ReportEntry reportEntry = entries.get(tracker.getCategory());
            if (reportEntry == null) {
                reportEntry = new ReportSummary.ReportEntry(tracker.getCategory(), Duration.ZERO);
                entries.put(tracker.getCategory(), reportEntry);
            }
            final Duration newPiece = Duration.between(tracker.getStartTimestamp(), tracker.getStopTimestamp());
            reportEntry.setDuration(reportEntry.getDuration().plus(newPiece));
        });

        return new ReportSummary(from, to, LocalDateTime.now(), new ArrayList<>(entries.values()));
    }

    public List<Category> saveAllCategories(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    @Transactional
    public Tracker startTracking(Integer categoryId, LocalDateTime from) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find category with id = " + categoryId));

        trackersRepository.findByCategoryAndStopTimestampIsNull(category).ifPresent(tracker -> {
            tracker.setStopTimestamp(LocalDateTime.now());
            trackersRepository.save(tracker);
        });
        Tracker tracker = new Tracker(category, LocalDateTime.now());
        return trackersRepository.save(tracker);
    }

    @Transactional
    public Tracker stopTracking(Integer categoryId, LocalDateTime dateTime) {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Could not find category with id = " + categoryId));

        Tracker tracker = trackersRepository.findByCategoryAndStartTimestampBeforeAndStopTimestampIsNull(category, dateTime)
                .orElseThrow(() -> new EntityNotFoundException("Запущенный трекер в категории " + categoryId + " не найден"));

        tracker.setStopTimestamp(dateTime);
        return trackersRepository.save(tracker);
    }

    public List<Category> getCategories(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return categoryRepository.findAll();
        }
        return categoryRepository.findAllById(ids);
    }

    public Optional<Tracker> findCurrentTracker() {
        return trackersRepository.findByStopTimestampIsNull();
    }

}
