package lnyswz.jxc.util;

import java.io.File;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * @blog http://sjsky.iteye.com
 * @author Michael
 */
public class ZxingEAN13EncoderHandler {

    /**
     * 编码
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    public void encode(String contents, int width, int height, String imgPath) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.EAN_13, codeWidth, height, null);

            MatrixToImageWriter
                    .writeToFile(bitMatrix, "png", new File(imgPath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEAN13Code(CharSequence s) {
        int length = s.length();

        int sum = 0;
        for (int i = length - 1; i >= 0; i -= 2) {
            int digit = (int) s.charAt(i) - (int) '0';
//            if (digit < 0 || digit > 9) {
//                throw FormatException.getFormatInstance();
//            }
            sum += digit;
        }
        sum *= 3;
        for (int i = length - 2; i >= 0; i -= 2) {
            int digit = (int) s.charAt(i) - (int) '0';
//            if (digit < 0 || digit > 9) {
//                throw FormatException.getFormatInstance();
//            }
            sum += digit;
        }

        int checkCode = sum % 10 > 0 ? 10 - sum % 10 : 0;

        return s + String.valueOf(checkCode);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String imgPath = "d:/test/twocode/zxing_EAN13.png";
        // 益达无糖口香糖的条形码
        String contents = "6923450657713";

        int width = 105, height = 50;
        ZxingEAN13EncoderHandler handler = new ZxingEAN13EncoderHandler();
        handler.encode(contents, width, height, imgPath);

        System.out.println("Michael ,you have finished zxing EAN13 encode.");
    }
}
