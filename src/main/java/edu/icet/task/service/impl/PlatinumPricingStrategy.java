package edu.icet.task.service.impl;

import edu.icet.task.service.PricingStrategy;

import java.math.BigDecimal;

public class PlatinumPricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, boolean isHighDemand) {
        return basePrice;
    }

    @Override
    public boolean hasPriorityAccess() {
        return true;
    }
}
