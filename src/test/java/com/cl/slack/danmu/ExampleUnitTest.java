package com.cl.slack.danmu;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    String html =
            "            \"args\" : {\n" +
            "                                \"chTopId\" : \"77226790\",\n" +
            "                \"subChId\" : \"2565893989\"\n" +
            "                   \n" +
            "            },\n" +
            "            \"assets\": {\n";
    @Test
    public void match() throws Exception{

        String topId,subId;
        Pattern pattern = Pattern.compile("\"chTopId\"\\s*:\\s*\"(.*?)\".*\\n.*\"subChId\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        topId = matcher.group(1);
        subId = matcher.group(2);

//        String topId,subId;
//        Pattern pattern = Pattern.compile("\"chTopId\"\\s*:\\s*\"(.*?)\"");
//        Matcher matcher = pattern.matcher(html);
//        matcher.find();
//        topId = matcher.group(1);
//
//        pattern = Pattern.compile("\"subChId\"\\s*:\\s*\"(.*?)\"");
//        matcher = pattern.matcher(html);
//        matcher.find();
//        subId = matcher.group(1);
    }
}