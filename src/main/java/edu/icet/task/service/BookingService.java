package edu.icet.task.service;

import edu.icet.task.model.dto.SeatHoldResponseDTO;

public interface BookingService {
    public SeatHoldResponseDTO holdSeat(Long seatId, Long userId);
}
