package org.ij.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Abdelkader Midani on 26/10/14.
 */
public class DateUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public static int getNbMinutesFromTime(String time){
        String[] timeSplit = time.split(":");

        int hours = Integer.parseInt(timeSplit[0]);
        int minutes = Integer.parseInt(timeSplit[1]);

        return hours * 60 + minutes;
    }

    public static String getArrivalTime(String time, String term){
        Date dt = null;
        try {
            dt = sdf.parse(time);
            dt.setTime(dt.getTime()+getNbMinutesFromTime(term)*60*1000);
            return sdf.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isBefore(String departureTime, String arrivalTime){
        if(StringUtils.isEmpty(arrivalTime)){
            return true;
        }
        try {
            Date dtD = sdf.parse(departureTime);
            Date dtA = sdf.parse(arrivalTime);
            return dtD.after(dtA);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

}
