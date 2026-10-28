package fr.parchorizon;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Calcul du prix à la sortie du parking. Appelé par les bornes de sortie. */
public class ParkingBilling {

    public double computePrice(String vehicleType, LocalDateTime entry, LocalDateTime exit, boolean subscriber) {
        if (exit.isBefore(entry)) {
            throw new IllegalArgumentException("Sortie avant l'entrée");
        }
        long minutes = Duration.between(entry, exit).toMinutes();
        boolean night = entry.getHour() >= 20
                && exit.toLocalDate().equals(entry.toLocalDate().plusDays(1))
                && exit.getHour() < 8;
        double price;

        switch (vehicleType) {
            case "CAR": {
                if (minutes <= 30) {
                    price = 0;
                } else if (night) {
                    price = 5.0;
                } else {
                    long hours = (minutes + 59) / 60;
                    long days = hours / 24;
                    long rest = hours % 24;
                    price = days * 20.0 + Math.min(rest * 2.0, 20.0);
                }
                break;
            }
            case "MOTORBIKE": {
                if (minutes <= 30) {
                    price = 0;
                } else if (night) {
                    price = 3.0;
                } else {
                    long hours = (minutes + 59) / 60;
                    long days = hours / 24;
                    long rest = hours % 24;
                    price = days * 10.0 + Math.min(rest * 1.0, 10.0);
                }
                break;
            }
            case "VAN": {
                long hours = (minutes + 59) / 60;
                if (hours == 0) {
                    hours = 1; // une heure minimum
                }
                long days = hours / 24;
                long rest = hours % 24;
                price = days * 30.0 + Math.min(rest * 3.0, 30.0);
                break;
            }
            default:
                throw new IllegalArgumentException("Type de véhicule inconnu : " + vehicleType);
        }

        if (subscriber) {
            price = price * 0.8;
        }
        return price;
    }

    public String printReceipt(String vehicleType, LocalDateTime entry, LocalDateTime exit, boolean subscriber) {
        ParkingConfig config = ParkingConfig.getInstance();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        double price = computePrice(vehicleType, entry, exit, subscriber);
        return config.getParkingName() + "\n"
                + "Entrée : " + entry.format(f) + "\n"
                + "Sortie : " + exit.format(f) + "\n"
                + (subscriber ? "Abonné\n" : "")
                + "Montant : " + String.format(Locale.FRANCE, "%.2f", price) + " " + config.getCurrency();
    }
}
