package com.wuda.bbs.utils.xmlHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleContentRegex {
    private static final String TIME_REGEX = "(?<=发信站: 珞珈山水 \\().*?(?=\\))"; //"(?<=\\\\n发信站: 珞珈山水 \\().*?(?=\\), .*\\\\n\\\\n)";
    private static final String CONTENT_REGEX = "(?<=站内\\\\n\\\\n)..*?(?=\\\\n【)|(?<=\\\\n\\\\n).*?(?=\\\\n--\\\\n)";
//    private static final String CONTENT_REGEX = "(?<=\\\\n\\\\n).*(?=\\\\n--\\\\n)|(?<=\\\\n\\\\n).*?(?=\\\\n\\\\n\\\\r\\[)"; //"(?<=站内\\\\n\\\\n).*?(?=\\\\n【)";
//    private static final String CONTENT_REGEX = "(?<=站内\\\\n\\\\n).*?(?=\\\\n【)";
//    private static final String REPLY2USERNAME_REGEX = "(?<=【 在).*?(?= \\()";
    private static final String REPLY2USERNAME_REGEX = "(?<=【 在).*?(?= \\()|(?<=【 在).*?(?=的大作中提到)";
    private static final String REPLY2CONTENT_REGEX = "(?<=大作中提到: 】\\s{0,5}\\\\n).*?(?=\\\\n)";

    public static String getTime(String rawContent) {
        Matcher matcher = Pattern.compile(TIME_REGEX).matcher(rawContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String getContent(String rawContent) {
        Matcher matcher = Pattern.compile(CONTENT_REGEX).matcher(rawContent);
        if (matcher.find()) {
            String content = matcher.group();
            content = content.replaceAll("\\\\n", "\n");
            return content.trim();
        }
        return "";
    }

    public static String getReply2username(String rawContent) {
        Matcher matcher = Pattern.compile(REPLY2USERNAME_REGEX).matcher(rawContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String getReply2content(String rawContent) {
        Matcher matcher = Pattern.compile(REPLY2CONTENT_REGEX).matcher(rawContent);
        if (matcher.find()) {
            String reply2content = matcher.group();
            if (reply2content.length() > 10)
                reply2content = reply2content.substring(0, 10);
            return reply2content + "...";
        }
        return "";
    }
}