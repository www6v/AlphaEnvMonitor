package com.gw.monitor.alphaenvmonitor.domain;

import java.util.ArrayList;
import java.util.List;

import com.gw.monitor.alphaenvmonitor.test.Address;

import net.sf.json.JSONObject;

public class InstanceMonitorStatusTest {
	public static void main(String args[]) {
//        Address address=new Address("P.O BOX 54534", "Seattle, WA", 42452,"561-832-3180", "531-133-9098");  
//        //objectתJSONObject  
//        JSONObject jsObj = JSONObject.fromObject(address);  
//        System.out.println(jsObj.toString(4));  

        //////////////////////
        //
        ClientStatus cs = new ClientStatus();
        cs.setClienttotal(8419);
        List<ClientStatus> csList = new ArrayList<ClientStatus>();
        csList.add(cs);

        ServerStatus ss = new ServerStatus();
        ss.setServer("ip:port =  10.15.88.73:3306");
        ss.setConn("ok");
        ss.setSpeed(136712);
        ss.setSlow(0);
        List<ServerStatus>  ssList = new ArrayList<ServerStatus>();       
        ssList.add(ss);
        
        MonitorStatus ms = new MonitorStatus();
//        ms.setClients(csList);
//        ms.setServers(ssList);

        ms.setClient(csList);
        ms.setServer(ssList); 
        //
        InstanceMonitorStatus ims = new InstanceMonitorStatus();
        ims.setIp("192.168.0.9");
        ims.setPort("81");
        ims.setType(1);
        ims.setBuildtime("2010-08-04 11:41:07");
        ims.setInfo(ms);

        //objectתJSONObject 
        JSONObject imsJsObj = JSONObject.fromObject(ims);    
        System.out.println(imsJsObj.toString(4));     
	}
}
