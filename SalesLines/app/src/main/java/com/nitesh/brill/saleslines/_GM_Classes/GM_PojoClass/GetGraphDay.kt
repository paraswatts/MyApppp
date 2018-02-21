package com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass

/**
 * Created by Nitesh Android on 23-08-2017.
 */
class GetGraphDay {

    var salevalue: String? = ""
    var Newleads: String? = null
    var Missedfollowedups: String? = null
    var FollowedUps: String? = null

    var Day: String? = null



    constructor(salevalue: String?, Newleads: String?, FollowedUps: String?, Missedfollowedups: String?, Day: String?) {
        this.salevalue = salevalue
        this.Newleads = Newleads
        this.FollowedUps = FollowedUps
        this.Missedfollowedups = Missedfollowedups
        this.Day = Day


    }


}