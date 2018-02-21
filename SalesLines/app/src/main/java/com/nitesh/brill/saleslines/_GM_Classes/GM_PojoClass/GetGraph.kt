package com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass

/**
 * Created by Nitesh Android on 23-08-2017.
 */
class GetGraph {

    var salevalue: String? = ""
    var Newleads: String? = null
    var Missedfollowedups: String? = null
    var FollowedUps: String? = null
    var Month: String? = null
    var Day: String? = null
    var Day2: String? = null


    constructor(salevalue: String?, Newleads: String?, FollowedUps: String?, Missedfollowedups: String?, Month: String?) {
        this.salevalue = salevalue
        this.Newleads = Newleads
        this.FollowedUps = FollowedUps
        this.Missedfollowedups = Missedfollowedups
        this.Month = Month


    }

    constructor(salevalue: String?, Newleads: String?, FollowedUps: String?, Missedfollowedups: String?, Day: String?, Day2: String?) {
        this.salevalue = salevalue
        this.Newleads = Newleads
        this.FollowedUps = FollowedUps
        this.Missedfollowedups = Missedfollowedups
        this.Day = Day


    }


}