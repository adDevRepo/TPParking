package fr.parchorizon.service.strategy;

import java.time.Duration;
import java.time.LocalDateTime;

public class VanPricingStrategy implements PricingStrategy {

    @Override
    public double computePrice(LocalDateTime entry, LocalDateTime exit) {
        if (exit.isBefore(entry)) {
            throw new IllegalArgumentException("Sortie avant l'entrée");
        }

        long minutes = Duration.between(entry, exit).toMinutes();
        long hours = (minutes + 59) / 60;

        if (hours == 0) {
            hours = 1;
        }

        long days = hours / 24;
        long rest = hours % 24;

        return days * 30.0 + Math.min(rest * 3.0, 30.0);
    }
}
