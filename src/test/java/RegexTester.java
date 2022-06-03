import be.rx4.impax.utils.ParseUtil;

public class RegexTester {

    public static void main(String[] args) {

        ParseUtil.extractMeasurementsFrom(" 180.2mm");
        ParseUtil.extractMeasurementsFrom(" 1802mm");
        ParseUtil.extractMeasurementsFrom("   180.2 mm");
        ParseUtil.extractMeasurementsFrom(" 180.2  mm    ");

    }
}
