
import javafx.application.Application;
import org.json.simple.JSONObject;
import org.rmj.appdriver.MySQLAESCrypt;
import org.rmj.webcamfx.ui.ReadGCardBarcode;

public class testGCard {
    public static void main(String [] args){
        ReadGCardBarcode instance = new ReadGCardBarcode();
        
        Application.launch(instance.getClass());

        JSONObject lsQRData = instance.getGCardInfo();
        
        System.out.println(MySQLAESCrypt.Decrypt((String) lsQRData.get("QR_CODE"), "20190625"));
        System.out.println((String) lsQRData.get("CODE_93"));
    }
}
