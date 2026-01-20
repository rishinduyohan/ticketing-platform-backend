package edu.icet.task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SeatHoldResponseDTO {
    private Long seatId;
    private String status;
    private LocalDateTime holdExpiry;
    private String message;
}
