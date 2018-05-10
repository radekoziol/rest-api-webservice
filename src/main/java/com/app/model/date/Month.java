package com.app.model.date;

/**
 * Handles useful month methods.
 */
public enum Month {

    January("31"),
    February1("28"),
    /**
     * Leap year February
     */
    February2("29"),
    March("31"),
    April ("30"),
    May ("31"),
    June("30"),
    July ("31"),
    August ("31"),
    September ("30"),
    October ("31"),
    November ("30"),
    December ("31");


    private String day;

    /**
     * @param day
     */
    Month(String day){
        this.day = day;
    }

    /**
     * @param month as int
     * @param year as int
     * @return month as Enum Month
     */
    public static Month getMonth(int month, int year){
        if(Year.isLeapYear(year) && month == 2)
            return February2;
        else if(month <= 2)
            return Month.values()[month - 1];
        return  Month.values()[month];
    }

    /**
     * @param day as int
     * @param month as int
     * @param year as int
     * @return true if day is within month
     * @throws IllegalArgumentException if day is not within month
     */
    public static boolean dayIsWithinMonth(int day, int month, int year) throws IllegalArgumentException{
        Month m = Month.getMonth(month, year);
        return m.getDayAsInt() - day >= 0;

    }

    /**
     * @param year
     * @return next month in given year
     */
    public Month getNextMonth(int year){

        if(Year.isLeapYear(year) && this.equals(Month.January))
            return Month.February2;
        else if(this.equals(Month.February1))
            return Month.March;
        else
            return values()[(this.ordinal()+1) % values().length];
    }

    /**
     * @param year
     * @return previous month in given year
     */
    public Month getPreviousMonth (int year){

        if(!Year.isLeapYear(year) && this.equals(Month.March))
            return Month.February1;
        else if(this.equals(Month.February2))
            return Month.January;
        else if (this.ordinal() == 0)
            return Month.December;
        else
            return values()[(this.ordinal()-1) % values().length];
    }


    /**
     * @return how many days month contains as int
     */
    public int getDayAsInt() {
        return Integer.parseInt(day);
    }

    public int getMonthAsInt(){
        if(this.ordinal() > 2)
            return this.ordinal();
        else if(this.equals(Month.January))
            return 1;
        return 2;
    }

    public String getDay() { return day;}
}

