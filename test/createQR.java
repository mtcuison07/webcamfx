import org.rmj.appdriver.MySQLAESCrypt;
import org.rmj.webcamfx.lib.QRCode;
import org.apache.commons.codec.binary.Base64;

public class createQR {
    public static void main(String [] args){
        String lsURL = "";
        
        lsURL = "This is a test.";
        QRCode.create(lsURL, "Error 1");
        lsURL = "340299A52425D328D2E6CDBC3C0AEBCEF705FBF246D95E1895796D6629BB7E5B3AB6ECF3D5CBBB7559E945370550DEC5";
        QRCode.create(lsURL, "Error 2");
        lsURL = "DD886983D6B69F0A22FB16773CF37FE1AA4F0DDE5F9AE59F0325DE8606EB168153C7AE684571011F27B436235B5ED1EE";
        QRCode.create(lsURL, "Error 3");
        
        //lsValue = QRCode.read("D:\\GGC_Java_Systems\\temp\\webcam\\convention.png");
        //System.out.println(lsValue);
    }
    
    
    public static String base64_encode(String fsValue){
        // Encode data on your side using BASE64
        byte[] bytesEncoded = Base64.encodeBase64(fsValue.toLowerCase().getBytes());
        return new String(bytesEncoded);
    }
    public static String base64_decode(String fsValue){
        // Decode data on other side, by processing encoded data
        byte[] valueDecoded = Base64.decodeBase64(fsValue);
        return new String(valueDecoded);
    }
}
