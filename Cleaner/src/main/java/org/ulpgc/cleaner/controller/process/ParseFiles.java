package org.ulpgc.cleaner.controller.process;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseFiles {
    public List<String> processFile(String content) {
        List<String> contentSplit = new ArrayList<>();
        String[] parts = content.split("\\*\\*\\*");

        String metadata = parts[0]; // + "***" + parts[1] + "***" + "\n" + "***" + parts[3] + "***" + parts[4];
        String contentText = parts[2].toLowerCase();

        contentSplit.add(contentText);
        contentSplit.add(metadata);

        return contentSplit;
    }

    public static String renameFile(String filename) {
        Pattern pattern = Pattern.compile("(\\d+).txt");
        Matcher matcher = pattern.matcher(filename);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return filename;
    }
}
