package edu.icet.task.service.impl;

import edu.icet.task.model.dto.BookingDTO;
import edu.icet.task.model.entity.Event;
import edu.icet.task.model.entity.User;
import edu.icet.task.service.PricingStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceCalculatorService {

    public BookingDTO calculatePrice(User user, Event event) {
        PricingStrategy strategy = switch (user.getTier()) {
            case VIP -> new VippricingStrategy();
            case PLATINUM -> new PlatinumPricingStrategy();
            default -> new RegularPricingStrategy();
        };

        BigDecimal finalPrice = strategy.calculatePrice(event.getBasePrice(), event.isHighDemand());
        boolean priority = strategy.hasPriorityAccess();

        BookingDTO dto = new BookingDTO();
        dto.setAmountPaid(finalPrice);
        dto.setPriorityAccess(priority);
        dto.setUserTier(user.getTier().name());

        return dto;
    }
}
