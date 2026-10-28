package fr.parchorizon;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests de caractérisation des tarifs actuels.
 */
class ParkingBillingTest {

    private final ParkingBilling parkingBilling = new ParkingBilling();
    private final LocalDateTime entree = LocalDateTime.of(2026, 11, 4, 10, 0);


    // TESTS MOTOS

    @Test
    void motorbikeThirtyMinutesAreFree() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 9, 0);

        assertEquals(0.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(30), false), 0.001);
    }

    @Test
    void motorbikeThirtyMinutesAndOneSecondCostOneEuro() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 9, 0);

        assertEquals(1.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(30).plusSeconds(1), false), 0.001);
    }

    @Test
    void motorbikeNinetyMinutesCostOneEuro() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(1.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(90), false), 0.001);
    }

    @Test
    void motorbikeNinetyOneMinutesCostTwoEuros() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(2.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(91), false), 0.001);
    }

    @Test
    void motorbikeTwentyFourHoursCostTenEurosAndTwentyFiveHoursCostEleven() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(10.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusHours(24), false), 0.001);
        assertEquals(11.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusHours(25), false), 0.001);
    }

    // CAR

    @Test
    void carFourHours() {
        LocalDateTime entry = LocalDateTime.of(2026, 11, 4, 10, 0);

        assertEquals(8.00, parkingBilling.computePrice(
                "CAR", entry, entry.plusHours(4), false), 0.001);
    }

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
                0.001);
    }

    @Test
    void car30MinutesEstPasGratuit() {
        assertEquals(
                2.0,
                parkingBilling.computePrice("CAR", entree, entree.plusMinutes(31), false),
                0.001);
    }

    @Test
    void car1Heures1MinutesCoutent4Euros() {
        assertEquals(
                4.0,
                parkingBilling.computePrice(
                        "CAR", entree, entree.plusHours(1).plusMinutes(1), false),
                0.001);
    }

    @Test
    void car4HeuresCoutent8Euros() {
        assertEquals(
                8.00,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(4), false),
                0.001);
    }

    @Test
    void car10HeuresDepassentForfaitJour() {
        assertEquals(
                20.0,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(10), false),
                0.001);
    }

    @Test
    void car24HeuresCoutent20Euros() {
        assertEquals(
                20.0,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(24), false),
                0.001);
    }

    @Test
    void car25HeuresCoutent22Euros() {
        assertEquals(
                22.0,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(25), false),
                0.001);
    }

    @Test
    void carParkingNuitCoute5Euros() {
        LocalDateTime entreeNuit = LocalDateTime.of(2026, 11, 4, 20, 0);
        LocalDateTime sortieNuit = LocalDateTime.of(2026, 11, 5, 7, 0);

        assertEquals(
                5.0,
                parkingBilling.computePrice("CAR", entreeNuit, sortieNuit, false),
                0.001);
    }

    @Test
    void carParkingNuitFinApres8h() {
        LocalDateTime entreeNuit = LocalDateTime.of(2026, 11, 4, 20, 0);
        LocalDateTime sortieNuit = LocalDateTime.of(2026, 11, 5, 8, 0);

        assertEquals(
                20.0,
                parkingBilling.computePrice("CAR", entreeNuit, sortieNuit, false),
                0.001);
    }

    @Test
    void carReductionAbonnement() {
        assertEquals(
                6.4,
                parkingBilling.computePrice("CAR", entree, entree.plusHours(4), true),
                0.001);
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

    // AUTRES TESTS MOTOS

    @Test
    void motorbikeNightPackageDoesNotApplyAtEightAm() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 8, 0);

        assertEquals(10.00, parkingBilling.computePrice(
                "MOTORBIKE", entry, exit, false), 0.001);
    }

    @Test
    void motorbikeSubscriberGetsTwentyPercentDiscountOnWeekday() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(3.20, parkingBilling.computePrice(
                "MOTORBIKE", entry, entry.plusHours(4), true), 0.001);
    }

    @Test
    void motorbikeSubscriberGetsDiscountOnNightPackage() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 7, 0);

        assertEquals(2.40, parkingBilling.computePrice(
                "MOTORBIKE", entry, exit, true), 0.001);
    }
}