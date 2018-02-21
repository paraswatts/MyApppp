package com.nitesh.brill.saleslines._User_Classes.User_PojoClass

/**
 * Created by Nitesh Android on 19-08-2017.
 */
class SingleLeadDetail(var `$id`: String,
                       var EnquiryNum: String,

                       var NextInteractionBy: String,
                       var NextInteractionDate: String

) {

    override fun toString(): String {
        return "ClassPojo [$`$id` = $`$id`,   EnquiryNum =  $NextInteractionBy, NextInteractionBy =  $EnquiryNum,  \$NextInteractionDate = $`NextInteractionDate`"
    }

}