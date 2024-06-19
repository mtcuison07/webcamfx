package org.rmj.webcamfx.lib;

import java.io.File;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class QRCode {
    private static final String pxeModuleName = "QRCode";
    private static final String pxeDefaultDIR = "D:\\GGC_Java_Systems\\temp\\webcam\\";
    private static final String pxeDefaultExt = ".png";
    private static final String pxeCharset = "UTF-8";
    
    /**
     * QR CODE CREATE
     * @param fsQRCdData the string value of QR image to be created.
     * @param fsFileName the image filename when saved(do not include extension name).
     * @return BOOLEAN
     */
    public static boolean create(String fsQRCdData, String fsFileName){
        try {
            generateQRCodeImage(fsQRCdData, 1080, 1080, pxeDefaultDIR + fsFileName + pxeDefaultExt);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static boolean create(String fsQRCdData, String fsTitle, String fsFileName, int fnSize){
        try {
            // Generate QR code
            BitMatrix bitMatrix = new MultiFormatWriter().encode(fsQRCdData, BarcodeFormat.QR_CODE, fnSize, fnSize);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Create the final image with space for the text below the QR code
            int textHeight = 150;
            BufferedImage finalImage = new BufferedImage(fnSize, fnSize + textHeight, BufferedImage.TYPE_INT_RGB);

            // Draw the QR code on the final image
            Graphics2D g = finalImage.createGraphics();
            g.setColor(Color.WHITE); // Set background to white
            g.fillRect(0, 0, fnSize, fnSize + textHeight); // Fill the entire image
            g.drawImage(qrImage, 0, 0, null);

            // Draw the text below the QR code
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            FontMetrics fontMetrics = g.getFontMetrics();
            int textWidth = fontMetrics.stringWidth(fsTitle);
            int x = (fnSize - textWidth) / 2;
            int y = fnSize + ((textHeight - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();
            g.drawString(fsTitle, x, y);
            g.dispose();

            // Save the final image to file
            ImageIO.write(finalImage, "png", new File(pxeDefaultDIR + fsFileName + ".png"));

            System.out.println("QR code with text generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public static String encrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);   
    }
    
    public static String decrypt(String encryptedData, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decodedEncryptedData = Base64.getDecoder().decode(encryptedData);
        byte[] original = cipher.doFinal(decodedEncryptedData);
        return new String(original, "UTF-8");
    }
    
    public static String read(String fsFilePath){
        try {
            File file = new File(fsFilePath);
            String decodedText = decodeQRCode(file);
            if(decodedText == null) {
                System.out.println("No QR Code found in the image");
            } else {
                System.out.println("Decoded text = " + decodedText);
                return decodedText;
            }
        } catch (IOException e) {
            System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
        }
        
        return null;
    }
    
    private static String decodeQRCode(File fsFilePath) throws IOException{
        BufferedImage bufferedImage = ImageIO.read(fsFilePath);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }
    
    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    private static byte[] getQRCodeImageByteArray(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }
}
