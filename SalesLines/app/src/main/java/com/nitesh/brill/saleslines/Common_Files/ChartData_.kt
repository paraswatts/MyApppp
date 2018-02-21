package com.nitesh.brill.saleslines.Common_Files

/**
 * Created by Nitesh Android on 03-10-2017.
 */

import android.app.Application
import java.io.Serializable

class ChartData_ : Application, Serializable {
    var pyramidLabel: String? = null
    var pyramid_value: Int = 0


    constructor(label: String, value: Int) {
        this.pyramidLabel = label
        this.pyramid_value = value


    }

//    /*Comparator for sorting the list by roll no*/
//    var StuRollno: Comparator<ChartData_> = object : Comparator<ChartData_>{
//
//        override fun compare(s1: ChartData_, s2: ChartData_): Int {
//
//            val rollno1 = s1.pyramid_value
//            val rollno2 = s2.pyramid_value
//
//            /*For ascending order*/
//            return rollno1 - rollno2
//
//            /*For descending order*/
//            //rollno2-rollno1;
//        }
//    }


}
