
package fr.parchorizon.service.strategy;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;

public final class WeekEndDiscountDecorator implements PricingStrategy {

    private final PricingStrategy wrappedStrategy;

    public WeekEndDiscountDecorator(PricingStrategy wrappedStrategy) {
        this.wrappedStrategy = Objects.requireNonNull(wrappedStrategy);
    }

    @Override
    public double computePrice(LocalDateTime entry, LocalDateTime exit) {
        double price = wrappedStrategy.computePrice(entry, exit);

        DayOfWeek day = entry.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return price * 0.5;
        }

        return price;
    }
}
