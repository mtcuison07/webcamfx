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

public class ScanQR extends Application {
    public static String psFormTitle = "";
    public static String psDirectory = "";
    public static String psFileName = "";
    
    private double xOffset = 0; 
    private double yOffset = 0;
    
    public void setFormTitle(String fsValue){
        psFormTitle = fsValue;
    }
    
    public void setDirectory(String fsValue){
        psDirectory = fsValue;
    }
    
    public void setFileName(String fsValue){
        psFileName = fsValue;
    }
    
    @Override
    public void start(Stage stage) throws Exception {          
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ScanQR.fxml"));
        
        ScanQRController controller = new ScanQRController();        
        
        if (!(psFileName == null && psFileName.isEmpty())){
            controller.setFormTitle(psFormTitle);
            controller.setFileName(psFileName);
            controller.setDirectory(psDirectory);
            
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
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(psFormTitle);
            stage.getIcons().add(new Image("org/rmj/webcamfx/ui/64.png"));
            stage.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}