/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.webcamfx.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

/**
 *
 * @author micha
 */
public class ReadGCardBarcode extends Application{
    public static JSONObject poJSON;
    
    public JSONObject getGCardInfo(){
        return poJSON;
    }
    
    @Override
    public void start(Stage stage) throws Exception {        
        poJSON = Webcam.getQRBarcode();
    }
    
    public static void main(String [] args){
        launch(args);
    }
}
