package edu.icet.task.service;

import edu.icet.task.model.dto.BookingDTO;
import edu.icet.task.model.dto.SeatHoldResponseDTO;

public interface BookingService {
    SeatHoldResponseDTO holdSeat(Long seatId, Long userId);
    BookingDTO processBooking(Long userId, Long seatId);
    void cancelHold(Long seatId, Long userId);
}
