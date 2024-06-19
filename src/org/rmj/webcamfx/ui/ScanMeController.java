package org.rmj.webcamfx.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.activation.MimetypesFileTypeMap;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.webcamfx.lib.Utils;

public class ScanMeController implements Initializable {
    @FXML
    private AnchorPane acButtons;
    @FXML
    private Button cmdOkay;
    @FXML
    private Button cmdCancel;
    @FXML
    private ImageView gimage;
    @FXML
    private TextField txtPIN;
    @FXML
    private Label lblTitle;
    
    private String psFormTitle = "";
    private String psFileName = "";
    private String psPINCode = "";
    @FXML
    private Button cmdExit;
    
    public void setFormTitle(String fsValue){ psFormTitle = fsValue;}
    public void setFileName(String fsValue){ psFileName = fsValue; }
    public void setPIN(String fsValue){ psPINCode = fsValue; }
    public String getPIN(){ return psPINCode; }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblTitle.setText(psFormTitle);
        
        try {           
            File f = new File(psFileName);
            
            if(f.exists() && !f.isDirectory()) { 
                String mimetype= new MimetypesFileTypeMap().getContentType(f);
                Image image = new Image("file:" + psFileName);

                //load image
                updateImageView(gimage, image);
                
                if (psPINCode.isEmpty() || psPINCode.equals(""))
                    txtPIN.setText("");
                else {
                    txtPIN.setText(psPINCode);
                    txtPIN.setDisable(true);
                    cmdCancel.setDisable(true);
                }
            } else {
                System.err.println("Please verify the file directory given.");
                ShowMessageFX.Information(getStage(),"Please verify the file directory given.", "Guanzon Scan Me", "Unable to load image.");
                unloadScene();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(ScanMeController.class.getName()).log(Level.SEVERE, null, e);
            ShowMessageFX.Error(getStage(),e.getMessage(), "Guanzon Scan Me", "Exception");
            unloadScene();
        }
    }    

    @FXML
    private void cmdOkay_Click(ActionEvent event) {       
        psPINCode = txtPIN.getText();
        
        if (psPINCode.isEmpty()) {
            ShowMessageFX.Information(getStage(), "OTP must not be empty.", "Guanzon Scan Me", "Invalid OTP");
            return;
        }
        
        unloadScene();
    }

    @FXML
    private void cmdCancel_Click(ActionEvent event) {
        psPINCode = "";
        unloadScene();
    }
    
    private void unloadScene(){
        Stage stage = (Stage) acButtons.getScene().getWindow();
        stage.close();
    }
    
    private Stage getStage(){
        return (Stage) acButtons.getScene().getWindow();
    }
    
    private void updateImageView(ImageView view, Image image){
        Utils.onFXThread(view.imageProperty(), image);
    }
}
