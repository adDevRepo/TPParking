package fr.parchorizon;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Seul test présent dans le projet. Il ne couvre presque rien :
 * c'est à vous de protéger le comportement actuel à l'étape 1.
 */
class ParkingBillingTest {

    @Test
    void carFourHours() {
        LocalDateTime entry = LocalDateTime.of(2026, 11, 4, 10, 0);
        assertEquals(8.00, new ParkingBilling().computePrice("CAR", entry, entry.plusHours(4), false), 0.001);
    }



    // test motos


    @Test
    void motorBikeThirteenMinutesFree() {
        LocalDateTime entry  = LocalDateTime.of(2026, 10, 28, 9, 0);
        LocalDateTime exit =  entry.plusMinutes(30);

        assertEquals(0.00, new ParkingBilling().computePrice("MOTORBIKE", entry, exit, false), 0.001);
    }
     // 31 minutes payantes
    @Test
    void motorbikeThirtyOneMinutes() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);
        LocalDateTime exit = entry.plusMinutes(31);

        assertEquals(1.00, new ParkingBilling().computePrice("MOTORBIKE", entry, exit, false), 0.001);
    }

    // 1 heure et 1 minute
    @Test
    void motorbikeOneHourAndOneMinuteCostsTwoEuros() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);
        LocalDateTime exit = entry.plusMinutes(61);

        assertEquals(
                2.00,
                new ParkingBilling().computePrice("MOTORBIKE", entry, exit, false),
                0.001
        );
    }


    @Test
    void motorbikeTwentyFourHoursVsTwentyFiveHours() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 28, 10, 0);

        LocalDateTime exit24h = entry.plusHours(24);
        LocalDateTime exit25h = entry.plusHours(25);

        assertEquals(
                10.00,
                new ParkingBilling().computePrice("MOTORBIKE", entry, exit24h, false),
                0.001
        );

        assertEquals(
                11.00,
                new ParkingBilling().computePrice("MOTORBIKE", entry, exit25h, false),
                0.001
        );
    }



    @Test
    void motorbikeNightPackageCostsThreeEuros() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 27, 20, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 28, 7, 0);

        assertEquals(
                3.00,
                new ParkingBilling().computePrice("MOTORBIKE", entry, exit, false),
                0.001
        );
    }


    @Test
    void motorbikeSubscriberGetsTwentyPercentDiscount() {
        LocalDateTime entry = LocalDateTime.of(2026, 10, 4, 10, 0);
        LocalDateTime exit = entry.plusHours(4);

        assertEquals(
                3.20,
                new ParkingBilling().computePrice("MOTORBIKE", entry, exit, true),
                0.001
        );
    }


}
