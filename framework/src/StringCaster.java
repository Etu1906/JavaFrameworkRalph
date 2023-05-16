package model.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Time;

public class StringCaster {

    public static Object cast(String input , Class<?> clazz) {
        System.out.println(" la classe :  "+clazz.getName());
        System.out.println(" simpleName :  "+clazz.getSimpleName());
        System.out.println(" caster 2");
        // Try to cast to integer
        try {
            if( clazz.getName().compareToIgnoreCase("int") == 0 || clazz.getSimpleName().compareToIgnoreCase("integer") == 0 ){
                if( input == null )
                    return 0;
                return Integer.parseInt(input);
            }
        } catch (Exception e) {}
        
        if( clazz.getSimpleName().compareToIgnoreCase("float") == 0 || clazz.getSimpleName().compareToIgnoreCase("float") == 0  ){
            // Try to cast to float
            try {
                if( input == null )
                    return 0;
                return Float.parseFloat(input);
            } catch (Exception e) {}
        }
        if( clazz.getSimpleName().compareToIgnoreCase("double") == 0 || clazz.getSimpleName().compareToIgnoreCase("double") == 0  ){
            // Try to cast to double
            try {
                if( input == null )
                    return 0;
                return Double.parseDouble(input);
            } catch (Exception e) {}
        }
        // Try to cast to long
        if( clazz.getSimpleName().compareToIgnoreCase("long") == 0 || clazz.getSimpleName().compareToIgnoreCase("long") == 0  ){
            try {
                if( input  == null )
                    return 0;
                return Long.parseLong(input);
            } catch (Exception e) {}
        }
        // Try to cast to BigDecimal
        try {
            return new BigDecimal(input);
        } catch (Exception e) {}

        if( input != null ){
            if (  clazz.getName().compareTo("boolean") == 0  || clazz.getSimpleName().compareToIgnoreCase("boolean") == 0   )
                return Boolean.parseBoolean(input);
        }

        // Try to cast to Timestamp
        try{
            Timestamp timestamp = Timestamp.valueOf(input);
            return timestamp;
        }catch( Exception ex ) {}

        // Try to cast to Time
        try{
            Time time = Time.valueOf(input);
            return time;
        }catch( Exception e ){}
        
        if(  clazz.getName().compareToIgnoreCase("java.sql.date") == 0  ){
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
                } catch (Exception e) {}
            }
        }
        if(  clazz.getName().compareToIgnoreCase("java.util.date") == 0  ){
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
                } catch (Exception e) {}
            }
        }
        // Could not cast to any known type, return input as string
        return input;
    }

}
