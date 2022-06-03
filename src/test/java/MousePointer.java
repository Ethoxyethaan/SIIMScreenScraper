import java.awt.*;

public class MousePointer
{

    public static void main(String[] args) throws InterruptedException {

        while (true) {
            PointerInfo pointerInfo;
            pointerInfo = MouseInfo.getPointerInfo();
            //double x = pointerInfo.getLocation().getX();
            //System.out.println(x);

            Thread.sleep(500);
            System.out.println("x: " + pointerInfo.getLocation().getX() + ", y: " + pointerInfo.getLocation().getY());
        }
    }

}
