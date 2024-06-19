package org.rmj.webcamfx.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class TDS extends Application {
    private static String sPin = "";
    
    public void setValue(String fsValue){
        sPin = fsValue;
    }
    
    public static void main(String [] args){
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {           
        if (sPin.equals("")){
            System.err.println("UNSET parameter...");
            System.exit(1);
        }
        
        if (sPin.equals("")){
            System.out.println("PAYLOAD is emtpy...");
            System.exit(1);
        }
        
        String [] lasPin = sPin.split("»");
        
        
        sPin = Webcam.showQR("G-Card Points Update", sPin, "OTP: " + lasPin[7]);
        System.exit(0);
    }
}
