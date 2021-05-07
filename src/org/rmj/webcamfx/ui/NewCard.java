package org.rmj.webcamfx.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.rmj.appdriver.agentfx.CommonUtils;

public class NewCard extends Application {
    private static String sPin = "";
    
    public void setValue(String fsValue){
        sPin = fsValue;
    }
    
    public static void main(String [] args){
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {    
        /*Application.Parameters parm = getParameters();        
        Object [] args = parm.getUnnamed().toArray();
        if (args.length == 0){
            System.err.println("UNSET parameter...");
            System.exit(1);
        } else if (args.length > 1){
            System.err.println("Too many parameters...");
            System.exit(1);
        }*/
        
        //String sPin = (String) args[0];
        //String sPin = "4150504C49434154494F4EBBBBBBBB30373731393030303335333034BBBBBBBBBBBB";
        
        if (sPin.equals("")){
            System.err.println("UNSET parameter...");
            System.exit(1);
        }
        
        //convert hex to str
        sPin = CommonUtils.Win2UTF(sPin);
        
        if (sPin.equals("")){
            System.out.println("CARD NUMBER is emtpy...");
            System.exit(1);
        }
        
        sPin = Webcam.showQR(sPin, "new");
        System.exit(0);
    }
}
