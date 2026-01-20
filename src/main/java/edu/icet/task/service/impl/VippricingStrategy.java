package edu.icet.task.service.impl;

import edu.icet.task.service.PricingStrategy;

import java.math.BigDecimal;

public class VippricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, boolean isHighDemand) {
        if (isHighDemand) {
            return basePrice;
        }
        return basePrice.multiply(new BigDecimal("0.90")); // 10% off
    }

    @Override
    public boolean hasPriorityAccess() {
        return false;
    }
}
