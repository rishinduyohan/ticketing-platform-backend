package edu.icet.task.controller;

import edu.icet.task.model.dto.ErrorDTO;
import edu.icet.task.model.dto.SeatHoldRequestDTO;
import edu.icet.task.model.dto.SeatHoldResponseDTO;
import edu.icet.task.service.BookingService;
import edu.icet.task.service.exceptions.SeatLockedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/{id}/hold")
    public ResponseEntity<SeatHoldResponseDTO> holdSeat(@PathVariable Long id, @RequestBody SeatHoldRequestDTO request) {
        SeatHoldResponseDTO response = bookingService.holdSeat(id, request.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelHold(@PathVariable Long id, @RequestBody SeatHoldRequestDTO request) {
        bookingService.cancelHold(id, request.getUserId());
        return ResponseEntity.ok("Seat hold cancelled successfully. Seat is now AVAILABLE.");
    }


    @ExceptionHandler(SeatLockedException.class)
    public ResponseEntity<ErrorDTO> handleSeatLocked(SeatLockedException ex) {
        ErrorDTO error = new ErrorDTO(ex.getMessage(), ex.getSecondsRemaining());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}