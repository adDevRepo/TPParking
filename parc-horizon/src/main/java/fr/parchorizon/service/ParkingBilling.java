
package fr.parchorizon.service;

import fr.parchorizon.config.ParkingConfig;
import fr.parchorizon.service.strategy.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ParkingBilling {

    public double computePrice(
            String vehicleType,
            LocalDateTime entry,
            LocalDateTime exit,
            boolean subscriber) {

        PricingStrategy strategy = PricingStrategyFactory.forVehicleType(vehicleType);

        if (subscriber) {
            strategy = new DiscountDecorator(strategy, 0.20);
        }

        return strategy.computePrice(entry, exit);
    }

    public String printReceipt(
            String vehicleType,
            LocalDateTime entry,
            LocalDateTime exit,
            boolean subscriber) {

        ParkingConfig config = ParkingConfig.getInstance();

        DateTimeFormatter f =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        double price = computePrice(
                vehicleType, entry, exit, subscriber);

        return config.getParkingName() + "\n"
                + "Entrée : " + entry.format(f) + "\n"
                + "Sortie : " + exit.format(f) + "\n"
                + (subscriber ? "Abonné\n" : "")
                + "Montant : "
                + String.format(Locale.FRANCE, "%.2f", price)
                + " " + config.getCurrency();
    }
}
