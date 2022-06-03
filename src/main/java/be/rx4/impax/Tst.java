package be.rx4.impax;

import be.rx4.impax.model.Measurement;
import be.rx4.impax.utils.ParseUtil;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Tst {
    public static void main(String[] args) throws InterruptedException, IOException {
        Set<Measurement> measurements = new HashSet<>();

        while(true){
            String allText = ImpaxScreenReader.getAllText();

            int previousSize=measurements.size();
            if(!allText.isEmpty()){
                Set<Measurement> detectedMeasurements = ParseUtil.extractMeasurementsFrom(allText);
                measurements.addAll(detectedMeasurements);
            }

            if(measurements.size()>previousSize){
                System.out.print("[");
                for (Measurement measurement : measurements) {
                    System.out.print(measurement.toString()+",");
                }
                System.out.println(']');
            }
            PointerInfo pointerInfo;
            pointerInfo = MouseInfo.getPointerInfo();
            //double x = pointerInfo.getLocation().getX();
            //System.out.println(x);
            Thread.sleep(250);
        }
    }
}
