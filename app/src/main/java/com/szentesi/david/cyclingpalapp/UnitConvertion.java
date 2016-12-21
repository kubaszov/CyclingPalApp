package com.szentesi.david.cyclingpalapp;

/**
 * Created by David Szentesi on 15/12/2016.
 */

public class UnitConvertion {

    public static final double poundsInKilos = 2.20462262;
    public static final double kilosInPounds = 0.45359237;
    public static final double centimetersInInches = 2.54;
    public static final double inchesInCentimeters = 0.39370079;

    public static int kilosToPounds(int kilos) {
        double pounds = kilos * poundsInKilos;
        return (int)pounds;
    }

    public static int poundsToKilos(int pounds) {
        double kilos = pounds * kilosInPounds;
        return (int)kilos;
    }

    public static int inchesToCentimeters(int inches) {
        double centimeters = inches * centimetersInInches;
        return (int)centimeters;
    }

    public static int centimetersToInches(int centimeters) {
        double inches = centimeters * inchesInCentimeters;
        return (int)inches;
    }

    public static String inchesToFeet(int inches) {
        int feet = inches / 12;
        int inch = inches % 12;
        return String.valueOf(feet) + "ft" + String.valueOf(inch);
    }

    public static String cmToInchesFeet(int centimeter) {
        return UnitConvertion.inchesToFeet(UnitConvertion.centimetersToInches(centimeter));
    }

}
