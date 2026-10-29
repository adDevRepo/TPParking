package fr.parchorizon.service.strategy;

import java.time.Duration;
import java.time.LocalDateTime;

public class MotorBikePricingStrategy implements PricingStrategy {

    @Override
    public double computePrice(LocalDateTime entry, LocalDateTime exit) {
        if (exit.isBefore(entry)) {
            throw new IllegalArgumentException("Sortie avant l'entrée");
        }

        long minutes = Duration.between(entry, exit).toMinutes();

        boolean night = entry.getHour() >= 20
                && exit.toLocalDate().equals(entry.toLocalDate().plusDays(1))
                && exit.getHour() < 8;

        if (minutes <= 30) {
            return 0.0;
        }

        if (night) {
            return 3.0;
        }

        long hours = (minutes + 59) / 60;
        long days = hours / 24;
        long rest = hours % 24;

        return days * 10.0 + Math.min(rest * 1.0, 10.0);
    }
}
