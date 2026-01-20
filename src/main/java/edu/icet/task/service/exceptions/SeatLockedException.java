package edu.icet.task.service.exceptions;

public class SeatLockedException extends RuntimeException {
    private Long secondsRemaining;

    public SeatLockedException(String message, Long secondsRemaining) {
        super(message);
        this.secondsRemaining = secondsRemaining;
    }

    public long getSecondsRemaining() {
        return secondsRemaining;
    }
}
