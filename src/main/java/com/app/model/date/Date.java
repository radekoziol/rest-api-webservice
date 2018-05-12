package com.app.model.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Math.abs;
import static java.lang.Math.decrementExact;

/**
 * Supports yyyy-mm-dd date (without time)
 */
public class Date {

    private String year;
    private String month;
    private String day;

    /**
     * @param date String in format "yyyy-mm-dd"
     */
    public Date(String date){
        checkDate(date);
        String [] temp = date.split("-",3);
        this.year = temp[0];
        this.month = temp[1];
        this.day = temp[2];
    }

    /**
     * @param year String in format "yyyy"
     * @param month String in format "mm"
     * @param day String in format "dd"
     */
    public Date(String year, String month, String day) {
        checkDate(year + "-" + month  + "-" +  day);
        this.year = year;
        this.month = month;
        this.day = day;
    }


    /**
     * This method checks if data is in valid format (yyyy-mm-dd)
     * Note that it is assumed that year must be > 1582
     * @param input input
     * @throws IllegalArgumentException if date is invalid or impossible
     * @// TODO: 08.02.18 assumption that year > 1582 may have troubles
     *
     */
    private void checkDate(String input) throws IllegalArgumentException {

        //Firstly we check if number of chars is correct
        if(!input.matches("\\d{4}-\\d{1,2}-\\d{1,2}"))
            throw new IllegalArgumentException("This date: " + input + " is impossible!");

        String [] temp = input.split("-",3);
        int year = Integer.parseInt(temp[0]);
        int month = Integer.parseInt(temp[1]);
        int day = Integer.parseInt(temp[2]);

        //Finally we check if this date is even possible
        if(!( ((year > 1582 ) && ((month > 0))
                && (month<= 12)) && ((day > 0)
                && (day <= 31)) && (Month.dayIsWithinMonth(day,month,year)) ))
            throw new IllegalArgumentException("This date: " + input + " is impossible!");

    }


    /**
     * @param endDate
     * @return true if current date is later than given one
     * @// TODO: 08.02.18 can this function be simplified?
     */
    //        ISO 4217
    public boolean isLaterThan(Date endDate) {

        if(this.getYear() - endDate.getYear() > 0)
            return true;
        else if (this.getYear() - endDate.getYear() < 0)
            return false;
        else if (this.getMonth() - endDate.getMonth() > 0)
            return true;
        else if (this.getMonth() - endDate.getMonth() < 0)
            return false;
        else return this.getDay() - endDate.getDay() > 0;
    }


    /**
     * This method shift given date for given day number
     * @param dayNumber
     * @return shifted date
     * @// TODO: 08.02.18 Can this function be simplified? Surely..
     */
    public Date shiftDate(int dayNumber) {

        //Firstly let's introduce later useful variables
        int yearAdd = 0;
        Month month = Month.getMonth(getMonth(), getYear() + yearAdd);

        //Next let's count years
        while (dayNumber >= 365) {

            //If next year is leap year and we need to consider february
            if (Year.isLeapYear(getYear() + yearAdd + 1)
                    && month.ordinal() > 2) {
                if(dayNumber >= 366) {
                    yearAdd++;
                    dayNumber -= 366;
                }
                else break;
            }
            //Else if we want to jump leap year we should consider if we are before february
            else if (Year.isLeapYear(getYear() + yearAdd )
                    && month.ordinal() <= 2) {
                if(dayNumber >= 366) {
                    yearAdd++;
                    dayNumber -= 366;
                }
                else break;
            }
            //Standard case
            else {
                yearAdd++;
                dayNumber -= 365;
            }

        }

        //Next months
        //Updating month
        month = Month.getMonth(getMonth(), getYear() + yearAdd);
        while (dayNumber >= month.getDayAsInt()) {
            dayNumber -= month.getDayAsInt();
            month = month.getNextMonth(getYear() + yearAdd);
            //Adding year if we pass December
            if (month.equals(Month.January))
                yearAdd++;
        }



        /*
         * Finally days, which are now < 31
         * Men.. I'm tired already - me too :<
         * Firstly, we check if we don't have to switch month
         */
        if (getDay() + dayNumber <= month.getDayAsInt()){
            dayNumber += getDay();
        }
        else{
            dayNumber = abs(month.getDayAsInt() - getDay() - dayNumber);
            month = month.getNextMonth(getYear() + yearAdd);
            if (month.equals(Month.January))
                yearAdd++;
        }

        return new Date(
                String.valueOf(getYear() + yearAdd),
                String.valueOf(month.getMonthAsInt()),
                String.valueOf(dayNumber));

    }

    /**
     * This method implements binary search with shiftDate method.
     * Note that day difference may be also negative
     * @param startDate
     * @param endDate
     * @return Day difference between startDate and endDate
     *
     */
    public static int dayDifference(Date startDate, Date endDate) {

        // Firstly let's calculate year difference, which will be useful for binary search
        int yearDifference = abs(startDate.getYear() - endDate.getYear());

        // If startDate is later than endDate we need to swap and multiply by -1
        if(startDate.isLaterThan(endDate))
            return -1 * searchBinaryDayDifference
                (0,((yearDifference + 1) * 365), endDate, startDate);

        return searchBinaryDayDifference
                (0,((yearDifference + 1) * 365), startDate, endDate);

    }

    /**
     * @param l search from l
     * @param r search to r
     * @param startDate
     * @param endDate
     * @return Day difference between startDate and endDate
     */
    private static int searchBinaryDayDifference(int l, int r, Date startDate, Date endDate) {

        while(l != r) {
            int mid = (l + r) / 2;

            if (startDate.shiftDate(mid).equals(endDate))
                return mid;

            if (startDate.shiftDate(mid).isLaterThan(endDate))
                r = mid;
            else
                l = mid;
        }

        return l;
    }

    /**
     * @return current date - thanks to DataTimeFormatter
     */
    public static Date getCurrentDate(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String[] date = dtf.format(now).split("-",3);
        return new Date(date[0], date[1], date[2]);
    }


    /**
     * @return year as int
     */
    private int getYear() {
        return Integer.parseInt(year);
    }

    /**
     * @return month as int
     */
    private int getMonth() {
        return Integer.parseInt(month);
    }

    /**
     * @return day as int
     */
    private int getDay() {
        return Integer.parseInt(day);
    }


    /**
     * @return date representation in yyyy-mm-dd
     */
    @Override
    public String toString() {

        String output = year + "-";

        if(month.length() == 1)
            output += "0" + month + "-";
        else output += month + "-";

        if(day.length() == 1)
            output += "0" + day;
        else output += day;

        return output;
    }

    /**
     * @param obj
     * @return true if other object toString method returns the same
     */
    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }


}
