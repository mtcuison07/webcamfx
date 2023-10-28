


import java.sql.ResultSet;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.webcamfx.lib.QRCode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mac
 */
public class createQRHealthChecklist {
    public static void main(String [] args){
        final String PRODUCTID = "gRider";
        final String USERID = "M001111122";
        
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
        
        String lsSQL = "SELECT sBranchCd, sBranchNm" +
                        " FROM Branch" +
                        " WHERE cRecdStat = '1'" + 
                            " AND sBranchCd IN ('M180')" + 
                        " ORDER BY sBranchCd";
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        String lsMainURL = "https://restgk.guanzongroup.com.ph/system/health_checklist/checklist_entry.php?brc=";
        String lsURL = "";
        try {
            while (loRS.next()){
                
                lsURL = lsMainURL + loRS.getString("sBranchCd");
                System.out.println(loRS.getString("sBranchNm") + "\t" + lsURL);
                
                QRCode.create(lsURL, loRS.getString("sBranchCd") + "-" + loRS.getString("sBranchNm"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
