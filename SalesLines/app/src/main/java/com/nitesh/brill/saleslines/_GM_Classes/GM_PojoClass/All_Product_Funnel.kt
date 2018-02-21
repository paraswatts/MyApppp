package com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass

class All_Product_Funnel(

        var `$id`: String = "",
        var month: String = "",
        var leadcount: String = "",
        var leadstages: String = ""


) {


    override fun toString(): String {
        return "ClassPojo [$`$id` = $`$id`, month = $month,leadcount = $leadcount,leadstages = $leadstages"
    }
}