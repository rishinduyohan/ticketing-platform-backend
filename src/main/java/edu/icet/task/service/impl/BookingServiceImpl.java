package edu.icet.task.service.impl;

import edu.icet.task.model.dto.BookingDTO;
import edu.icet.task.model.dto.SeatHoldResponseDTO;
import edu.icet.task.model.entity.*;
import edu.icet.task.repository.BookingRepository;
import edu.icet.task.repository.SeatRepository;
import edu.icet.task.repository.UserRepository;
import edu.icet.task.service.BookingService;
import edu.icet.task.service.annotation.AuditFailure;
import edu.icet.task.service.exceptions.SeatLockedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

        @Autowired
        private SeatRepository seatRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private BookingRepository bookingRepository;

        @Autowired
        private PriceCalculatorService priceCalculatorService;


    @Transactional
    @Override
    public SeatHoldResponseDTO holdSeat(Long seatId, Long userId) {
        Seat seat = seatRepository.findByIdForUpdate(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (SeatStatus.SOLD.equals(seat.getStatus())) {
            throw new RuntimeException("This seat has already been sold and cannot be held.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (SeatStatus.HELD.equals(seat.getStatus()) && seat.getHoldExpiry() != null) {
            long secondsRemaining = java.time.Duration.between(now, seat.getHoldExpiry()).getSeconds();
            if (secondsRemaining > 0) {
                throw new SeatLockedException("Seat is locked by another user.", secondsRemaining);
            }
        }

        User user = userRepository.findById(userId).orElseThrow();
        seat.setStatus(SeatStatus.HELD);
        seat.setHeldBy(user);
        seat.setHoldExpiry(now.plusMinutes(10));
        seatRepository.save(seat);

        return new SeatHoldResponseDTO(seat.getId(), "HELD", seat.getHoldExpiry(), "Seat successfully held.");
    }

        @Transactional
        @AuditFailure
        @Override
        public BookingDTO processBooking(Long userId, Long seatId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (!SeatStatus.HELD.equals(seat.getStatus()) || !seat.getHeldBy().getId().equals(userId)) {
                throw new RuntimeException("You do not have an active hold on this seat.");
            }

            if (seat.getHoldExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Your hold has expired. Please try again.");
            }

            BookingDTO priceDetails = priceCalculatorService.calculatePrice(user, seat.getEvent(),seat);

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setSeat(seat);
            booking.setAmountPaid(priceDetails.getAmountPaid());
            booking.setStatus(BookingStatus.valueOf("CONFIRMED"));
            bookingRepository.save(booking);

            seat.setStatus(SeatStatus.valueOf("SOLD"));
            seat.setHoldExpiry(null);
            seatRepository.save(seat);

            priceDetails.setBookingId(booking.getId());
            priceDetails.setStatus("CONFIRMED");
            return priceDetails;
        }

    @Transactional
    public void cancelHold(Long seatId, Long userId) {
        Seat seat = seatRepository.findByIdForUpdate(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (!SeatStatus.HELD.equals(seat.getStatus())) {
            throw new RuntimeException("Seat is not currently on hold.");
        }

        if (seat.getHeldBy() == null || !seat.getHeldBy().getId().equals(userId)) {
            throw new RuntimeException("You do not have permission to cancel this hold.");
        }

        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setHeldBy(null);
        seat.setHoldExpiry(null);

        seatRepository.save(seat);
    }
}
