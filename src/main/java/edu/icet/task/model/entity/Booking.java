package edu.icet.task.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}

