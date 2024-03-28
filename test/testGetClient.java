
import javafx.application.Application;
import org.rmj.webcamfx.ui.ScanQR;

public class testGetClient {
    public static void main(String [] args){
        ScanQR instance = new ScanQR();
        instance.setFormTitle("Scan Client Info");
        instance.setDirectory("d:/GGC_Java_Systems/temp/webcam/");
        instance.setFileName("M00111005387»JO»M12623000392");
        
        Application.launch(instance.getClass());
    }
}
