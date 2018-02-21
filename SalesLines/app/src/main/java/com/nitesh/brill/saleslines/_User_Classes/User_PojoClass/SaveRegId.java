package com.nitesh.brill.saleslines._User_Classes.User_PojoClass;

import java.io.Serializable;

/**
 * Created by Paras-Android on 19-01-2018.
 */

public class SaveRegId {
    String reg_id;
    String name;
    String loginId;

    public SaveRegId(String reg_id, String name,String loginId)
    {
        this.reg_id = reg_id;
        this.name = name;
        this.loginId = loginId;
    }

    public String getLoginId(){return loginId;}
    public void setLoginId(String loginId){this.loginId = loginId;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getReg_id(){return reg_id;}
    public void setReg_id(String reg_id){this.reg_id = reg_id;}



}
