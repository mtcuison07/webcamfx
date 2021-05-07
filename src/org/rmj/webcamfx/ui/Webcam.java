package org.rmj.webcamfx.ui;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONObject;
import org.rmj.appdriver.MySQLAESCrypt;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.webcamfx.lib.QRCode;

public class Webcam {
    private static final String pxeModuleName = "Webcam";
    private static final String pxeDefaultDIR = "D:\\GGC_Java_Systems\\temp\\webcam\\";
    private static final String pxeDefaultExt = ".png";
    
    /**
     * READS QR IMAGES USING WEBCAM AND RETURN ITS VALUE
     * 
     * @return String value of the image
     */
    public static String getQRValue(){
        WebCamFX instance = new WebCamFX();
        
        instance.setCamType(CameraType.QRReader);
        showModal(instance, pxeModuleName);
        
        return instance.getQRValue();
    }
    
    public static JSONObject getQRBarcode(){
        WebCamFX instance = new WebCamFX();
        
        instance.setCamType(CameraType.QRBarcode);
        showModal(instance, pxeModuleName);
        
        return instance.getQRBarcode();
    }
    
    /**
     * CAPTURES AN IMAGE USING WEBCAM
     * 
     * @param fsFileName the filename when the image was saved \n
     * 
     * Sample usage: \n\t
     * captureImage("captured1");
     * 
     * @return Full path of the image
     */
    public static String captureImage(String fsFileName){
        WebCamFX instance = new WebCamFX();
        
        instance.setCamType(CameraType.Webcam);
        instance.setFileName(fsFileName);
        
        showModal(instance, pxeModuleName);
        
        return instance.getQRValue();
    }
    
    /**
     * GETS PIN FROM QR CODE
     * 
     * @param fsQRValue the value of the QR Image to be displayed \n
     * 
     * Sample usage: \n\t
     * getPIN("GwapoYungGumawaNito");
     * 
     * @return the pin that the user entered
     */
    public static String getPIN(String fsQRValue){
        QRCode oQRCode = new QRCode();
        
        if (oQRCode.create(fsQRValue, "tmpQRPIN")){
            String lsFileName = pxeDefaultDIR + "tmpQRPIN" + pxeDefaultExt;
            
            ScanMe instance = new ScanMe();
            instance.setFormTitle("GUANZON QR TO OTP");
            instance.setFileName(lsFileName);
            instance.setPIN("");
            
            showModal(instance, pxeModuleName);

            //delete the file
            File f = new File(lsFileName);
            f.delete();
            
            return instance.getPIN();
        }
        
        return "";
    }
    
    public static String showQR(String fsQRValue, String fsValue){
        QRCode oQRCode = new QRCode();
        
        fsQRValue = MySQLAESCrypt.Encrypt(fsQRValue, "20190625");
        if (oQRCode.create(fsQRValue, "tmpQRPIN")){
            String lsFileName = pxeDefaultDIR + "tmpQRPIN" + pxeDefaultExt;
            
            ScanMe instance = new ScanMe();
        
            instance.setFormTitle("NEW G-CARD CREATED");
            instance.setFileName(lsFileName);
            instance.setPIN(fsValue);
            showModal(instance, pxeModuleName);
            
            //delete the file
            File f = new File(lsFileName);
            f.delete();
            
            return instance.getPIN();
        }
        
        return "";
    }
    
    public static void displayNewGCard(String fsPin){
        NewCard instance = new NewCard();
        instance.setValue(fsPin);
        
        Application.launch(instance.getClass());
    }
    
    private static void showModal(Application foObj, String fsModuleName){
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
            foObj.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Webcam.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }        
    }
}
