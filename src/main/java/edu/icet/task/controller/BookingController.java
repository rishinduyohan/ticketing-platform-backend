package edu.icet.task.controller;

import edu.icet.task.model.dto.BookingDTO;
import edu.icet.task.service.BookingService;
import edu.icet.task.service.annotation.AuditFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping
    @AuditFailure
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO request) {
        BookingDTO response = bookingService.processBooking(request.getUserId(), request.getSeatId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
