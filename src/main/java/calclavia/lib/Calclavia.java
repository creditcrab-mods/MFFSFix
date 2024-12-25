package calclavia.lib;

import java.util.ArrayList;
import java.util.List;

public class Calclavia {
    public static final String RESOURCE_DIRECTORY = "/mods/calclavia/";
    public static final String TEXTURE_DIRECTORY = "/mods/calclavia/textures/";
    public static final String GUI_DIRECTORY = "/mods/calclavia/textures/gui/";
    public static final String GUI_COMPONENTS
        = "/mods/calclavia/textures/gui/gui_components.png";
    public static final String GUI_BASE_FILE
        = "/mods/calclavia/textures/gui/gui_base.png";
    public static final String GUI_EMPTY_FILE
        = "/mods/calclavia/textures/gui/gui_empty.png";

    public static List<String> splitStringPerWord(String string, int wordsPerLine) {
        String[] words = string.split(" ");
        ArrayList<String> lines = new ArrayList<>();

        for (int lineCount = 0; (double) lineCount
             < Math.ceil((double) ((float) words.length / (float) wordsPerLine));
             ++lineCount) {
            String stringInLine = "";

            for (int i = lineCount * wordsPerLine;
                 i < Math.min(wordsPerLine + lineCount * wordsPerLine, words.length);
                 ++i) {
                stringInLine = stringInLine + words[i] + " ";
            }

            lines.add(stringInLine.trim());
        }

        return lines;
    }
}
