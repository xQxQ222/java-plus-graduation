package ru.yandex.practicum.model.similarity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "similarities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Similarity {
    @EmbeddedId
    SimilarityKey id;

    @Column(name = "rating", nullable = false)
    double rating;

    @Column(name = "timestamp", nullable = false)
    Instant timestamp;
}
