package fr.parchorizon;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
}
