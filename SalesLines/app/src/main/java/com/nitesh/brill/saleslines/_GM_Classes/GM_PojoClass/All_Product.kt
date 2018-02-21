package com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass

class All_Product(

        var `$id`: String = "",
        var productCount: String = "",
        var productsold: String = ""


) {


    override fun toString(): String {
        return "ClassPojo [$`$id` = $`$id`, productCount = $productCount,productsold = $productsold"
    }
}