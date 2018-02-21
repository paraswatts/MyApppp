package com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass

/**
 * Created by Nitesh Android on 09-08-2017.
 */
class Manager_Total_Target(
        var `$id`: String,
        var TargetId: String,
        var EmployeeName: String,
        var Target: String,

        var ProductName: String
        ) {


    override fun toString(): String {
        return "ClassPojo [ Target = $Target," +
                "  EmployeeName = $EmployeeName," +
                "  ProductName = $ProductName," +
                " \$id = $`$id` ,TargetId = $TargetId"
    }




}