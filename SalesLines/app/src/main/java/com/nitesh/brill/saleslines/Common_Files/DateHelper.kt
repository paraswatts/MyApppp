package com.nitesh.brill.saleslines.Common_Files

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Web Designing Brill on 23-06-2017.
 */

class DateHelper {

    private var date: Date? = null

    private val locale = Locale.getDefault()

    private var simpleDateFormat: SimpleDateFormat? = null

    private var addCurrentTimeZoneOffsetToDate = false

    private var nullDateText = "-"

    constructor(date: Date) {
        if (addCurrentTimeZoneOffsetToDate) {
            this.date = addTimezoneOffsetToDate(date)
        } else {
            this.date = date
        }
    }

    constructor(date: Date, locale: Locale) {
        if (addCurrentTimeZoneOffsetToDate) {
            this.date = addTimezoneOffsetToDate(date)
        } else {
            this.date = date
        }
    }



    //13
    val day: String
        get() {
            simpleDateFormat = SimpleDateFormat("dd", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //13
    val intDay: Int
        get() {
            simpleDateFormat = SimpleDateFormat("dd", locale)
            if (date != null) {
                return Integer.parseInt(simpleDateFormat!!.format(date))
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return 0
            }
        }

    //April
    val monthLongName: String
        get() {
            simpleDateFormat = SimpleDateFormat("MMMM", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //13:30
    val hour: String
        get() {
            simpleDateFormat = SimpleDateFormat("HH:mm", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //1:30 AM
    val hourWithAMPM: String
        get() {
            simpleDateFormat = SimpleDateFormat("h:mm a", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //1:30
    val hourIn12HourFormat: String
        get() {
            simpleDateFormat = SimpleDateFormat("h:mm", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //AM/PM
    val dateAMPM: String
        get() {
            simpleDateFormat = SimpleDateFormat("a", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //Apr
    val monthShortName: String
        get() {
            simpleDateFormat = SimpleDateFormat("MMM", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //Wednesday
    val dayOfTheWeek: String
        get() {
            simpleDateFormat = SimpleDateFormat("EEEE", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //Wed
    val dayOfWeekShortName: String
        get() {
            simpleDateFormat = SimpleDateFormat("EEE", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //04
    val month: String
        get() {
            simpleDateFormat = SimpleDateFormat("MM", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //04
    val intMonth: Int
        get() {
            simpleDateFormat = SimpleDateFormat("MM", locale)
            if (date != null) {
                return Integer.parseInt(simpleDateFormat!!.format(date))
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return 0
            }
        }

    //1993
    val year: String
        get() {
            simpleDateFormat = SimpleDateFormat("yyyy", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //1993
    val intYear: Int
        get() {
            simpleDateFormat = SimpleDateFormat("yyyy", locale)
            if (date != null) {
                return Integer.parseInt(simpleDateFormat!!.format(date))
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return 0
            }
        }

    //19
    val hourOnly: String
        get() {
            simpleDateFormat = SimpleDateFormat("HH", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //54
    val minuteOnly: String
        get() {
            simpleDateFormat = SimpleDateFormat("mm", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }


    //1993
    val seconds: String
        get() {
            simpleDateFormat = SimpleDateFormat("ss", locale)
            if (date != null) {
                return simpleDateFormat!!.format(date)
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return nullDateText
            }
        }

    //1993
    val intSeconds: Int
        get() {
            simpleDateFormat = SimpleDateFormat("ss", locale)
            if (date != null) {
                return Integer.parseInt(simpleDateFormat!!.format(date))
            } else {
               Log.d(DateHelper::class.java!!.getSimpleName(), LOG_TEXT)
                return 0
            }
        }

    /**
     * Adding current time zone offset

     * @param date target date to add time zone offset
     * *
     * @return date with time zone offset
     */
    private fun addTimezoneOffsetToDate(date: Date): Date {
        val hourAndMinuteOffset = DateConverter.currentTimeZoneOffset
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, hourAndMinuteOffset.hour)
        calendar.add(Calendar.MINUTE, hourAndMinuteOffset.minute)

        return calendar.time
    }


    fun addCertainTime(hours: Int, minute: Int): String {

        val now = Calendar.getInstance()

        val tmp = now.clone() as Calendar
        tmp.add(Calendar.MINUTE, minute)

        tmp.add(Calendar.HOUR_OF_DAY, hours)
        val nowPlus = tmp
        travelTime = nowPlus.get(Calendar.HOUR_OF_DAY).toString() + ":" + nowPlus.get(Calendar.MINUTE) + " :" + nowPlus.get(Calendar.SECOND)
        nullDateText = getTime(nowPlus.get(Calendar.HOUR_OF_DAY), nowPlus.get(Calendar.MINUTE))
       Log.d("====dsdsdsddds======", "=======dsddsdsddss=====" + nowPlus.get(Calendar.HOUR_OF_DAY) + ":" + nowPlus.get(Calendar.MINUTE) + "" + nowPlus.get(Calendar.SECOND))
        // + nowPlus.get(Calendar.YEAR) + "" + nowPlus.get(Calendar.MONTH) + ":" + nowPlus.get(Calendar.DAY_OF_MONTH));
        return nullDateText
    }

    //============================================================\\

    fun addCertainDate(hours: Int, minute: Int): String {

        val now = Calendar.getInstance()

        val tmp = now.clone() as Calendar
        tmp.add(Calendar.MINUTE, minute)

        tmp.add(Calendar.HOUR_OF_DAY, hours)
        val nowPlus = tmp
        nullDateText = nowPlus.get(Calendar.YEAR).toString() + "-" + (nowPlus.get(Calendar.MONTH) + 1) + "-" + nowPlus.get(Calendar.DAY_OF_MONTH)

       Log.d("", "==================addCertainDate==================" + nowPlus.get(Calendar.YEAR) + "-" + nowPlus.get(Calendar.MONTH) + "-" + nowPlus.get(Calendar.DAY_OF_MONTH))
        return nullDateText
    }

    //=====================================\\
    fun addCertainDateAfterAdd(hours: Int, min: Int, y: Int, m: Int, d: Int): String {


       Log.d("===========" + y, m.toString() + "=========" + d)

        val now = Calendar.getInstance()

        val tmp = now.clone() as Calendar
        tmp.add(Calendar.MINUTE, min)
        tmp.add(Calendar.HOUR_OF_DAY, hours)

        tmp.set(Calendar.YEAR, y)
        tmp.set(Calendar.DAY_OF_MONTH, d)
        tmp.set(Calendar.MONTH, m)
        val nowPlus = tmp


        nullDateText = nowPlus.get(Calendar.YEAR).toString() + "-" + nowPlus.get(Calendar.MONTH) + "-" + nowPlus.get(Calendar.DAY_OF_MONTH)

        return nullDateText
    }


    fun getTime(h: Int, m: Int): String {
        val time: String
        var min = "" + m
        var hours = 0
        var hrs = "" + h
        if (h > 12) {
            hours = h - 12
            hrs = "" + hours
            time = " PM"

        } else {
            time = " AM"
        }

        if (min.length == 1) {

            min = "0" + m
        } else {
            min = "" + m
        }


        if (hours == 0) {
            hours = 12
        }

        if (hrs.length == 1) {
            hrs = "0" + hrs
        }
        return "" + hrs + ":" + min + time
    }

    /**
     * if you want addCurrentTimeZoneOffset to your date, just cal
     * l this method
     */
    fun shouldAddCurrentTimeZoneOffsetToDate() {
        addCurrentTimeZoneOffsetToDate = true
    }

    /**
     * set text in case if your date is null

     * @param nullDateText text to set for null date
     */
    fun setNullDateText(nullDateText: String) {
        this.nullDateText = nullDateText
    }

    /**
     * get given date

     * @return date
     */
    val givenDate: Date?
        get() {
            if (date == null) {
               Log.d("GETTING GIVEN DATE ", "GIVEN DATE IS NULL!")
                return null
            } else
                return date
        }

    class DatePair internal constructor(val hour: Int, val minute: Int)


    //=================================================\\
    enum class DatePatterns private constructor(private val datePattern: String) {

        SIMPLE_DATE_PATTERN("yyyy MMM"),
        SIMPLE_DATE_PATTERN_WITH_DAY("yyyy MMM dd"),
        DATE_PATTERN_WITH_T("yyyy-MM-dd'T'HH:mm:ss"),
        DATE_PATTERN_DATE_PATTERN_WITH_T_AND_AP_PM_WITH_T("yyyy-MM-dd'T'hh:mm:ss a"),
        USUAL_DATE_PATTERN("yyyy-MM-dd HH:mm:ss"),
        ISO8601_DATE_PATTERN("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        ISO8601_DATE_PATTERN_SECOND_TYPE("yyyy/MM/dd'T'HH:mm:ss'Z'"),
        PATTERN_1("EEE MMM dd HH:mm:ss yyyy"),
        PATTERN_2("yyyy-MM-dd'T'HH:mm:ss"),
        PATTERN_3("yyyy-MM-dd HH:mm:ss"),
        PATTERN_4("yyyy-MM-dd HH:mm:ss Z"),
        PATTERN_5("yyyy-MM-dd'T'HH:mm:ssZ"),
        PATTERN_6("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        PATTERN_7("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        PATTERN_8("yyyy-MM-dd'T'HH:mm:ss Z"),
        PATTERN_9("dd/MM/yyyy"),
        PATTERN_10("dd-MM-yyyy HH:mm:ss"),
        PATTERN_11("dd-MMM-yyyy"),
        PATTERN_12("MM dd, yyyy"),
        PATTERN_13("E, MMM dd yyyy"),
        PATTERN_14("E, MMM dd yyyy HH:mm:ss"),
        PATTERN_15("yyyy-MMM-dd");

        override fun toString(): String {
            return datePattern
        }
    }


    //==========================================\\
    object DateConverter {

        /**
         * Convert date from String to Date format

         * @param date        string date
         * *
         * @param datePattern for parsing
         * *
         * @return formatted Date
         */
        fun stringToDate(date: String, datePattern: String): Date? {

            val format = SimpleDateFormat(datePattern, Locale.ENGLISH)
            try {
                return format.parse(date)
            } catch (e: ParseException) {
               Log.d(" " + e.message, " Please check if you have valid pattern! ")
            }

            return null
        }

        /**
         * Convert date from given Strings to Date format

         * @param date         string date
         * *
         * @param datePatterns patterns for parsing
         * *
         * @return formatted Date
         */
        fun stringToDate(date: String, vararg datePatterns: String): Date? {

            for (datePattern in datePatterns) {
                val format = SimpleDateFormat(datePattern, Locale.ENGLISH)
                try {
                    return format.parse(date)
                } catch (e: ParseException) {
                    if (datePatterns.size > 1)
                       Log.d("Trying to parse ", "Failed, trying next pattern")
                    else
                       Log.d("Trying to parse ", "Failed to parse")
                }

            }
            return null
        }

        /**
         * hourAndMinuteOffset[0] hour offset
         * hourAndMinuteOffset[1] minute offset

         * @return hourAndMinuteOffset array, current time zone offset
         */
        val currentTimeZoneOffset: DatePair
            get() {
                val calendar = Calendar.getInstance(TimeZone.getDefault(),
                        Locale.getDefault())
                val currentLocalTime = calendar.time
                val date = SimpleDateFormat("Z", Locale.getDefault())
                val localTime = date.format(currentLocalTime)

                val hourOffset = Integer.valueOf(localTime.substring(1, 3))!!
                val minuteOffset = Integer.valueOf(localTime.substring(3, localTime.length))!!

                return DatePair(hourOffset!!, minuteOffset!!)
            }
    }

    companion object {

        private val LOG_TEXT = "date must not be null"
        var travelTime: String = ""
    }


}

