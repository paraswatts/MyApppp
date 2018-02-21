package com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass

/**
 * Created by Nitesh Android on 09-08-2017.
 */
class Manager_GetUserProfileData(

        var Name: String="",

        var EmailId: String="",

        var password: String="",

        var Phone: String="",
        var  RoleId:String="",
        var LinkedIn: String="",

        var `$id`: String="",

        var TotalExperience: String="",

        var DOB: String="",

        var Education: String="",

        var UserId: String="",

        var UName: String="",

        var CompanyName: String="",

        var DOJ: String="",

        var Designation: String=""
)
{
    override fun toString(): String {
        return "ClassPojo [Name = $Name, EmailId = $EmailId, RoleId = $RoleId," +
                " password = $password, Phone = $Phone, LinkedIn = $LinkedIn," +
                " \$id = $`$id`, TotalExperience = $TotalExperience]," +
                " DOB = $DOB], Education = $Education]," +
                " UserId = $UserId], UName = $UName]," +
                " CompanyName = $CompanyName], DOJ = $DOJ], " +
                "v = $Designation]"
    }

}