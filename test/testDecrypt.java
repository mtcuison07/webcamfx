
import org.rmj.appdriver.MySQLAESCrypt;

public class testDecrypt {
    public static void main (String [] args){
        System.out.println(MySQLAESCrypt.Decrypt("A718FF4E1C36E4FE502A5E3D3B45F382ED6E69240D81F1DAB06936AD9B0C9E487959E8D9C7C06EBA0F110C09D188AA4637F71BC80C27E8E74D7C929F61E87CEA", "20190625"));
    }
}
