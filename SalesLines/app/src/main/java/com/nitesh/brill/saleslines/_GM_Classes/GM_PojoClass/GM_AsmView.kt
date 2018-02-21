package com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass

/**
 * Created by Nitesh Android on 11-08-2017.
 */
class GM_AsmView  (var `$id`: String,
        var UserId: Int,
        var RoleId: Int,
        var Name: String="")
{

    override fun toString(): String {
        return "ClassPojo [Name = $Name,   UserId = $UserId,  \$id = $`$id`, RoleId = $RoleId]"
    }

}