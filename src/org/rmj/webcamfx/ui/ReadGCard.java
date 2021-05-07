/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.webcamfx.ui;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.rmj.appdriver.MySQLAESCrypt;

/**
 *
 * @author micha
 */
public class ReadGCard extends Application{
    public static String psGCardInfo;
    
    public String getGCardInfo(){
        return psGCardInfo;
    }
    
    @Override
    public void start(Stage stage) throws Exception {        
        psGCardInfo = Webcam.getQRValue();
    }
    
    public static void main(String [] args){
        launch(args);
    }
}
