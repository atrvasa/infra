package com.atrvasa.infra;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * this abstract class contain some tools for work with
 * Georgian and Persian date
 */
public abstract class DateTools {

    /**
     * convert georgian date to persian date and return year
     *
     * @param date Georgian date object
     * @return int as a persian year
     */
    public static int getPersianYear(Date date) {
        if (date == null)
            return 0;

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int[] x = DateLoader.gregorian_to_jalali(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        return x[0];
    }

    /**
     * convert georgian date to persian date and return number of month
     *
     * @param date Georgian date object
     * @return int as a number of persian month
     */
    public static int getPersianMonth(Date date) {
        if (date == null)
            return 0;

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int[] x = DateLoader.gregorian_to_jalali(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        return x[1];
    }

    /**
     * convert startDate and endDate to persian date and
     * check the comparable year and month between start date and end date
     *
     * @param comparableYear persian year number such as 1399
     * @param comparableMonth persian month as number from 1 to 12
     * @param startDate Georgian date object
     * @param endDate Georgian date Object
     * @return return ture if comparable year and month between start and end date
     */
    public static boolean inPersianDateRange(int comparableYear, int comparableMonth, Date startDate, Date endDate) {
        int startYear = 0, startMonth = 0, endYear = 0, endMonth = 0;
        if (startDate == null)
            return false;
        else {
            startYear = DateTools.getPersianYear(startDate);
            startMonth = DateTools.getPersianMonth(startDate);
        }
        boolean biggerThenStart = comparableYear > startYear || (comparableYear == startYear && comparableMonth >= startMonth);

        if (!biggerThenStart)
            return false;

        if (endDate == null)
            return true;

        endYear = DateTools.getPersianYear(endDate);
        endMonth = DateTools.getPersianMonth(endDate);

        if (comparableYear < endYear) return true;
        if (comparableYear == endYear && comparableMonth <= endMonth) return true;


        return false;
    }

    /**
     * Convert time to Georgian date and based toCultureName convert to culture
     * @param time string contain georgian date
     * @param toCultureName culture name such as 'en', or 'fa'
     * @return translated time to culture in parameter as string
     */
    public static String translatingDate(String time, String toCultureName) {
        Date date = getDate(time, "en");
        return getDateOnCulture(date, toCultureName);
    }

    /**
     * Convert string to Georgian date based on fromCultureName
     * first convert string date to georgian if it is persian date
     * then create instance of Date
     * @param strDate time as a string
     * @param fromCultureName culture name such as 'en', or 'fa'
     * @return Georgian date object
     */
    public static Date getDate(String strDate, String fromCultureName) {
        DateLoader loader = new DateLoader(strDate);

        int year = 0, month = 0, day = 0;
        switch (fromCultureName) {
            case "fa":
                int[] p = DateLoader.jalali_to_gregorian(loader.year, loader.month, loader.day);
                year = p[0];
                month = p[1];
                day = p[2];
                break;
            case "en":
                year = loader.year;
                month = loader.month;
                day = loader.day;
                break;
        }

        Calendar calendar = new GregorianCalendar(year, month - 1, day, loader.hour, loader.minute, loader.second);
        return calendar.getTime();
    }

    /**
     * create Georgian date of current time and convert to culture name
     * @param cultureName culture name such as 'en', or 'fa'
     * @return string that contains converted date to culture name
     */
    public static String getCurrentDateOnCulture(String cultureName) {
        Date currentDate = new Date();
        return getDateOnCulture(currentDate, cultureName);
    }

    /**
     * convert Georgian date to culture name and return as a string, if withoutTime
     * param set to false remove data about clock.
     *
     * @param date Georgian data object
     * @param cultureName culture name such as 'en', or 'fa'
     * @param withoutTime return clock or not
     * @return string that contains date and clock if withoutTime set to true.
     */
    public static String getDateOnCulture(Date date, String cultureName, boolean withoutTime) {
        String strDate = getDateOnCulture(date, cultureName);
        if (!withoutTime)
            return strDate;
        else {
            String[] strList = strDate.split(" ");
            return strList[0];
        }
    }

    /**
     * convert Georgian date to culture name and return as a string.
     *
     * @param date Georgian data object
     * @param cultureName culture name such as 'en', or 'fa'
     * @return string that contains date and clock.
     */
    public static String getDateOnCulture(Date date, String cultureName) {
        switch (cultureName) {
            case "fa":
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int[] x = DateLoader.gregorian_to_jalali(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                return DefaultPatterns.DATE_PATTERN
                        .replace("yyyy", x[0] + "")
                        .replace("MM", x[1] + "")
                        .replace("dd", x[2] + "")
                        .replace("HH", calendar.get(Calendar.HOUR) + "")
                        .replace("mm", calendar.get(Calendar.MINUTE) + "")
                        .replace("ss", calendar.get(Calendar.SECOND) + "");
            case "en":
                DateFormat format = new SimpleDateFormat(DefaultPatterns.DATE_PATTERN);
                return format.format(date);
            default:
                return "";
        }
    }

    /**
     * Convert date param to Georgian string time based on date pattern
     * @param date Georgian date object
     * @return string that contain date and time
     */
    public static String toGeorgianString(Date date) {
        if (date == null)
            return null;
        DateFormat df = new SimpleDateFormat(DefaultPatterns.DATE_PATTERN);
        try {
            return df.format(date);
        } catch (Exception ex) {
            return null;
        }
    }
}
