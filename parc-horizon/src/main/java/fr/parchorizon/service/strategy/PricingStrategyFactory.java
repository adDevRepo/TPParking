package fr.parchorizon.service.strategy;

/** Creates the pricing strategy associated with a vehicle type. */
public final class PricingStrategyFactory {

    private PricingStrategyFactory() {
    }

    public static PricingStrategy forVehicleType(String vehicleType) {
        switch (vehicleType) {
            case "CAR":
                return new CarPricingStrategy();
            case "MOTORBIKE":
                return new MotorBikePricingStrategy();
            case "VAN":
                return new VanPricingStrategy();
            default:
                throw new IllegalArgumentException(
                        "Type de véhicule inconnu : " + vehicleType);
        }
    }
}
