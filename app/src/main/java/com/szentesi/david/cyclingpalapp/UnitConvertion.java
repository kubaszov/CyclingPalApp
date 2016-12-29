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

    public static double calculateBMI (int userWeight, int userHeight) {
        double d = ((double)userWeight / ((double)userHeight * (double)userHeight)) * 10000;
        // round up double to two decimal places
        // http://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java
        return Math.round(d * 100.0)/ 100.0;

    }

    public static double calculateInitialCaloriIntake(int userAge, int userWeight, int userHeight, String userGender) {
        double calories = 0.0;
        if(userGender.equals("Male")) {
            calories = (10 * userWeight) + (6.25 * userHeight) - (5 * userAge) + 5;
        }
        else if (userGender.equals("Female")){
            calories = (10 * userWeight) + (6.25 * userHeight) - (5 * userAge) - 161;
        }
        return calories;
    }

}
