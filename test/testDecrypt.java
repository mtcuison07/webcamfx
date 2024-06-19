
import java.sql.ResultSet;
import java.sql.SQLException;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.MySQLAESCrypt;
import org.rmj.webcamfx.lib.QRCode;

public class testDecrypt {
    public static void main (String [] args){
        final String PRODUCTID = "gRider";
        final String USERID = "M001111122";
        
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Java_Systems";
        }
        else{
            path = "/srv/GGC_Java_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        
        GRider poGRider = new GRider(PRODUCTID);

        if (!poGRider.loadEnv(PRODUCTID)) {
            System.err.println(poGRider.getMessage());
            System.err.println(poGRider.getErrMsg());
            MiscUtil.close(poGRider.getConnection());
            System.exit(1);
        }
        if (!poGRider.logUser(PRODUCTID, USERID)) {
            System.err.println(poGRider.getMessage());
            System.err.println(poGRider.getErrMsg());
            MiscUtil.close(poGRider.getConnection());
            System.exit(1);
        }
        
        String lsSQL = "SELECT" +
                            "  CONCAT(a.`sEmployID`, '»', b.`sCompnyNm`, '»', c.`sBranchNm`, '»', d.`sDeptName`, '»', e.`sPositnNm`) xEmployID" +
                            ", b.sCompnyNm" +
                            ", a.sEmployID" +
                        " FROM Employee_Master001 a" +
                            " LEFT JOIN Client_Master b ON a.`sEmployID` = b.`sClientID`" +
                            " LEFT JOIN Branch c ON a.sBranchCd = c.`sBranchCd`" +
                            " LEFT JOIN Department d ON a.`sDeptIDxx` = d.`sDeptIDxx`" +
                            " LEFT JOIN `Position` e ON a.`sPositnID` = e.`sPositnID`" +
                        " WHERE a.`sBranchCd` = 'M001'" +
                            " AND a.`cRecdStat` = '1'";
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        try {
            while (loRS.next()){
                lsSQL = loRS.getString("xEmployID");
        
                lsSQL = MySQLAESCrypt.Encrypt(lsSQL, "08220326");
                
                QRCode.create(lsSQL, 
                        loRS.getString("sCompnyNm"),
                        loRS.getString("sEmployID"),
                        1080);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
//        System.out.println(MySQLAESCrypt.Decrypt("614E2A76C64C5DA50FC4F06CCA0B62340DB64FC3E1ED1D05460BB3413227B28E5E4B2F1E0051770E0F94FBC7CF008BD589678DF6A8FA2F7DD4EA37F5E5AD4C1AB96021410FC58F17346A23F80E1400B70CA0F2F64821F1F31AF66E25F1271937B82812732D8C1988E27F927398DC4CDB", "08220326"));
        
        
    }
}
