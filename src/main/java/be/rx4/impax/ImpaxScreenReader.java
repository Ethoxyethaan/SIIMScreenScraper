package be.rx4.impax;

import be.rx4.impax.utils.NativeGUITools;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImpaxScreenReader {

    private static final Robot ROBOT;
    public static final int IMPAX_PRESENTATION = 0xcccccc;
    //private static PointerInfo mousePointerInfo = ;

    static {
        Robot ROBOT1;
        try {
            ROBOT1 = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            ROBOT1 = null;
        }
        ROBOT = ROBOT1;
    }

    private static final ITesseract instance = new Tesseract();  // JNA Interface Mapping

    static {
        instance.setDatapath("C:\\Program Files\\VietOCR\\tessdata"); // path to tessdata directory
        instance.setTessVariable("user_defined_dpi", "2400");
        instance.setTessVariable("load_system_dawg",     "false");
        instance.setTessVariable("load_freq_dawg",      "false");
        instance.setTessVariable("user_words_suffix",    "user-words");
        instance.setTessVariable("user_patterns_suffix",  "user-patterns");
        instance.setTessVariable("tessedit_char_whitelist", "0123456789.,mc²³");
        //instance.setTessVariable("tessedit_create_hocr", "1");
        instance.setTessVariable("tessedit_pageseg_mode", "1");
        instance.setTessVariable("hocr_font_info", "0");
        instance.setTessVariable("tessedit_create_pdf", "1");
        instance.setTessVariable("tessedit_pageseg_mode", "1");
        instance.setTessVariable("tessedit_pageseg_mode", "1");
        instance.setTessVariable("tessedit_create_txt", "1");
        instance.setTessVariable("tessedit_write_unlv", "1");
        instance.setTessVariable("tessedit_pageseg_mode", "6");
        instance.setLanguage("eng");
    }
    //X: 258 Y: 179         top left
    //X: 1593 Y: 1005       bottom right

    //R 204 G 204 B 204

    public static String getAllText() throws IOException {

        String result = "";
        if(!NativeGUITools.isImpax()){
            return result;
        }

        BufferedImage screenCapture = ROBOT.createScreenCapture(new Rectangle(258, 180, 1593 - 258, 1005 - 180));
        File outputfile99 = new File("C:\\uz\\Temp\\TESS4J\\image_"+new Random().nextInt(Integer.MAX_VALUE)+".png");
        ImageIO.write(screenCapture, "png", outputfile99);
        //dit kan 100 maal beter met een buffers maar is gewoon prototype.
        for(int i = 0; i<(1593 - 258);i++){
            for(int j = 0; j < (1005 - 180);j++){
                int rgb = screenCapture.getRGB(i, j);
                if((rgb&0xFFFFFF)== IMPAX_PRESENTATION){
                    screenCapture.setRGB(i,j,0x0);
                } else {
                    screenCapture.setRGB(i,j,0xFFFFFF);
                }
            }
        }

        File outputfile = new File("C:\\uz\\Temp\\TESS4J\\image_"+new Random().nextInt(Integer.MAX_VALUE)+".png");
        ImageIO.write(screenCapture, "png", outputfile);

        //System.out.println(windowRectangle);

        try {
            result = instance.doOCR(screenCapture);
            //System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }


    public static Rectangle findCurrentViewPort(){

        Rectangle windowRectangle = NativeGUITools.getWindowRectangle();

        if (windowRectangle == null){
            return null;
        }

        if(!windowRectangle.contains(MouseInfo.getPointerInfo().getLocation())){
            return null;
        }

        int x =  MouseInfo.getPointerInfo().getLocation().x - windowRectangle.x;
        int y = MouseInfo.getPointerInfo().getLocation().y - windowRectangle.y;
        x=x<0?0:x;
        y=y<0?0:y;
        BufferedImage image = ROBOT.createScreenCapture(windowRectangle);
        /*
        try {
            for(int i=-5; i < 5;i++){
                for(int j = -5;j<5;j++){
                    image.setRGB(x+i, y+j, 0xFF0000);
                }
            }
            System.out.println("DRAWING!!!! "+x + " "+y);
        } catch (Throwable th){
            System.out.println("hello");
        }*/

        //search down
        Rectangle bottomRectangle = null;

        Rectangle topRectangle = null;

        for(int i = x;i<image.getHeight() && bottomRectangle==null;i++){
            if((image.getRGB(i,y)&0xFFFFFF)==IMPAX_PRESENTATION){
                bottomRectangle =  getLineHorizontal(150,x,i,image);
            }
        }

        for(int i = x;i>=0&&topRectangle == null;i--){
            if((image.getRGB(i,y)&0xFFFFFF)==IMPAX_PRESENTATION){
                topRectangle=  getLineHorizontal(150,x,i,image);
            }
        }


        System.out.println(topRectangle);
        System.out.println(bottomRectangle);
        //search up

        //search left

        //search right

        File outputfile = new File("C:\\uz\\Temp\\TESS4J\\image_"+new Random().nextInt(Integer.MAX_VALUE)+".png");
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }



        /*
        for(int i = 0; i<(1593 - 258);i++){
            for(int j = 0; j < (1005 - 180);j++){
                int rgb = image.getRGB(i, j);
                if((rgb&0xFFFFFF)==0xcccccc){
                    image.setRGB(i,j,0x0);
                } else {
                    image.setRGB(i,j,0xFFFFFF);
                }
            }
        }*/

        return null;
    }


    private static Rectangle getLineHorizontal(int minSize,int x,int y,BufferedImage image){
        System.out.println("trying to find line at coordinate "+x+" : "+y);
        int counter= 0;
        int start =0;
        int finish = 0;
        try {
            for (int i = 0; i < image.getWidth(); i++) {
                int rgb = image.getRGB(i, y);
                if (rgb == IMPAX_PRESENTATION) {
                    counter++;
                    if (start == 0) {
                        start = i;
                    } else {
                        finish = i;
                    }

                } else if (counter < minSize) {
                    start = 0;
                    finish = 0;
                    //counter =0;
                }

            }
            if (counter >= minSize) {
                return new Rectangle(start, y, finish - start, 1);

            }
        } catch (Throwable ignore){
            System.out.println("what happened? ");
        }

        return null;
    }



}
