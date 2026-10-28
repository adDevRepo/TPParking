package fr.parchorizon;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests d'acceptation de la demande : véhicules électriques et tarif week-end.
 * À copier dans src/test/java/fr/parchorizon/ à l'étape 4 seulement.
 *
 * Ils appellent une nouvelle méthode, à ajouter à ParkingBilling :
 *   double computePrice(String vehicleType, LocalDateTime entry, LocalDateTime exit,
 *                       boolean subscriber, double chargedKWh)
 * L'ancienne méthode à 4 paramètres doit continuer à fonctionner.
 */
class EvolutionAcceptanceTest {

    private static final double CENT = 0.001;

    private final ParkingBilling billing = new ParkingBilling();

    private final LocalDateTime wednesday10h = LocalDateTime.of(2026, 11, 4, 10, 0);
    private final LocalDateTime saturday10h = LocalDateTime.of(2026, 11, 7, 10, 0);

    // --- Véhicules électriques ---

    @Test
    void electricCarPaysCarRatePlusCharging() {
        assertEquals(8.00 + 4.20, billing.computePrice("ELECTRIC_CAR", wednesday10h, wednesday10h.plusHours(4), false, 12), CENT);
    }

    @Test
    void chargingIsPaidEvenDuringTheFreeHalfHour() {
        assertEquals(1.75, billing.computePrice("ELECTRIC_CAR", wednesday10h, wednesday10h.plusMinutes(20), false, 5), CENT);
    }

    @Test
    void chargingIsNotCapped() {
        assertEquals(20.00 + 21.00, billing.computePrice("ELECTRIC_CAR", wednesday10h, wednesday10h.plusHours(24), false, 60), CENT);
    }

    @Test
    void subscriberDiscountDoesNotApplyToCharging() {
        assertEquals(6.40 + 4.20, billing.computePrice("ELECTRIC_CAR", wednesday10h, wednesday10h.plusHours(4), true, 12), CENT);
    }

    // --- Tarif week-end ---

    @Test
    void weekendHalvesParking() {
        assertEquals(4.00, billing.computePrice("CAR", saturday10h, saturday10h.plusHours(4), false, 0), CENT);
    }

    @Test
    void weekendAlsoAppliesToTheOldMethod() {
        assertEquals(4.00, billing.computePrice("CAR", saturday10h, saturday10h.plusHours(4), false), CENT);
    }

    @Test
    void weekendAndSubscriberCumulate() {
        assertEquals(4.00, billing.computePrice("CAR", saturday10h, saturday10h.plusHours(5), true, 0), CENT);
    }

    @Test
    void weekendAppliesToNightFlatRate() {
        LocalDateTime sunday22h = LocalDateTime.of(2026, 11, 8, 22, 0);
        assertEquals(2.50, billing.computePrice("CAR", sunday22h, sunday22h.plusHours(9), false, 0), CENT);
    }

    @Test
    void weekendDependsOnEntryDay() {
        LocalDateTime friday23h = LocalDateTime.of(2026, 11, 6, 23, 0);
        assertEquals(5.00, billing.computePrice("CAR", friday23h, friday23h.plusHours(8), false, 0), CENT);
    }

    @Test
    void weekendAppliesToVansAndMotorbikes() {
        assertEquals(6.00, billing.computePrice("VAN", saturday10h, saturday10h.plusHours(4), false, 0), CENT);
        assertEquals(2.00, billing.computePrice("MOTORBIKE", saturday10h, saturday10h.plusHours(4), false, 0), CENT);
    }

    // --- L'exemple de l'énoncé ---

    @Test
    void exampleFromTheRequest() {
        assertEquals(7.40, billing.computePrice("ELECTRIC_CAR", saturday10h, saturday10h.plusHours(4), true, 12), CENT);
    }

    // --- Rien ne change en semaine ---

    @Test
    void weekdayPricesAreUnchanged() {
        assertEquals(8.00, billing.computePrice("CAR", wednesday10h, wednesday10h.plusHours(4), false, 0), CENT);
        assertEquals(6.40, billing.computePrice("CAR", wednesday10h, wednesday10h.plusHours(4), true, 0), CENT);
    }
}
