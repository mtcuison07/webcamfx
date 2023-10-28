import org.rmj.appdriver.MySQLAESCrypt;
import org.rmj.webcamfx.lib.QRCode;
import org.apache.commons.codec.binary.Base64;

public class createQRPanalo {
    public static void main(String [] args){
        //String lsURL = "https://restgk.guanzongroup.com.ph/xtest/index.php?";
        String lsURL = "https://restgk.guanzongroup.com.ph/promo/fblike/promoreg.php?";
        
        //0=m001?1=or?2=0001?3=09260375777
        //branch
        String lsBranch = "N001";
        lsBranch = "brc=" + base64_encode(lsBranch); // + "&"
        //document type
        String lsDocTyp = "or";
        lsDocTyp = "typ=" + base64_encode(lsDocTyp) + "&";
        //document no
        String lsDocNox = "00001";
        lsDocNox = "nox=" + base64_encode(lsDocNox) + "&";
        //mobile no
        String lsMobile = "09260375777";
        lsMobile = "mob=" + base64_encode(lsMobile) + "&";       
        
        //String lsValue = MySQLAESCrypt.Encrypt("m001", "20190625");
        //String lsValue = lsURL + lsBranch + lsDocTyp + lsDocNox + lsMobile;
        //System.out.println(lsValue);
        
        String lsValue = lsURL + lsBranch;
        System.out.println(lsValue);
        
        QRCode.create(lsValue, "H001");
        
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
