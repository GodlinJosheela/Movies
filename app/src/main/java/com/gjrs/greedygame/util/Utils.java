package com.gjrs.greedygame.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String convertDateFormat(String date, String fromFormat, String toFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fromFormat);
        LocalDate dateTime = LocalDate.parse(date, formatter);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(toFormat);

        return dateTime.format(formatter2);
    }

    public static boolean compareDates(String d1, String d2) {
        try {
            // If you already have date objects then skip 1

            //1
            // Create 2 dates starts
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1" + sdf.format(date1));
            System.out.println("Date2" + sdf.format(date2));

            // Create 2 dates ends
            //1

            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return true;
            }
            // before() will return true if and only if date1 is before date2
            if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return false;
            }

            //equals() returns true if both the dates are equal
            if (date1.equals(date2)) {
                System.out.println("Date1 is equal Date2");
                return true;
            }

            System.out.println();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }
}
