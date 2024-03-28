package org.rmj.webcamfx.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {
    private static String psFormTitle = "";
    private static String psDirectory = "";
    private static String psQRValuexx = "";
    
    public void setFormTitle(String fsValue){
        psFormTitle = fsValue;
    }
    
    public void setDirectory(String fsValue){
        psDirectory = fsValue;
    }
    
    public void setQRValue(String fsValue){
        psQRValuexx = fsValue;
    }
    
    public static void main(String [] args){
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {    
        Webcam.showClientQR(psFormTitle, psDirectory, psQRValuexx);
        System.exit(0);
    }
}
