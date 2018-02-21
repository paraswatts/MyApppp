package com.nitesh.brill.saleslines._User_Classes.User_PojoClass

/**
 * Created by Nitesh Android on 16-08-2017.
 */
class User_Response(
        var Success: String="",
        var Message: String=""

) {

    override fun toString(): String {
        return "ClassPojo [Message = $Message, Success = $Success"
    }


}