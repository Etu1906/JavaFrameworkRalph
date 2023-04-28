package model.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Time;

public class StringCaster {

    public static Object cast(String input) {
        // Try to cast to integer
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {}
        
        // Try to cast to float
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {}

        // Try to cast to double
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {}

        // Try to cast to long
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {}
        
        // Try to cast to BigDecimal
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {}

        try{
            return Boolean.parseBoolean(input);
        }catch( Exception e ){}


        // Try to cast to Timestamp
        try{
            Timestamp timestamp = Timestamp.valueOf(input);
            return timestamp;
        }catch( ParseException e ) {}

        // Try to cast to Time
        try{
            Time time = Time.valueOf(input);
            return time;
        }catch( ParseException e ){}
        

        // Try to cast to java.sql.Date
        DateFormat[] sqlDateFormats = {
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("MM/dd/yyyy"),
            new SimpleDateFormat("dd/MM/yyyy")
        };
        for (DateFormat dateFormat : sqlDateFormats) {
            try {
                java.util.Date utilDate = dateFormat.parse(input);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                return sqlDate;
            } catch (ParseException e) {}
        }

        // Try to cast to java.util.Date
        DateFormat[] utilDateFormats = {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"),
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        };
        for (DateFormat dateFormat : utilDateFormats) {
            try {
                java.util.Date date = dateFormat.parse(input);
                return date;
            } catch (ParseException e) {}
        }

        // Could not cast to any known type, return input as string
        return input;
    }

}
