package org.rmj.webcamfx.ui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.json.simple.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.rmj.appdriver.agent.MsgBox;

import org.rmj.webcamfx.lib.Utils;

public class WebCamController{
    @FXML
    private ImageView gimage;
    @FXML
    private Button cmbSave;
    @FXML
    private Button cmbStartCamera;
    @FXML
    private Button cmbCapture;
    @FXML
    private AnchorPane acButtons;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that realizes the video capture
    private VideoCapture capture;   
    // Captured image
    private Image imageToShow;
    private BufferedImage imageToSave;
    
    private int pnCamType = 0;
    private String psFileName = "";    
    private JSONObject poJSON;
    
    private final String pxeDfaultDIR = "D:/GGC_Java_Systems/temp/";
    private final String pxeDfltFName = "fxclt.jpg";
    
    PauseTransition delay;
    @FXML
    private Label lblTitle;
    @FXML
    private Button cmdExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    
    /**
     * Sets Filename for the captured image.
     * 
     * @param fsValue the filename for the captured image.
     * 
     * Sample: image01.jpg
     */
    public void setFileName(String fsValue){
        psFileName = fsValue;
    }
    
    
    /**
     * Get the Value of the Scanned QR Code
     * 
     * @return String Value
     */
    public String getQRValue(){
        return psFileName;
    }
    
    /**
     * Get the Value of the Scanned QR Code + Barcode
     * 
     * @return JSONObject
     */
    public JSONObject getQRBarcode(){
        return poJSON;
    }
    
    
    /**
     * Sets the Camera Type
     * 
     * @param fnValue 0-Webcam; 1-QR Scanner; 
     */
    public void setCamType(int fnValue){
        pnCamType = fnValue;
    }
   
    @FXML
    private void StartCamera(ActionEvent event) {
        cmbSave.setDisable(true);
        cmbStartCamera.setDisable(true);
        cmbCapture.setDisable(false);
        
        startAcquisition();
    }

    static {
        try {
            int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
            if(bitness == 32){
                System.out.println("32 bit detected");
                System.out.println(Core.NATIVE_LIBRARY_NAME);
                System.load("D:/GGC_Java_Systems/lib/opencv/x86/opencv_java341.dll");
            }
            else if (bitness == 64){
                System.out.println("64 bit detected");
                System.load("D:/GGC_Java_Systems/lib/opencv/x64/opencv_java341.dll");
            }
        } catch (UnsatisfiedLinkError e) {
           System.err.println("Native code library failed to load.\n" + e);
           System.exit(1);
        }      
    }       
   
    @FXML
    private void CapturePicture(ActionEvent event) {
        cmbSave.setDisable(false);
        cmbStartCamera.setDisable(false);
        cmbCapture.setDisable(true);

        this.stopAcquisition();
    }

    @FXML
    private void SavePicture(ActionEvent event) {
        psFileName = pxeDfaultDIR + (psFileName.equals("") ? pxeDfltFName : psFileName);
        
        File outputfile = new File(psFileName);
        
        try {        
            ImageIO.write(imageToSave, "jpg", outputfile);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        System.out.println("Save Picture");
        unloadScene();
    }

    public void initialize(){        
        lblTitle.setText("Guanzon Camera v1.0");
        if (pnCamType == CameraType.QRBarcode || 
            pnCamType == CameraType.QRReader){
            
            lblTitle.setText("Guanzon QR Reader v1.0");
            acButtons.setMaxHeight(0);
            acButtons.setMinSize(0,0);
            
            //will close the stage when the PauseTransition was started
            delay = new PauseTransition(javafx.util.Duration.millis(5));
            delay.setOnFinished( event -> unloadScene());
        }            
        
        
        startAcquisition();
    }
   
    private Mat grabFrame(){
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()){
            try{
                // read the current frame
                this.capture.read(frame);

                //if the frame is not empty, process it
                //if (!frame.empty()){
                //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                //}
            } catch (Exception e){
                //log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }   
   
    private void updateImageView(ImageView view, Image image){
        Utils.onFXThread(view.imageProperty(), image);
    }

    private void startAcquisition(){   
        if (pnCamType == CameraType.QRBarcode)
            poJSON = new JSONObject();
        
        capture = new VideoCapture();
        this.capture.open(0);

        // is the video stream available?
        if (this.capture.isOpened()){
            // grab a frame every 33 ms (30 frames/sec)
            Runnable frameGrabber = new Runnable() {
                @Override
                public void run(){
                    //System.out.println("running"); 
                    // effectively grab and process a single frame
                    Mat frame = grabFrame();
                    // convert and show the frame
                    imageToShow = Utils.mat2Image(frame);
                    imageToSave = Utils.matToBufferedImage(frame);

                    LuminanceSource source = new BufferedImageLuminanceSource(imageToSave);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    if (pnCamType == CameraType.QRReader ||
                        pnCamType == CameraType.QRBarcode){
                        
                        try {
                            Result result = new MultiFormatReader().decode(bitmap);
                            
                            if (pnCamType == CameraType.QRBarcode){
                                if (result.getBarcodeFormat() == BarcodeFormat.QR_CODE)
                                    poJSON.put("QR_CODE", result.getText());
                                else if (result.getBarcodeFormat() == BarcodeFormat.CODE_93){
                                    poJSON.put("CODE_93", result.getText());
                                }
                                
                                if (poJSON.containsKey("QR_CODE") && 
                                    poJSON.containsKey("CODE_93")){
                                
                                    shutdown();

                                    //this will unload the stage
                                    delay.play();
                                }
                                
                            } else{
                                psFileName = result.getText();
                            
                                shutdown();

                                //this will unload the stage
                                delay.play();
                            }
                        } catch (NotFoundException e) {
                            psFileName = "";
                        }                
                    }                    
                    updateImageView(gimage, imageToShow);
                }
            };
            this.timer = Executors.newSingleThreadScheduledExecutor();
            this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
        } else{
            psFileName = "";
            MsgBox.showOk("Unable to open camera. Please check if camera exists.");
        }      
    }
   
    private void stopAcquisition(){
        if (this.timer!=null && !this.timer.isShutdown()){
            try{
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e){
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()){
            // release the camera
            this.capture.release();
        }
    }
    
    public void shutdown() {
        this.stopAcquisition();
    }
    
    private void unloadScene(){
        Stage stage = (Stage) acButtons.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cmdCancel_Click(ActionEvent event) {
        unloadScene();
    }
}
