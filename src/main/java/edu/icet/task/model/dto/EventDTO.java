package edu.icet.task.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private Long id;
    private String name;
    private BigDecimal basePrice;
    private boolean isHighDemand;
    private LocalDateTime eventDate;
}
