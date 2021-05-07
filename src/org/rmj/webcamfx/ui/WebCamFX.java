package org.rmj.webcamfx.ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONObject;

/**
 *
 * @author kalyptus
 */
public class WebCamFX extends Application {
    public static int pnCamType = 1;
    public static String psFileName = "";
    private static JSONObject poJSON = new JSONObject();
    
    private double xOffset = 0; 
    private double yOffset = 0;
    
    public void setCamType(int fnValue){
        pnCamType = fnValue;
    }
    
    public void setFileName(String fsValue){
        psFileName = fsValue;
    }
    
    public String getQRValue(){
        return psFileName;
    }
    
    public JSONObject getQRBarcode(){
        return poJSON;
    }
    
    @Override
    public void start(Stage stage) throws Exception {          
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("WebCam.fxml"));
        
        WebCamController controller = new WebCamController();
        controller.setCamType(pnCamType);
        
        if (!psFileName.equals("") && 
            pnCamType == CameraType.Webcam) 
            controller.setFileName(psFileName);
        
        fxmlLoader.setController(controller);
        Parent parent = fxmlLoader.load();
        
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("org/rmj/webcamfx/ui/64.png"));
        stage.setTitle(pnCamType == 0 ? "Guanzon Camera v1.0" : "Guanzon QR Reader v1.0");
        stage.setOnHiding( event -> {controller.shutdown();} );
        stage.showAndWait();
        
        if (pnCamType != CameraType.QRBarcode)
            psFileName = controller.getQRValue();
        else
            poJSON = controller.getQRBarcode();
    }

    public static void main(String[] args) {
        launch(args);
    }
}