package irisia.utils;

public class StringUtil {
    public static String capitalizeOnlyFirst(final String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 1) {
            return Character.toString(s.charAt(0));
        }
        return (s.charAt(0)) + s.substring(1).toLowerCase();
    }

    public static String simpleTranslateColors(String string) {
        return string.replace("&", "\u00A7");
    }
    public static String formatArguments(String... args){
        String formatedArguments = "";

        for(String argument : args){
            formatedArguments += argument + " | ";
        }
        return formatedArguments.replace("lastArgument | ", "");
    }
}