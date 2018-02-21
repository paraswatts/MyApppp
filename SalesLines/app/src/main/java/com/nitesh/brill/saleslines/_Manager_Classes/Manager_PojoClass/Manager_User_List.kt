package com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass

/**
 * Created by Nitesh Android on 12-08-2017.
 */

class Manager_User_List(var `$id`: String,
                        var UserId: Int,
                        var Name: String,
                        var ManagerId: String) {

    override fun toString(): String {
        return "ClassPojo [Name = $Name,   UserId = $UserId,  \$id = $`$id`,ManagerId = $ManagerId"
    }

}