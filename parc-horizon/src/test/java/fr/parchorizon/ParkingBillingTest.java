package fr.parchorizon;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingBillingTest {

    private final ParkingBilling billing = new ParkingBilling();

    // Vérifie le prix d'une voiture garée pendant quatre heures.
    @Test
    void carFourHours() {
        LocalDateTime entry = LocalDateTime.of(2026, 11, 4, 10, 0);

        assertEquals(8.00, billing.computePrice(
                "CAR", entry, entry.plusHours(4), false), 0.001);
    }



//    TESTS MOTOS

    // Les trente premières minutes en moto sont gratuites.
    @Test
    void motorbikeThirtyMinutesAreFree() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 9, 0);

        assertEquals(0.00, billing.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(30), false), 0.001);
    }

    // Une seconde après les trente minutes gratuites, une heure est facturée.
    @Test
    void motorbikeThirtyMinutesAndOneSecondCostOneEuro() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 9, 0);

        assertEquals(1.00, billing.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(30).plusSeconds(1), false), 0.001);
    }

    // Après trente minutes gratuites, une heure entamée coûte un euro.
    @Test
    void motorbikeNinetyMinutesCostOneEuro() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(1.00, billing.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(90), false), 0.001);
    }

    // Une minute de plus entame une deuxième heure payante.
    @Test
    void motorbikeNinetyOneMinutesCostTwoEuros() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(2.00, billing.computePrice(
                "MOTORBIKE", entry, entry.plusMinutes(91), false), 0.001);
    }

    // Le prix est plafonné à dix euros par tranche de vingt-quatre heures.
    @Test
    void motorbikeTwentyFourHoursCostTenEurosAndTwentyFiveHoursCostEleven() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(10.00, billing.computePrice(
                "MOTORBIKE", entry, entry.plusHours(24), false), 0.001);
        assertEquals(11.00, billing.computePrice(
                "MOTORBIKE", entry, entry.plusHours(25), false), 0.001);
    }

    // Le forfait nuit coûte trois euros jusqu'à 7 h 59.
    @Test
    void motorbikeNightPackageAppliesFromEightPmUntilBeforeEightAm() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 7, 59);

        assertEquals(3.00, billing.computePrice(
                "MOTORBIKE", entry, exit, false), 0.001);
    }

    // À 8 h pile, le forfait nuit ne s'applique plus.
    @Test
    void motorbikeNightPackageDoesNotApplyAtEightAm() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 8, 0);

        assertEquals(10.00, billing.computePrice(
                "MOTORBIKE", entry, exit, false), 0.001);
    }

    // L'abonné bénéficie de vingt pour cent de réduction sur le tarif moto.
    @Test
    void motorbikeSubscriberGetsTwentyPercentDiscountOnWeekday() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        assertEquals(3.20, billing.computePrice(
                "MOTORBIKE", entry, entry.plusHours(4), true), 0.001);
    }

    // La réduction abonné s'applique aussi au forfait nuit.
    @Test
    void motorbikeSubscriberGetsDiscountOnNightPackage() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 7, 0);

        assertEquals(2.40, billing.computePrice(
                "MOTORBIKE", entry, exit, true), 0.001);
    }
}