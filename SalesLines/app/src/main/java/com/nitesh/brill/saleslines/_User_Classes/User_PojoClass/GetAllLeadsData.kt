package com.nitesh.brill.saleslines._User_Classes.User_PojoClass

/**
 * Created by Nitesh Android on 17-08-2017.
 */
class GetAllLeadsData {
    var Data_PC: String = ""
    var leadname: String = ""
    var Phone: String = ""
    var type: String = ""
    var rating: String = ""
    var LeadId: String = ""
    var EnquiryDate: String = ""
    var EnquiryId: String = ""
    var DemoId: String = ""
    var ManagerId: String = ""
    var UserId: String = ""


    constructor(Data_PC: String, leadname: String, type: String, LeadId: String, Phone: String, rating: String, EnquiryDate: String, EnquiryId: String, DemoId: String, ManagerId: String, UserId: String) {

        this.Data_PC = Data_PC
        this.leadname = leadname
        this.type = type
        this.LeadId = LeadId
        this.Phone = Phone
        this.rating = rating
        this.EnquiryDate = EnquiryDate
        this.EnquiryId = EnquiryId
        this.DemoId = DemoId
        this.ManagerId = ManagerId
        this.UserId = UserId


    }

}
