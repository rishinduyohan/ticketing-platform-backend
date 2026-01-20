package edu.icet.task.service.impl;

import edu.icet.task.model.dto.BookingDTO;
import edu.icet.task.model.dto.SeatHoldResponseDTO;
import edu.icet.task.model.entity.Booking;
import edu.icet.task.model.entity.Seat;
import edu.icet.task.model.entity.SeatStatus;
import edu.icet.task.model.entity.User;
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

            LocalDateTime now = LocalDateTime.now();

            boolean isAvailable = "AVAILABLE".equals(seat.getStatus());
            boolean isExpired = seat.getHoldExpiry() != null && seat.getHoldExpiry().isBefore(now);

            if (isAvailable || isExpired) {
                seat.setStatus("HELD");
                seat.setHoldExpiry(now.plusMinutes(10));
                seat.setHeldBy(userRepository.getReferenceById(userId));
                seatRepository.save(seat);

                return new SeatHoldResponseDTO(seat.getId(), "HELD", seat.getHoldExpiry(), "Seat successfully held for 10 minutes.");
            }

            long secondsRemaining = java.time.Duration.between(now, seat.getHoldExpiry()).getSeconds();
            throw new SeatLockedException("Seat is currently held by another user. wait "+secondsRemaining);
        }

        @Transactional
        @AuditFailure
        public BookingDTO processBooking(Long userId, Long seatId) {
            // 1. Fetch user and seat (Seat must be HELD by this user)
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (!"HELD".equals(seat.getStatus()) || !seat.getHeldBy().getId().equals(userId)) {
                throw new RuntimeException("You do not have an active hold on this seat.");
            }

            if (seat.getHoldExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Your hold has expired. Please try again.");
            }

            // 2. Calculate Price using Strategy Pattern [cite: 28, 37]
            BookingDTO priceDetails = priceCalculatorService.calculatePrice(user, seat.getEvent());

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setSeat(seat);
            booking.setAmountPaid(priceDetails.getAmountPaid());
            booking.setStatus("CONFIRMED");
            bookingRepository.save(booking);

            seat.setStatus("SOLD");
            seat.setHoldExpiry(null);
            seatRepository.save(seat);

            priceDetails.setBookingId(booking.getId());
            priceDetails.setStatus("CONFIRMED");
            return priceDetails;
        }
}
