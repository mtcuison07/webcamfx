/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.webcamfx.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;

/**
 *
 * @author micha
 */
public class ScanPin extends Application{    
    private static GRider oApp;
    private static String sData;

    
    public void setData(String fsValue){
        sData = fsValue;
    }
    
    public String getData(){
        return sData;
    }
    
    @Override
    public void start(Stage stage) throws Exception {        
        sData = Webcam.getPIN(sData);
    }
    
    public static void main(String [] args){
        launch(args);
    }
}
