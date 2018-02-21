package com.nitesh.brill.saleslines._User_Classes.User_PojoClass

/**
 * Created by Nitesh Android on 30-08-2017.
 */
class LeadDemo(var DemoDate: String = "",
               var Interaction_By: String = "",
               var CreatedDate: String = "",
               var H_Checked: String = "",
               var Comments: String = ""

) {

    override fun toString(): String {
        return "ClassPojo [DemoDate = $DemoDate, Interaction_By = $Interaction_By,CreatedDate = $CreatedDate,H_Checked = $H_Checked,Comments = $Comments"
    }


}