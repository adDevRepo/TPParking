
package fr.parchorizon.service;

import fr.parchorizon.config.ParkingConfig;
import fr.parchorizon.service.strategy.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ParkingBilling {

    private static final double PRICE_PER_KWH = 0.35;

    // Ancienne méthode conservée pour les bornes existantes.
    public double computePrice(
            String vehicleType,
            LocalDateTime entry,
            LocalDateTime exit,
            boolean subscriber) {

        return computePrice(
                vehicleType, entry, exit, subscriber, 0.0);
    }

    // Nouvelle méthode avec la recharge électrique.
    public double computePrice(
            String vehicleType,
            LocalDateTime entry,
            LocalDateTime exit,
            boolean subscriber,
            double chargedKWh) {

        validateChargedKWh(chargedKWh);

        PricingStrategy strategy =
                PricingStrategyFactory.forVehicleType(vehicleType);

        // La réduction week-end porte uniquement sur le stationnement.
        strategy = new WeekEndDiscountDecorator(strategy);

        // L'abonnement s'applique après la réduction week-end.
        if (subscriber) {
            strategy = new DiscountDecorator(strategy, 0.20);
        }

        double parkingPrice = strategy.computePrice(entry, exit);
        double chargingPrice = computeChargingPrice(vehicleType, chargedKWh);

        // On ajoute la recharge après les réductions, qui ne concernent que le stationnement.
        return parkingPrice + chargingPrice;
    }

    private double computeChargingPrice(String vehicleType, double chargedKWh) {
        if (!"ELECTRIC_CAR".equals(vehicleType)) {
            return 0.0;
        }

        return chargedKWh * PRICE_PER_KWH;
    }

    private void validateChargedKWh(double chargedKWh) {
        if (chargedKWh < 0.0 || !Double.isFinite(chargedKWh)) {
            throw new IllegalArgumentException(
                    "La quantité de kWh doit être positive ou nulle");
        }
    }

    public String printReceipt(
            String vehicleType,
            LocalDateTime entry,
            LocalDateTime exit,
            boolean subscriber) {

        ParkingConfig config = ParkingConfig.getInstance();

        DateTimeFormatter f =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        double price =
                computePrice(vehicleType, entry, exit, subscriber);

        return config.getParkingName() + "\n"
                + "Entrée : " + entry.format(f) + "\n"
                + "Sortie : " + exit.format(f) + "\n"
                + (subscriber ? "Abonné\n" : "")
                + "Montant : "
                + String.format(Locale.FRANCE, "%.2f", price)
                + " " + config.getCurrency();
    }
}
