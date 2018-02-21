package com.nitesh.brill.saleslines.Authenticate

/**
 * Created by Nitesh Android on 14-07-2017.
 */

class LoginResponse(
        var Name: String,

        var ManagerId: String,

        var CompanyName: String?,

        var UserId: String?,

        var ClientId: String?,

        var `$id`: String?,

        var RoleId: String?
) {


    override fun toString(): String {
        return "ClassPojo [Name = $Name, ManagerId = $ManagerId, CompanyName = $CompanyName, UserId = $UserId, ClientId = $ClientId, \$id = $`$id`, RoleId = $RoleId]"
    }
}
