package com.tj.practice.common.util;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析二维码图片
 */
public class QRCodeUtil {

    public static String parseUrl(String filePath) throws Exception {
        double wr = 0;
        BufferedImage image = ImageIO.read(new URL(filePath)); //读取图片
        int nw = 250 ;
        wr = nw * 1.0/image.getWidth(); //获取缩放比例
//        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, wr), null);
//        Image itemp = ato.filter(image, null); //缩放后的图片
//        ImageIO.write((BufferedImage)itemp,"jpg",new File("C:\\Users\\Administrator\\Desktop\\a4.jpg"));
        BufferedImage newImage = new BufferedImage((int) (wr * image.getWidth()), (int) (wr * image.getHeight()), BufferedImage.TYPE_BYTE_GRAY);
        newImage.getGraphics().drawImage(image, 0, 0, newImage.getWidth() + 1, newImage.getHeight() + 1, 0, 0, image.getWidth(),
                image.getHeight(), null);
        LuminanceSource source
                = new BufferedImageLuminanceSource(newImage);
        Binarizer binarizer = new
                HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new
                BinaryBitmap(binarizer);
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatReader
                formatReader = new MultiFormatReader();
        Result result =
                formatReader.decode(binaryBitmap, hints);
        return result.getText();
    }

    public static String parseQRCode(String imgStr) throws Exception {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(imgStr)));
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatReader formatReader = new MultiFormatReader();
        Result result = formatReader.decode(binaryBitmap, hints);
        return result.getText();
    }
}
