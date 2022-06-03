package be.rx4.impax.utils;

import be.rx4.impax.model.Measurement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil {

    private static final Pattern COMPILE = Pattern.compile("^(.*?)([0-9\\.,]*)[ \t]*([cm]{2}).*$");

    public static Set<Measurement> extractMeasurementsFrom(String text){
        Set<Measurement> measurements = new HashSet<>();
        String[] split = text.split("\n");
        for (String s : split) {
            boolean matches = COMPILE.matcher(s).matches();
            if(matches){
                Matcher matcher = COMPILE.matcher(s);
                matcher.find();
                String group1 = matcher.group(2);
                String group2 = matcher.group(3);
                if(Objects.equals(group2, "m")){
                   group2="mm";
                }
                if(Objects.equals(group2, "c")){
                    group2="cm";
                }
                measurements.add(new Measurement(group1,group2));
            }
        }
        return measurements;
    }
}
