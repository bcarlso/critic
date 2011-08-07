package net.bcarlso.critic;

import java.util.Calendar;
import java.util.Date;

public class Helpers {
    public static Date july(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(2011, Calendar.JULY, day);
        return calendar.getTime();
    }
}
