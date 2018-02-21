package com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass

/**
 * Created by Nitesh Android on 09-08-2017.
 */
class Manager_GetPreviousDetails(
        var `$id`: String,
        var CompanyName: String,
        var Id: String,
        var Designation: String,
        var DateOfLeaving: String,
        var DateOfJoin: String,
        var SectorPreviouslyWorked: String) {


    override fun toString(): String {
        return "ClassPojo [Id = $Id, DateOfLeaving = $DateOfLeaving," +
                " DateOfJoin = $DateOfJoin, SectorPreviouslyWorked = $SectorPreviouslyWorked, Designation = $Designation," +
                " \$id = $`$id` ,CompanyName = $CompanyName"
    }
}