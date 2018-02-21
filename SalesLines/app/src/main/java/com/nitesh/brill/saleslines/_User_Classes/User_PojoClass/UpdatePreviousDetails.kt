package com.nitesh.brill.saleslines._User_Classes.User_PojoClass

import java.util.*

/**
 * Created by Nitesh Android on 16-08-2017.
 */
class UpdatePreviousDetails(

        var CompanyName: String,
        var Designation: String,
        var DateOfJoin: Date,
        var DateOfLeaving: Date,
        var SectorPreviouslyWorked: String,
        var Id: Int
) {

    override fun toString(): String {
        return "ClassPojo [DateOfJoin = $DateOfJoin, DateOfLeaving = $DateOfLeaving," +
                " SectorPreviouslyWorked = $SectorPreviouslyWorked, Id = $Id,"+
        " CompanyName = $CompanyName]" +
                "v = $Designation]"
    }
}