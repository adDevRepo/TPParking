package fr.parchorizon.config;

/** Informations fixes du parking, affichées sur les tickets. */
public final class ParkingConfig {

    private static final ParkingConfig INSTANCE = new ParkingConfig();

    private final String parkingName = "Parc Horizon - Centre";
    private final String currency = "EUR";

    private ParkingConfig() {
    }

    public static ParkingConfig getInstance() {
        return INSTANCE;
    }

    public String getParkingName() { return parkingName; }
    public String getCurrency() { return currency; }
}
