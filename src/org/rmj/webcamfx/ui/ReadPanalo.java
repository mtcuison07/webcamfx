/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.webcamfx.ui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.WebClient;

public class ReadPanalo extends Application{
    public static String psGCardInfo;
    public static GRider poGRider;
    
    public String getGCardInfo(){
        return psGCardInfo;
    }
    
    @Override
    public void start(Stage stage) throws Exception {            
        String lsAPI = "gconnect/create_raffle.php";
        
        String lsSQL = "SELECT sValuexxx" + 
                        " FROM xxxOtherConfig" + 
                        " WHERE sConfigID = 'WebSvr'" + 
                            " AND sProdctID = 'gRider'";
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        if (!loRS.next()) System.exit(1);
        
        lsAPI = loRS.getString("sValuexxx") + lsAPI;
        
        psGCardInfo = Webcam.getQRValue();

        if (psGCardInfo.isEmpty()) System.exit(1);
        
        Map<String, String> headers = getAPIHeader();

        JSONObject param = new JSONObject();
        param.put("payload", psGCardInfo);

        String response = WebClient.sendHTTP(lsAPI, param.toJSONString(), (HashMap<String, String>) headers);

        JSONParser parser = new JSONParser();  
        JSONObject json = (JSONObject) parser.parse(response);  
        
        if(json == null){
            System.out.println("No Response"); 
            System.exit(1);
        }else{
            if(json.get("result").equals("error")){
                System.out.println(json.toJSONString());
                System.exit(1);
            } else{
                System.out.println(response);
                System.exit(0);
            }
        } 
    }
    
    public static void main(String [] args){
        String path;
        
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Java_Systems";
        }
        else{
            path = "/srv/GGC_Java_Systems";
        }
        
        System.setProperty("sys.default.path.config", path);
        
        poGRider = new GRider(args[0]);
        
        if (!poGRider.loadEnv(args[0])) System.exit(1);
        if (!poGRider.loadUser(args[0], args[1])) System.exit(1);
        
        launch(args);
    }
    
    public static HashMap getAPIHeader() {
        try {
            String clientid = "GGC_BM001";
            String productid = "IntegSys";
            String imei = InetAddress.getLocalHost().getHostName();
            String user = "M001111122";
            String log = "";
            System.out.println(imei);
            Calendar calendar = Calendar.getInstance();
            Map<String, String> headers =
                    new HashMap<String, String>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("g-api-id", productid);
            headers.put("g-api-imei", imei);
            
            headers.put("g-api-key", SQLUtil.dateFormat(calendar.getTime(), "yyyyMMddHHmmss"));
            headers.put("g-api-hash", org.apache.commons.codec.digest.DigestUtils.md5Hex((String)headers.get("g-api-imei") + (String)headers.get("g-api-key")));
            headers.put("g-api-client", clientid);
            headers.put("g-api-user", user);
            headers.put("g-api-log", log);
            headers.put("g-char-request", "UTF-8");
            headers.put("g-api-token", "");
            
            return (HashMap) headers;
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
