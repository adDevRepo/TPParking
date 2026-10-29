package fr.parchorizon.service.strategy;

import java.time.LocalDateTime;
import java.util.Objects;

public final class DiscountDecorator implements PricingStrategy {

    private final PricingStrategy wrappedStrategy;
    private final double discountRate;

    public DiscountDecorator(PricingStrategy wrappedStrategy, double discountRate) {
        this.wrappedStrategy = Objects.requireNonNull(wrappedStrategy);

        if (discountRate < 0.0 || discountRate > 1.0) {
            throw new IllegalArgumentException(
                    "Le taux de remise doit être compris entre 0 et 1");
        }

        this.discountRate = discountRate;
    }

    @Override
    public double computePrice(LocalDateTime entry, LocalDateTime exit) {
        double originalPrice = wrappedStrategy.computePrice(entry, exit);
        return originalPrice * (1.0 - discountRate);
    }
}