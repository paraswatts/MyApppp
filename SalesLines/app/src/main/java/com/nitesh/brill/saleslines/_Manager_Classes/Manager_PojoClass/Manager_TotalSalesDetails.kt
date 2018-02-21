package com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass

/**
 * Created by Nitesh Android on 09-08-2017.
 */
class Manager_TotalSalesDetails(
        var `$id`: String,
        var EnquiryNum: String,
        var leadname: String,


        var ClosedDate: String,
        var ProductSold: String,
        var Salevalue: String,
        var sman: String) {


    override fun toString(): String {
        return "ClassPojo [ ClosedDate = $ClosedDate," +
                " ProductSold = $ProductSold, SectorPreviouslyWorked = $Salevalue, leadname = $leadname," +
                " \$id = $`$id` ,EnquiryNum = $EnquiryNum"
    }
}