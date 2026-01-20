package edu.icet.task.service.exceptions;

public class SeatLockedException extends RuntimeException{
        public SeatLockedException(String message) {
            super(message);
        }
}
