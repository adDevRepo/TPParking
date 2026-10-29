package fr.parchorizon;

import fr.parchorizon.service.ParkingBilling;
import fr.parchorizon.service.strategy.DiscountDecorator;
import fr.parchorizon.service.strategy.PricingStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests de caractérisation des tarifs actuels.
 */
class ParkingBillingTest {

    private final ParkingBilling parkingBilling = new ParkingBilling();
    private final LocalDateTime entree = LocalDateTime.of(2026, 11, 4, 10, 0);


    // CAR
    @Test
    void carFourHours() {
        LocalDateTime entry = LocalDateTime.of(2026, 11, 4, 10, 0);
        assertEquals(8.00, new ParkingBilling().computePrice("CAR", entry, entry.plusHours(4), false), 0.001);
    }

    // Durée gratuite
    @Test
    void carZeroMinutesEstGratuit() {
        assertEquals(
                0.00,
                parkingBilling.computePrice("CAR", entree, entree, false),
                0.001);
    }

    @Test
    void car30MinutesEstGratuit() {
        assertEquals(
                0.0,
                parkingBilling.computePrice("CAR", entree, entree.plusMinutes(30), false),
                0.001
        );
    }

    @Test
    void car30MinutesEstPasGratuit() {
        assertEquals(
                2.0,
                parkingBilling.computePrice("CAR", entree, entree.plusMinutes(31), false),
                0.001
        );
    }

