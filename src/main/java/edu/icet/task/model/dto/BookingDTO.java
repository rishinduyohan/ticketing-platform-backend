package edu.icet.task.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long bookingId;
    private Long userId;
    private Long seatId;
    private BigDecimal amountPaid;
    private String userTier;
    private boolean priorityAccess;
    private String status;
}
