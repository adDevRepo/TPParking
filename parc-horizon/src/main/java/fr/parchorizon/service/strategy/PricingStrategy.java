
package fr.parchorizon.service.strategy;

import java.time.LocalDateTime;

public interface PricingStrategy {

    double computePrice(LocalDateTime entry, LocalDateTime exit);
}
