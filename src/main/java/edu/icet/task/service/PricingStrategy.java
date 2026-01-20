package edu.icet.task.service;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(BigDecimal basePrice, boolean isHighDemand);
    boolean hasPriorityAccess();
}
