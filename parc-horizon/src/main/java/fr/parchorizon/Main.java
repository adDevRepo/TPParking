package fr.parchorizon;

import java.time.LocalDateTime;

/** Simule quelques passages à la borne de sortie. */
public class Main {

    public static void main(String[] args) {
        ParkingBilling billing = new ParkingBilling();
        LocalDateTime wednesday10h = LocalDateTime.of(2026, 11, 4, 10, 0);

        System.out.println(billing.printReceipt("CAR", wednesday10h, wednesday10h.plusHours(4), false));
        System.out.println();
        System.out.println(billing.printReceipt("MOTORBIKE", wednesday10h, wednesday10h.plusMinutes(20), false));
        System.out.println();
        System.out.println(billing.printReceipt("VAN", wednesday10h, wednesday10h.plusHours(30), true));
    }
}
