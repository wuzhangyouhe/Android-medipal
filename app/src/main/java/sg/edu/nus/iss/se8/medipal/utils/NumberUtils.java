package sg.edu.nus.iss.se8.medipal.utils;

public class NumberUtils {
    public static boolean isStringAPositiveNonZeroInteger(String integerAsString) {
        int number = Integer.parseInt(integerAsString);

        return (number > 0);
    }
}
