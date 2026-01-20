package edu.icet.task.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "is_high_demand")
    private boolean isHighDemand;

    @Column(name = "event_date")
    private LocalDateTime eventDate;
}
