import be.rx4.impax.utils.NativeGUITools;

import java.awt.*;

public class GetWindowRect {

    public static void main(String[] args) throws InterruptedException {

        while(true){
            Rectangle windowRectangle = NativeGUITools.getWindowRectangle();

            if(windowRectangle == null){
                System.out.println("could not find selected window");
            }
            System.out.println(windowRectangle.toString());

            Thread.sleep(500);
        }

    }
}
