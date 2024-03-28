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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.webcamfx.lib.QRCode;
import org.rmj.webcamfx.lib.Utils;

public class ScanQRController implements Initializable {
    @FXML
    private AnchorPane acButtons;
    @FXML
    private Button cmdOkay;
    @FXML
    private Button cmdCancel;
    @FXML
    private ImageView gimage;
    @FXML
    private Label lblTitle;
    @FXML
    private Button cmdExit;
    
    private String psFormTitle = "";
    private String psDirectory = "";
    private String psFileName = "";
    
    public void setFormTitle(String fsValue){ psFormTitle = fsValue;}
    public void setDirectory(String fsValue){ psDirectory = fsValue;}
    public void setFileName(String fsValue){ psFileName = fsValue; }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblTitle.setText(psFormTitle);
        
        try {           
            File f = new File(psDirectory + "client.png");           
            if(f.exists() && !f.isDirectory()) f.delete();
                        
            QRCode.create(psFileName, "client");

            //load image
            Image image = new Image("file:" + psDirectory + "client.png");
            updateImageView(gimage, image);
            
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(ScanQRController.class.getName()).log(Level.SEVERE, null, e);
            ShowMessageFX.Error(e.getMessage(), "Guanzon Scan Me", "Exception");
            unloadScene();
        }
    }    

    @FXML
    private void cmdOkay_Click(ActionEvent event) {       
        unloadScene();
    }

    @FXML
    private void cmdCancel_Click(ActionEvent event) {
        unloadScene();
    }
    
    private void unloadScene(){
        Stage stage = (Stage) acButtons.getScene().getWindow();
        stage.close();
    }
    
    private void updateImageView(ImageView view, Image image){
        Utils.onFXThread(view.imageProperty(), image);
    }
}
