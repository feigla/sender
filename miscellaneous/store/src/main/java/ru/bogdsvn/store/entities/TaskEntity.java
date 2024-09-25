package ru.bogdsvn.store.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Builder.Default
    @Column(updatable = false, nullable = false)
    private Instant createdAt = Instant.now();

    private Instant processedAt;

    private Instant latestTryAt;

    private String destinationEmail;

    private String message;
}
