package ru.k4nk.chronofocus.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "time_segments")
public class TimeSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd_HH:mm:ss")
    private LocalDateTime startTimestamp;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd_HH:mm:ss")
    private LocalDateTime endTimestamp;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public TimeSegment(Category category, LocalDateTime startTimestamp) {
        this.category = category;
        this.startTimestamp = startTimestamp;
    }
}