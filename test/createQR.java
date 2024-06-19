import org.rmj.appdriver.MySQLAESCrypt;
import org.rmj.webcamfx.lib.QRCode;
import org.apache.commons.codec.binary.Base64;

public class createQR {
    public static void main(String [] args){
        String lsURL = "";
        
        lsURL = "https://restgk.guanzongroup.com.ph/apk/gConnect.apk";
        QRCode.create(lsURL, "Guanzon Connect");
      
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
