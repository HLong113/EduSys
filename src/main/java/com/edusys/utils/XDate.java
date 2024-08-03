    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author pc
 */
public class XDate {
      static SimpleDateFormat formater = new SimpleDateFormat();
    public static Date toDate(String date, String pattern){//chuyển string qua date-dd/mm/yyyy
        try {
            formater.applyPattern(pattern);
            return formater.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static String toString(Date date, String pattern){//chuyển date qua string
        formater.applyPattern(pattern);
        return formater.format(date);
    }
    public static Date now(){
        return new Date();
    }
    public static Date addDays (Date date, long days){
        date.setTime(date.getTime() + days*24*60*60*1000);
        return date;
    }
    
}
