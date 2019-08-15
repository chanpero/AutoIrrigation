package com.example.autoirrigation;

import java.util.ArrayList;

public class DBUtil {
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> brrayList = new ArrayList<>();
    private ArrayList<String> crrayList = new ArrayList<>();

    private HttpConnSoap Soap = new HttpConnSoap();

    /**
     * Check User name and password
     */
    public String CheckUser(String userid, String userpwd){
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("userid");
        arrayList.add("password");
        brrayList.add(userid);
        brrayList.add(userpwd);

        crrayList = Soap.GetWebServer("CheckUser", arrayList, brrayList);
        if(crrayList.size() > 0)
            return crrayList.get(0);
        else
            return "0";
    }
}
