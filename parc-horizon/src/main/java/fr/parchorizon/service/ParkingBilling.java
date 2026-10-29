
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

        if (chargedKWh < 0 || !Double.isFinite(chargedKWh)) {
            throw new IllegalArgumentException(
                    "La quantité de kWh doit être positive ou nulle");
        }

        PricingStrategy strategy =
                PricingStrategyFactory.forVehicleType(vehicleType);

        // La réduction week-end porte uniquement sur le stationnement.
        strategy = new WeekEndDiscountDecorator(strategy);

        // L'abonnement s'applique après la réduction week-end.
        if (subscriber) {
            strategy = new DiscountDecorator(strategy, 0.20);
        }

        double parkingPrice = strategy.computePrice(entry, exit);

        // La recharge n'est ni réduite ni plafonnée.
        double chargingPrice = 0.0;

        if ("ELECTRIC_CAR".equals(vehicleType)) {
            chargingPrice = chargedKWh * PRICE_PER_KWH;
        }

        return parkingPrice + chargingPrice;
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
