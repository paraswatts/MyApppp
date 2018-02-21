package com.nitesh.brill.saleslines._User_Classes.User_PojoClass

/**
 * Created by Web Designing Brill on 04-07-2017.
 */

class User_SearchFragmentData {

    var name: String? = null
    var numOfSongs: Int = 0
    var thumbnail: Int = 0

    constructor() {}

    constructor(name: String, numOfSongs: Int, thumbnail: Int) {
        this.name = name
        this.numOfSongs = numOfSongs
        this.thumbnail = thumbnail
    }
}