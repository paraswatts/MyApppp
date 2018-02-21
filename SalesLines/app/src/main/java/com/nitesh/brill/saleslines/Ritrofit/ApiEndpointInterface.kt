package com.nitesh.brill.saleslines.Ritrofit


import com.google.gson.JsonElement
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.All_Product
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.All_Product_Funnel
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.GM_AsmView
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_GetPreviousDetails
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_GetUserProfileData
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_User_List
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Product
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.SaveRegId
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.User_Response
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded



/**
 * Created by root on 10/1/17.
 */

interface ApiEndpointInterface {

    // website link  http://console.salelinecrm.com/salesline/include/login.html
    // http://console.salelinecrm.com/saleslineapi/LoginVerification/rrgm@gmail.com/rrgm

    @Headers("Content-Type: application/json")
    @GET("LoginVerification/{UserName}/{Password}")

    fun loginUser(
            @Path("UserName") username: String,
            @Path("Password") password: String
    ): Call<JsonElement>


    // http://console.salelinecrm.com/notification/registerid/Userid/Andriodid


    @POST("registerid/{Userid}")
    fun sendAndroidId(
            @Path("Userid") Userid: String,
            @Body body: RequestBody
    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/Logout/3
    @POST("Logout/{Userid}")
    fun mLogout(
            @Path("Userid") Userid: String

    ): Call<JsonElement>


/*
  @Path("ClientId") ClientId: Int,
  */
    /**
     *  for retrieve the user/employee details
     */
    //http://console.salelinecrm.com/saleslineapi/GetUserProfileData/7
    @Headers("Content-Type: application/json")
    @GET("GetUserProfileData/{Userid}")
    fun getUserProfileData(@Path("Userid") Userid: String): Call<JsonElement>


    /**
     * for retrive the previous job details of employee
     */
    //http://console.salelinecrm.com/saleslineapi/GetPreviousDetails/7
    @Headers("Content-Type: application/json")
    @GET("GetPreviousDetails/{Userid}")
    fun getPreviousDetails(@Path("Userid") Userid: String): Call<List<Manager_GetPreviousDetails>>


    /****
     *    http://console.salelinecrm.com/saleslineapi/GetImage/client id
     *
     *    used to get Image
     */


    @Headers("Content-Type: application/json")
    @GET
    ("GetImage/{ClientId}")
    fun getImage(@Path("ClientId") ClientId: String


    ): Call<JsonElement>
    /**
     *
     *   Update profile data
     */
    // http://console.salelinecrm.com/saleslineapi/ProfileDataModify/
    //@Headers("Content-Type: application/json; charset=utf-8" )
    @POST("ProfileDataModify/{Userid}")
    fun addUpdateProfile(@Path("Userid") Id: Int, @Body body: RequestBody): Call<JsonElement>

    /**
     *  for getting the employees under gm
     * http://console.salelinecrm.com/saleslineapi/GMView/UserId/ClientId
     */
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("GMView/{UserId}/{ClientId}")
    fun getGMEmployee(
            @Path("UserId") UserId: String,
            @Path("ClientId") ClientId: String
    ): Call<List<GM_AsmView>>


    /**
     *    FOr getting te empoyee under the AGM
     * http://console.salelinecrm.com/saleslineapi/GMView/UserId/ClientId
     */

    @Headers("Content-Type: application/json")
    @GET("GMView/{UserId}/{ClientId}")
    fun getAGMEmployee(
            @Path("UserId") UserId: String,
            @Path("ClientId") ClientId: String
    ): Call<List<GM_AsmView>>


    /**
     *    FOr getting te empoyee under the AGM
     *  http://console.salelinecrm.com/saleslineapi/GMView/UserId/ClientId
     */

    @Headers("Content-Type: application/json")
    @GET("GMView/{UserId}/{ClientId}")
    fun getASMEmployee(
            @Path("UserId") UserId: String,
            @Path("ClientId") ClientId: String
    ): Call<List<GM_AsmView>>


    /**
     *    FOr getting te list of the user under the Maanager
     *  http://console.salelinecrm.com/saleslineapi/GettingSalemanUnderMgr/5
     */

    @Headers("Content-Type: application/json")
    @GET("GettingSalemanUnderMgr/{ManagerId}")
    fun getUserUnderManager(
            @Path("ManagerId") ManagerId: String

    ): Call<List<Manager_User_List>>


    /**
     * *   Update profile data
     */

    // http://console.salelinecrm.com/saleslineapi/InsertLeadDetails/
    @Headers("Content-Type: application/json")
    @POST("InsertLeadDetails")
    fun addLead(@Body body: RequestBody): Call<JsonElement>


    /**
     *
     * Update previous detais
     *  http://console.salelinecrm.com/saleslineapi/UpdatePreviousDetails/Id
     *
     *
     *    fun getUserUnderManager(
    @Path("ManagerId") ManagerId: String

    ): Call<List<Manager_User_List>>
     */


    @Headers("Content-Type: application/json")
    @POST
    ("UpdatePreviousDetails/{id}")
    fun updatePreviousDetails(@Path("id") Id: Int, @Body body: RequestBody): Call<User_Response>


    @Headers("Content-Type: application/json")
    @POST
    ("UpdatePreviousDetails/{id}")
    fun updatePreviousDetails2(@Path("id") Id: Int, @Body body: RequestBody): Call<JsonElement>


    @Headers("Content-Type: application/json")
    @POST
    ("InsertPreviousDetails")
    fun InsertPreviousDetails(@Body body: RequestBody): Call<JsonElement>

    /**
     *
     *  Used to get all leads under the users
     *
     *   http://console.salelinecrm.com/saleslineapi/SaveLeadEnquiryDetails
     */

    @Headers("Content-Type: application/json")
    @GET("GMView/{UserId}/{ClientId}")
    fun getSaveLeadsOfEmployee(
            @Path("UserId") UserId: String,
            @Path("ClientId") ClientId: String
    ): Call<User_Response>


    /**
     *
     *  Used to get all leads under the users
     *
     * http://console.salelinecrm.com/saleslineapi/allleads/8/1
     */


    @GET("allleads/{Name}/{ClientId}")
    fun getAllLeadsOfEmployee(
            @Path("Name") Name: String?,
            @Path("ClientId") ClientId: Int
    ): Call<JsonElement>


    /**
     *
     *  Used to update the lead
     * //  http://console.salelinecrm.com/saleslineapi/GetEnquiryDetails/EnquiryId
     *
     */
    @Headers("Content-Type: application/json")
    @GET
    ("GetEnquiryDetails/{EnquiryId}")
    fun mGetEnquiryDetails(@Path("EnquiryId") EnquiryId: String): Call<JsonElement>

    /**
     *
     *  Used to update the lead
     * //http://console.salelinecrm.com/saleslineapi/UpdateLeadDetails/LeadId
     *
     */
    @Headers("Content-Type: application/json")
    @POST
    ("UpdateLeadDetails/{LeadId}")
    fun updateLead(@Path("LeadId") Id: Int, @Body body: RequestBody): Call<JsonElement>


    /**
     *
     *  Used to get the lead details
     * //http://console.salelinecrm.com/saleslineapi/GetLeadPageDetails/LeadId
     *
     */
    @Headers("Content-Type: application/json")
    @GET
    ("GetLeadPageAndroidDetails/{LeadId}")
    fun getLeadDetails(@Path("LeadId") LeadId: String): Call<JsonElement>


    /**
     *
     *  Used to get the all user under the GM
     * //http://console.salelinecrm.com/saleslineapi/GetUserSearchDetails/Name/ClientId
     *
     */
    @Headers("Content-Type: application/json")
    @GET
    ("GetUserSearchDetails/{Name}/{ClientId}")
    fun getAllUserOfGm(@Path("Name") Id: String,
                       @Path("ClientId") ClientId: Int


    ): Call<JsonElement>

    //


    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsJanuary/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsJanuary/{Name}/{ClientId}")
    fun getTotalProductsJanuary(@Path("Name") Id: Int,
                                @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>


    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsFebruary/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsFebruary/{Name}/{ClientId}")
    fun getTotalProductsFebruary(@Path("Name") Id: Int,
                                 @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsMarch/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsMarch/{Name}/{ClientId}")
    fun getTotalProductsMarch(@Path("Name") Id: Int,
                              @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>


    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsApril/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsApril/{Name}/{ClientId}")
    fun getTotalProductsApril(@Path("Name") Id: Int,
                              @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>


    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsApril/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsMay/{Name}/{ClientId}")
    fun getTotalProductsMay(@Path("Name") Id: Int,
                            @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsJune/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsJune/{Name}/{ClientId}")
    fun getTotalProductsJune(@Path("Name") Id: Int,
                             @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsApril/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsJuly/{Name}/{ClientId}")
    fun getTotalProductsJuly(@Path("Name") Id: Int,
                             @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsAugust/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsAugust/{Name}/{ClientId}")
    fun getTotalProductsAugust(@Path("Name") Id: Int,
                               @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsSeptember/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsSeptember/{Name}/{ClientId}")
    fun getTotalProductsSeptember(@Path("Name") Id: Int,
                                  @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsOctober/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsOctober/{Name}/{ClientId}")
    fun getTotalProductsOctober(@Path("Name") Id: Int,
                                @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsApril/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsNovember/{Name}/{ClientId}")
    fun getTotalProductsNovember(@Path("Name") Id: Int,
                                 @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    /**
     *    get graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/TotalProductsDecember/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("TotalProductsDecember/{Name}/{ClientId}")
    fun getTotalProductsDecember(@Path("Name") Id: Int,
                                 @Path("ClientId") ClientId: Int


    ): Call<List<All_Product>>

    //=============================\\
    // http://console.salelinecrm.com/saleslineapi/LeadStageCountJanuary/3/1
    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountJanuary/{Name}/{ClientId}")
    fun getTotalFunnel(@Path("Name") Id: Int,
                       @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>

    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountFebruary/{Name}/{ClientId}")
    fun getTotalFunnelFebruary(@Path("Name") Id: Int,
                               @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountMarch/{Name}/{ClientId}")
    fun getTotalFunnelMarch(@Path("Name") Id: Int,
                            @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountApril/{Name}/{ClientId}")
    fun getTotalFunnelApril(@Path("Name") Id: Int,
                            @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountMay/{Name}/{ClientId}")
    fun getTotalFunnelMay(@Path("Name") Id: Int,
                          @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountJune/{Name}/{ClientId}")
    fun getTotalFunnelJune(@Path("Name") Id: Int,
                           @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>

    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountJuly/{Name}/{ClientId}")
    fun getTotalFunnelJuly(@Path("Name") Id: Int,
                           @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountAugust/{Name}/{ClientId}")
    fun getTotalFunnelAugust(@Path("Name") Id: Int,
                             @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountSeptember/{Name}/{ClientId}")
    fun getTotalFunnelSeptember(@Path("Name") Id: Int,
                                @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountOctober/{Name}/{ClientId}")
    fun getTotalFunnelOctober(@Path("Name") Id: Int,
                              @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountNovember/{Name}/{ClientId}")
    fun getTotalFunnelNovember(@Path("Name") Id: Int,
                               @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    @Headers("Content-Type: application/json")
    @GET
    ("LeadStageCountDecember/{Name}/{ClientId}")
    fun getTotalFunnelDecember(@Path("Name") Id: Int,
                               @Path("ClientId") ClientId: Int


    ): Call<List<All_Product_Funnel>>


    /**
     *    get Month graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/Monthgraphs/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("Monthgraphs/{Name}/{ClientId}")
    fun getMonthgraphs(@Path("Name") Id: Int,
                       @Path("ClientId") ClientId: Int


    ): Call<JsonElement>

    /**
     *    get Month graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/Daygraphs/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("Daygraphs/{Name}/{ClientId}")
    fun getDaygraphs(@Path("Name") Id: Int,
                     @Path("ClientId") ClientId: Int


    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/Totalsalerevgraphs/name/client


    /**
     *    get Month graph from server
     *
     *  //http://console.salelinecrm.com/saleslineapi/Totalsalerevgraphs/name/client
     */


    @Headers("Content-Type: application/json")
    @GET
    ("Totalsalerevgraphs/{Name}/{ClientId}")
    fun getTotalsalerevgraphs(
            @Path("Name") Id: Int,
            @Path("ClientId") ClientId: Int

    ): Call<JsonElement>


    /**
     *
     *  Used to update the lead
     * //h// http://console.salelinecrm.com/saleslineapi/UserId/FileUploader1
     *
     */


    @Headers("Content-Type: application/json")
    @POST
    ("UpdateLeadDetails/{UserId}")
    fun updateProfilePic(@Path("UserId") Id: Int, @Body body: RequestBody): Call<JsonElement>


    /**
     *
     *  Used to get the product of leads
     * //http://console.salelinecrm.com/saleslineapi/BindProducts/clientid
     *
     */

    @Headers("Content-Type: application/json")
    @GET
    ("BindProducts/{ClientId}")
    fun getBindProducts(

            @Path("ClientId") ClientId: String


    ): Call<JsonElement>


    /**
     *
     *  Used to get the sources of leads
     * //http://console.salelinecrm.com/saleslineapi/BindSources/clientid
     *
     */

    @Headers("Content-Type: application/json")
    @GET
    ("BindSources/{ClientId}")
    fun getBindSources(

            @Path("ClientId") ClientId: String


    ): Call<JsonElement>


    /**
     *
     *  Used to get the sources of leads
     * //http://console.salelinecrm.com/saleslineapi/BindStages/clientid
     *
     */

    @Headers("Content-Type: application/json")
    @GET
    ("BindStages/{ClientId}")
    fun getBindStages(

            @Path("ClientId") ClientId: String


    ): Call<JsonElement>


    /**
     *
     *  Used to get the sources of leads
     * //http://console.salelinecrm.com/saleslineapi/BindResponse/clientid
     *
     */

    @Headers("Content-Type: application/json")
    @GET
    ("BindResponse/{ClientId}")
    fun getBindResponse(

            @Path("ClientId") ClientId: String


    ): Call<JsonElement>

    /**
     *
     *  Used to get the upload professional profile
     * //http://console.salelinecrm.com/saleslineapi/UploadUserProfilePic/UserId
     */

    @Headers("Content-Type: application/json")
    @POST
    ("UploadUserProfilePic/{UserId}")
    fun mUploadUserProfilePic(
            @Path("UserId") ClientId: String,
            @Body body: RequestBody
    ): Call<JsonElement>


    /**
     *
     *  Used to savefeedback
     * //http://console.salelinecrm.com/saleslineapi/SaveFeedback
     */


    @Headers("Content-Type: application/json")
    @POST
    ("SaveFeedback")
    fun mSaveFeedback(
            @Body body: RequestBody
    ): Call<JsonElement>

    /**
     *
     *  Used to savefeedback
     * //http://console.salelinecrm.com/saleslineapi/SaveRecommendAppDetails
     */

    @Headers("Content-Type: application/json")
    @POST
    ("SaveRecommendAppDetails")
    fun mSaveRecommendAppDetails(
            @Body body: RequestBody
    ): Call<JsonElement>


    /**
     *
     *  Used to get the Lead Demos
     * //http://console.salelinecrm.com/saleslineapi/GetLeadDemos/EnquiryId
     */

    @Headers("Content-Type: application/json")
    @GET
    ("GetLeadDemos/{EnquiryId}")
    fun mGetLeadDemos(
            @Path("EnquiryId") EnquiryId: String

    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/GetAudioFiles/EnquiryId/ClientId

    @Headers("Content-Type: application/json")
    @GET
    ("GetAudioFiles/{EnquiryId}/{ClientId}")
    fun mGetAudioFiles(
            @Path("EnquiryId") EnquiryId: String, @Path("ClientId") ClientId: String

    ): Call<JsonElement>

    /* Save current location of salesman
    http://console.salelinecrm.com/saleslineapi/SaveGPSLocation
    *
    * */


    /**
     *
     *  Used to savefeedback
     * //http://console.salelinecrm.com/saleslineapi/SaveLeadDemoDetails/DemoId
     */

    @Headers("Content-Type: application/json")
    @POST
    ("SaveLeadDemoDetails/{DemoId}")
    fun mSaveLeadDemoDetails(
            @Path("DemoId") DemoId: String,
            @Body body: RequestBody
    ): Call<JsonElement>

    @FormUrlEncoded
    @POST("save_reg_id.php?")
    fun saveRegId(
            @Field("regid") reg_id: String,
            @Field("loginId") login_id: String,
            @Field("name") name: String

    ): Call<SaveRegId>

    /*
       close the lead
     */
    //http://console.salelinecrm.com/saleslineapi/SaleCloseLeadDemoDetails/demoid

    @Headers("Content-Type: application/json")
    @POST
    ("SaleCloseLeadDemoDetails/{DemoId}")
    fun mSaleCloseLeadDemoDetails(
            @Path("DemoId") DemoId: String,
            @Body body: RequestBody
    ): Call<JsonElement>


    /**
     *
     *  Used to savefeedback
     * //http://console.salelinecrm.com/saleslineapi/UpdateEnquiryDetails/Eid/LeadId
     */

    @Headers("Content-Type: application/json")
    @POST
    ("UpdateEnquiryDetails/{Eid}/{LeadId}")
    fun mUpdateEnquiryDetails(
            @Path("Eid") Eid: String,
            @Path("LeadId") LeadId: String,
            @Body body: RequestBody
    ): Call<JsonElement>


    /**
     *
     *  Used to get all leads under the users
     *
     * http://console.salelinecrm.com/saleslineapi/FollowedUpLeads/8/1
     */


    @GET("FollowedUpLeads/{Name}/{ClientId}")
    fun getFollowedUpLeads(
            @Path("Name") Name: String?,
            @Path("ClientId") ClientId: Int
    ): Call<JsonElement>

    /**
     *
     *  Used to get all leads under the users
     *
     * http://console.salelinecrm.com/saleslineapi/LeadsMissedFollowups/8/1
     */


    @GET("LeadsMissedFollowups/{Name}/{ClientId}")
    fun getLeadsMissedFollowups(
            @Path("Name") Name: String?,
            @Path("ClientId") ClientId: Int
    ): Call<JsonElement>


    /**
     *
     *  Used to get all leads under the users
     *
     * http://console.salelinecrm.com/saleslineapi/LeadsClosed/Status/UserId/ClientId
     */


    @GET("LeadsClosed/{Status}/{UserId}/{ClientId}")
    fun getLeadsClosed(
            @Path("Status") Status: String?,
            @Path("UserId") Name: String?,
            @Path("ClientId") ClientId: Int
    ): Call<JsonElement>


    /**
     *
     *  Used to get all leads under the users
     *
     * hhttp://console.salelinecrm.com/saleslineapi/FollowupsToday/UserId/ClientId
     */


    @GET("FollowupsToday/{UserId}/{ClientId}")
    fun getFollowupsToday(
            @Path("UserId") Name: String?,
            @Path("ClientId") ClientId: Int
    ): Call<JsonElement>


    /**
     *
     *  Used to get all leads under the users
     *
     *    http://console.salelinecrm.com/saleslineapi/CommittedProspectLeads/UserId/ClientId
     */


    @GET("CommittedProspectLeads/{UserId}/{ClientId}")
    fun getCommittedProspectLeads(
            @Path("UserId") Name: String?,
            @Path("ClientId") ClientId: Int
    ): Call<JsonElement>


    //

    /**
     *
     *  Used to   UpdateDetailsLead under the users
     *
     *    //http://console.salelinecrm.com/saleslineapi/UpdateDetailsLead/LeadId
     */


    @POST("UpdateDetailsLead/{LeadId}")
    fun mUpdateDetailsLead(
            @Path("LeadId") LeadId: String?,
            @Body body: RequestBody
    ): Call<JsonElement>


    /**
     *     Used to inactive the user
     *    http://console.salelinecrm.com/saleslineapi/FreezeUser/ClientId/UserId
     *
     */

    @POST("FreezeUser/{ClientId}/{UserId}")
    fun mFreezeUser(
            @Path("ClientId") ClientId: String?,
            @Path("UserId") UserId: String?,
            @Body body: RequestBody

    ): Call<JsonElement>

    /**
     *     Used to inactive the user
     *    http://console.salelinecrm.com/saleslineapi/UpdateEnquiryLead/LeadId
     *
     */

    @POST("UpdateEnquiryLead/{LeadId}")
    fun mUpdateEnquiryLead2(
            @Path("LeadId") LeadId: String?,

            @Body body: RequestBody

    ): Call<JsonElement>


//    @POST("UpdateEnquiryLead/{LeadId}")
//    fun mUpdateEnquiryLead(
//            @Path("LeadId") LeadId: String?,
//
//            @Body body: RequestBody
//
//    ): Call<List<User_Response>>

    //http://console.salelinecrm.com/saleslineapi/GettingSalemanUnderCompany/5/1

    @GET("GettingSalemanUnderCompany/5/{ClientId}")
    fun mGettingSalemanUnderCompany(
            @Path("ClientId") ClientId: String?
    ): Call<JsonElement>


    // http://console.salelinecrm.com/saleslineapi/LeaderBoard/Name/ClientId

    @GET("LeaderBoard/{Name}/{ClientId}")
    fun mLeaderBoard(
            @Path("Name") Name: String,
            @Path("ClientId") ClientId: String
    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/SaveSaleClosedDeatails

    @POST("InsertSaleClosedDeatails")
    fun mSaveSaleClosedDeatails(
            @Body body: RequestBody
    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/UpdateEnqStatusClose/Eid

    @POST("UpdateEnqStatusClose/{Eid}")
    fun mUpdateEnqStatusClose(
            @Path("Eid") Name: String,
            @Body body: RequestBody
    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/BindEnquiryNum/Eid
    @POST("BindEnquiryNum/{Eid}")
    fun mBindEnquiryNum(
            @Path("Eid") Name: String,
            @Body body: RequestBody
    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/FilterSearch/FromDate/ToDate/LeadStage/DemoDate/Prodcut/Type/SaleLine/UserId/ClientId/SalemanId
//http://console.salelinecrm.com/saleslineapi/FilterSearch/08-08-2017/08-30-2017/SaleLine/01-01-0001/SaleLine/S/SaleLine/3/1
    @GET("FilterSearch/{FromDate}/{ToDate}/{LeadStage}/{DemoDate}/{Prodcut}/{Type}/{LeadSource}/{UserId}/{ClientId}/{SalemanId}")
    fun getFilterSearchLeads(
            @Path("FromDate") FromDate: String,
            @Path("ToDate") ToDate: String,
            @Path("LeadStage") LeadStage: String,
            @Path("DemoDate") DemoDate: String,
            @Path("Prodcut") Prodcut: String,
            @Path("Type") Type: String,
            @Path("LeadSource") SaleLine: String,
            @Path("UserId") UserId: Int,
            @Path("ClientId") ClientId: Int,
            @Path("SalemanId") SalemanId: String

    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/SaveRateApp

    @POST("SaveRateApp")
    fun mSaveRateApp(
            @Body body: RequestBody
    ): Call<JsonElement>

    //http://console.salelinecrm.com/saleslineapi/GetStates
    @GET("GetStates")
    fun mGetStates(): Call<JsonElement>


    // http://console.salelinecrm.com/saleslineapi/BindProducts/ClientId


    @GET("BindProducts/{ClientId}")
    fun mGetSaleManTargets(
            @Path("ClientId") ClientId: String

    ): Call<List<Product>>


    //http://console.salelinecrm.com/saleslineapi/SaveSaleManTargets

    @POST("SaveSaleManTargets")
    fun mSaveSaleManTargets(
            @Body body: RequestBody
    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/UpdateSaleManTargets/TargetId
    @POST("UpdateSaleManTargets/{TargetId}")
    fun mUpdateSaleManTargets(
            @Path("TargetId") TargetId: String,
            @Body body: RequestBody
    ): Call<JsonElement>


    /*
    //http://console.salelinecrm.com/saleslineapi/SearchLeads/nitesh/10/1

    //http://console.salelinecrm.com/saleslineapi/SearchLeads/LeadName/UserId/ClientId
*/
    @GET("SearchLeads/{LeadName}/{UserId}/{ClientId}")
    fun mUpdateEnqStatusClose(
            @Path("LeadName") LeadName: String,
            @Path("UserId") UserId: String,
            @Path("ClientId") ClientId: String

    ): Call<JsonElement>


    /*
    //http://console.salelinecrm.com/saleslineapi/CheckduplicatePhone/Phone/ClientId/UserId
    */

    @GET("CheckDuplicatePhone/{Phone}/{ClientId}/{UserId}")
    fun mCheckDuplicatePhone(
            @Path("Phone") Phone: String,
            @Path("ClientId") ClientId: String,
            @Path("UserId") UserId: String

    ): Call<JsonElement>

    //http://console.salelinecrm.com/saleslineapi/CheckAddUpdatelead/{phone}/{ClientId}/{UserId}

    @GET("CheckAddUpdatelead/{Phone}/{ClientId}/{UserId}")
    fun mCheckAddUpdateLead(
            @Path("Phone") Phone: String,
            @Path("ClientId") ClientId: String,
            @Path("UserId") UserId: String

    ): Call<JsonElement>
/*
 http://console.salelinecrm.com/saleslineapi/GetLeadEnquiryDetails/Phone/ClientId

 */

    @GET("GetLeadEnquiryDetails/{Phone}/{EmployeeCode}/{ClientId}")
    fun mGetLeadEnquiryDetails(
            @Path("Phone") Phone: String,
            @Path("EmployeeCode") EmployeeCode: String,
            @Path("ClientId") ClientId: String

    ): Call<JsonElement>

    @GET("GetLeadCallEnquiryDetails/{Phone}/{EmployeeCode}/{ClientId}")
    fun GetLeadCallEnquiryDetails(
            @Path("Phone") Phone: String,
            @Path("EmployeeCode") EmployeeCode: String,
            @Path("ClientId") ClientId: String

    ): Call<JsonElement>

    /*
 http://console.salelinecrm.com/saleslineapi/ValidateLeadPhoneNumber/Phone/EmployeeCode/ClientId

 */

    @GET("ValidateLeadPhoneNumber/{Phone}/{EmployeeCode}/{ClientId}")
    fun mValidateLeadPhoneNumber(
            @Path("Phone") Phone: String,
            @Path("EmployeeCode") EmployeeCode: String,
            @Path("ClientId") ClientId: String

    ): Call<JsonElement>
    /*
    get GetEnquiryNum for lead
   */

    //http://console.salelinecrm.com/saleslineapi/GetEnquiryNum/ClientId

    @GET("GetEnquiryNum/{ClientId}")
    fun mGetEnquiryNum(
            @Path("ClientId") ClientId: String
    ): Call<JsonElement>


    //  http://console.salelinecrm.com/saleslineapi/SaveLeadEnquiryDetails
    @Headers("Content-Type: application/json")
    @POST("SaveLeadEnquiryDetails")
    fun mSaveLeadEnquiryDetails(@Body body: RequestBody): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/GetDemoStageProduct/LeadId/DemoId

    @GET("GetDemoStageProduct/{LeadId}/{DemoId}")
    fun mGetDemoStageProduct(
            @Path("LeadId") LeadId: String,
            @Path("DemoId") DemoId: String
    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/TotalSales/Name/Client/FromDate/ToDate

    @GET("TotalSales/{Name}/{Client}/{FromDate}/{ToDate}")
    fun mTotalSales(
            @Path("Name") Name: String,
            @Path("Client") Client: String,
            @Path("FromDate") FromDate: String,
            @Path("ToDate") ToDate: String
    ): Call<JsonElement>


    /*
    get GetSaleManTargets for User
   */

    //http://console.salelinecrm.com/saleslineapi/GetSaleManTargets/UserId

    @GET("GetSaleManTargets/{UserId}")
    fun mGetTotalSaleManTargets(
            @Path("UserId") UserId: String
    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/Getuserid/Emailid/id

    @GET("Getuserid/{Emailid}/0")
    fun mGetuserid(
            @Path("Emailid") Emailid: String
    ): Call<JsonElement>


    /*

 //http://console.salelinecrm.com/saleslineapi/GetDemoId/EnquiryId
    */

    @GET("GetDemoId/{EnquiryId}")
    fun mGetDemoId(

            @Path("EnquiryId") EnquiryId: String

    ): Call<JsonElement>


    //http://console.salelinecrm.com/saleslineapi/GettingSalemanUnderUserRole/172/4
//http://console.salelinecrm.com/saleslineapi/GettingSalemanUnderUserRole/UserId/RoleId
    @GET("GettingSalemanUnderUserRole/{UserId}/{RoleId}")
    fun mGettingSalemanUnderUserRole(
            @Path("UserId") UserId: String,
            @Path("RoleId") RoleId: String

    ): Call<JsonElement>

}

//http://console.salelinecrm.com/saleslineapi/GetLeadEnquiryDetails/Phone/EmployeeCode/ClientId