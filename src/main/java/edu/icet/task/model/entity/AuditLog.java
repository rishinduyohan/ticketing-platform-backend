package edu.icet.task.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action")
    private String action;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "details")
    private String details;

    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

}