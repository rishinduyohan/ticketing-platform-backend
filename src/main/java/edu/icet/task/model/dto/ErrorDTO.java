package edu.icet.task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDTO {
    private String errorCode;
    private String message;
    private long secondsRemaining;

    public ErrorDTO(String message, long secondsRemaining) {
        this.errorCode = "SEAT_ALREADY_LOCKED";
        this.message = message;
        this.secondsRemaining = secondsRemaining;
    }
}
