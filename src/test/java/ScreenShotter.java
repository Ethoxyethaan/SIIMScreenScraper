import be.rx4.impax.ImpaxScreenReader;

import java.awt.*;

public class ScreenShotter
{

    public static void main(String[] args) throws InterruptedException {

        while (true) {
            Rectangle currentViewPort = ImpaxScreenReader.findCurrentViewPort();
            Thread.sleep(500);

        }
    }

}
