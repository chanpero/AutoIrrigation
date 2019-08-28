package com.example.autoirrigation.Tools;

import java.util.ArrayList;

public class DBUtil {
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> brrayList = new ArrayList<>();
    private ArrayList<String> crrayList = new ArrayList<>();

    private HttpConnSoap Soap = new HttpConnSoap();

    /**
     * Check User name and password
     */
    public String CheckUser(String userid, String userpwd) {
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("userid");
        arrayList.add("password");
        brrayList.add(userid);
        brrayList.add(userpwd);

        crrayList = Soap.GetWebServer("CheckUser", arrayList, brrayList);
        if (crrayList.size() > 0)
            return crrayList.get(0);
        else
            return "0";
    }

    /**
     * Insert A Task into database
     */
    public boolean insertTask(String deviceCode, String operation, String time, String circ, String code) {
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("deviceCode");
        arrayList.add("operation");
        arrayList.add("time");
        arrayList.add("circ");
        arrayList.add("code");
        brrayList.add(deviceCode);
        brrayList.add(operation);
        brrayList.add(time);
        brrayList.add(circ);
        brrayList.add(code);

        crrayList = Soap.GetWebServer("InsertTask", arrayList, brrayList);
        return crrayList.get(0).equals("true");
    }

    /**
     * Delete A Task from database
     */
    public boolean deleteTask(String deviceCode, String operation, String time, String circ, String code) {
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("deviceCode");
        arrayList.add("operation");
        arrayList.add("time");
        arrayList.add("circ");
        arrayList.add("code");
        brrayList.add(deviceCode);
        brrayList.add(operation);
        brrayList.add(time);
        brrayList.add(circ);
        brrayList.add(code);

        crrayList = Soap.GetWebServer("DeleteTask", arrayList, brrayList);
        return crrayList.get(0).equals("true");
    }

    /**
     * Query task from database
     */
    public ArrayList<String> queryTask(String code) {
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("code");
        brrayList.add(code);

        crrayList = Soap.GetWebServer("QueryTask", arrayList, brrayList);
        return crrayList;
    }

    public boolean switchTask(String deviceCode, String operation, String time, String circ, String isOn, String code){
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("deviceCode");
        arrayList.add("operation");
        arrayList.add("time");
        arrayList.add("circ");
        arrayList.add("isOn");
        arrayList.add("code");
        brrayList.add(deviceCode);
        brrayList.add(operation);
        brrayList.add(time);
        brrayList.add(circ);
        brrayList.add(isOn);
        brrayList.add(code);

        crrayList = Soap.GetWebServer("SwitchTask", arrayList, brrayList);
        return crrayList.get(0).equals("true");
    }
}
