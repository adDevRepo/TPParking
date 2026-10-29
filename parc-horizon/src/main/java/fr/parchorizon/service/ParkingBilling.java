
package fr.parchorizon.service;

import fr.parchorizon.config.ParkingConfig;
import fr.parchorizon.service.strategy.CarPricingStrategy;
import fr.parchorizon.service.strategy.MotorBikePricingStrategy;
import fr.parchorizon.service.strategy.PricingStrategy;
import fr.parchorizon.service.strategy.VanPricingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ParkingBilling {

    public double computePrice(
            String vehicleType,
            LocalDateTime entry,
            LocalDateTime exit,
            boolean subscriber) {

        PricingStrategy strategy;

        switch (vehicleType) {
            case "CAR":
                strategy = new CarPricingStrategy();
                break;

            case "MOTORBIKE":
                strategy = new MotorBikePricingStrategy();
                break;

            case "VAN":
                strategy = new VanPricingStrategy();
                break;

            default:
                throw new IllegalArgumentException(
                        "Type de véhicule inconnu : " + vehicleType
                );
        }

        double price = strategy.computePrice(entry, exit);

        if (subscriber) {
            price *= 0.8;
        }

        return price;
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