    // Heures entamées
    @Test
    void car1Heures1MinutesCoutent4Euros() {
        assertEquals(
                4.0,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(1).plusMinutes(1), false),
                0.001
        );
    }

    @Test
    void car4HeuresCoutent8Euros() {
        assertEquals(
                8.00,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(4), false),
                0.001
        );
    }

    // Plafond + calcul sur plusieurs jours
    @Test
    void car10HeuresDepassentForfaitJour() {
        assertEquals(
                20.0,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(10), false),
                0.001
        );
    }

    @Test
    void car24HeuresCoutent20Euros() {
        assertEquals(
                20.0,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(24), false),
                0.001
        );
    }

    @Test
    void car25HeuresCoutent22Euros() {
        assertEquals(
                22.0,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(25), false),
                0.001
        );
    }

    // Forfait nuit
    @Test
    void carParkingNuitCoute5Euros() {
        LocalDateTime entreeNuit = LocalDateTime.of(2026, 11, 4, 20, 0);
        LocalDateTime sortieNuit = LocalDateTime.of(2026, 11, 5, 7, 0);

        assertEquals(
                5.0,
                parkingBilling.computePrice("CAR", entreeNuit, sortieNuit, false),
                0.001
        );
    }

    @Test
    void carParkingNuitFinApres8h() {
        LocalDateTime entreeNuit = LocalDateTime.of(2026, 11, 4, 20, 0);
        LocalDateTime sortieNuit = LocalDateTime.of(2026, 11, 5, 8, 0);

        assertEquals(
                20.0,
                parkingBilling.computePrice("CAR", entreeNuit, sortieNuit, false),
                0.001
        );
    }

    @Test
    void carReductionAbonnement() {
        assertEquals(
                6.4,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(4), true),
                0.001
        );
    }

    @Test
    void carParkingNuitAbonnement() {
        LocalDateTime entreeNuit = LocalDateTime.of(2026, 11, 4, 20, 0);
        LocalDateTime sortieNuit = LocalDateTime.of(2026, 11, 5, 7, 0);

        assertEquals(
                4.0,
                parkingBilling.computePrice("CAR", entreeNuit, sortieNuit, true),
                0.001);
    }

    // TESTS MOTOS

    // Vérifie que 30 minutes de stationnement moto sont gratuites.
    @Test
    void motorBikeThirteenMinutesFree() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 9, 0);

        assertEquals(0.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(30), false), 0.001);
    }

    // Vérifie qu'à 31 minutes, une heure est facturée.
    @Test
    void motorbikeThirtyOneMinutes() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(1.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(31), false), 0.001);
    }

    // Vérifie le tarif actuel pour une durée de 1 h 1 min : deux heures sont facturées.
    @Test
    void motorbikeOneHourAndOneMinuteCostsTwoEuros() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(2.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusHours(1).plusMinutes(1), false), 0.001);
    }

    // Vérifie le plafond de 10 € à 24 h et le prix de 11 € à 25 h.
    @Test
    void motorbikeTwentyFourHoursVsTwentyFiveHours() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(10.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusHours(24), false), 0.001);
        assertEquals(11.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusHours(25), false), 0.001);
    }

    // Vérifie que le forfait nuit moto coûte 3 € pour une entrée à 20 h
    // et une sortie avant 8 h le lendemain.
    @Test
    void motorbikeNightPackageCostsThreeEuros() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 7, 0);

        assertEquals(3.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, exit, false), 0.001);
    }

    // Vérifie qu'à 8 h pile, le forfait nuit ne s'applique plus.
    @Test
    void motorbikeNightPackageDoesNotApplyAtEightAm() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 8, 0);

        assertEquals(10.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, exit, false), 0.001);
    }

    // Vérifie que la réduction abonné de 20 % fait passer le tarif de 4 € à 3,20 €.
    @Test
    void motorbikeSubscriberGetsTwentyPercentDiscount() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(3.20, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusHours(4), true), 0.001);
    }

    // Vérifie que la réduction abonné s'applique aussi au forfait nuit de 3 €.
    @Test
    void motorbikeSubscriberGetsDiscountOnNightPackage() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 7, 0);

        assertEquals(2.40, parkingBilling.computePrice(
                "MOTORBIKE", entry, exit, true), 0.001);
    }


    // Test du pattern decorator associé aux remises

    @Test
    void appliesTwentyPercentDiscountToWrappedStrategyPrice() {
        PricingStrategy baseStrategy = (entry, exit) -> 10.0;
        DiscountDecorator decorator = new DiscountDecorator(baseStrategy, 0.20);

        LocalDateTime entry = LocalDateTime.of(2026, 11, 4, 10, 0);
        LocalDateTime exit = entry.plusHours(1);

        assertEquals(8.0, decorator.computePrice(entry, exit), 0.001);
    }

    @Test
    void carSaturdayGetsFiftyPercentDiscount() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 10, 31, 10, 0);

        assertEquals(
                4.0,
                parkingBilling.computePrice(
                        "CAR", entry, entry.plusHours(4), false),
                0.001
        );
    }

    @Test
    void carSundayGetsFiftyPercentDiscount() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 11, 1, 10, 0);

        assertEquals(
                4.0,
                parkingBilling.computePrice(
                        "CAR", entry, entry.plusHours(4), false),
                0.001
        );
    }

    @Test
    void carWeekdayDoesNotGetWeekendDiscount() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 11, 2, 10, 0);

        assertEquals(
                8.0,
                parkingBilling.computePrice(
                        "CAR", entry, entry.plusHours(4), false),
                0.001
        );
    }

    @Test
    void weekendDiscountIsAppliedBeforeSubscriberDiscount() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 10, 31, 10, 0);

        assertEquals(
                3.2,
                parkingBilling.computePrice(
                        "CAR", entry, entry.plusHours(4), true),
                0.001
        );
    }

    @Test
    void electricCarChargesFourHoursPlusTwelveKWh() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 10, 31, 10, 0);

        assertEquals(
                8.2,
                parkingBilling.computePrice(
                        "ELECTRIC_CAR", entry,
                        entry.plusHours(4), false, 12.0),
                0.001
        );
    }

    @Test
    void electricCarSubscriberWeekendExample() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 10, 31, 10, 0);

        assertEquals(
                7.4,
                parkingBilling.computePrice(
                        "ELECTRIC_CAR", entry,
                        entry.plusHours(4), true, 12.0),
                0.001
        );
    }

    @Test
    void electricChargingIsNotDiscountedForSubscriber() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 10, 31, 10, 0);

        double price = parkingBilling.computePrice(
                "ELECTRIC_CAR", entry,
                entry.plusHours(4), true, 12.0);

        // Stationnement : 8 € -> 4 € -> 3,20 €
        // Recharge : 12 × 0,35 € = 4,20 €
        assertEquals(3.2 + 4.2, price, 0.001);
    }

    @Test
    void electricCarOldMethodStillWorks() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 11, 4, 10, 0);

        // L'ancienne méthode ne reçoit pas de kWh.
        assertEquals(
                8.0,
                parkingBilling.computePrice(
                        "ELECTRIC_CAR", entry,
                        entry.plusHours(4), false),
                0.001
        );
    }

    @Test
    void negativeChargedKWhThrowsException() {
        LocalDateTime entry =
                LocalDateTime.of(2026, 11, 4, 10, 0);

        assertThrows(
                IllegalArgumentException.class,
                () -> parkingBilling.computePrice(
                        "ELECTRIC_CAR", entry,
                        entry.plusHours(4), false, -1.0)
        );
    }

}