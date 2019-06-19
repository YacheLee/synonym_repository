package uk.ac.warwick;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void getPageContentTest(){
        Assert.assertEquals(Utils.getPageContent("台大"), "#REDIRECT [[國立臺灣大學]]");
    }

    @Test
    public void isRedirectedTest() {
        Assert.assertTrue(Utils.isRedirected("台大"));
        Assert.assertFalse(Utils.isRedirected("國立臺灣大學"));
    }

    @Test
    public void getRedirectTitleTest(){
        Assert.assertEquals(Utils.getRedirectTitle("台大"), "國立臺灣大學");
        Assert.assertEquals(Utils.getRedirectTitle("國立臺灣大學"), "國立臺灣大學");
    }

    @Test
    public void getRedirectsTestWithoutRedirection(){
        Assert.assertEquals(Utils.getRedirects("國立臺灣大學").toString(), "[台湾大学, 臺灣大學, 臺大, 國立台灣大學, 国立台湾大学, 台灣大學, 台大, 椰林大道, 國立台灣大學水源校區, 國立台灣大學宿舍]");
    }

    @Test
    public void getRedirectsTestWithRedirection(){
        Assert.assertEquals(Utils.getRedirects("台大").toString(), "[台湾大学, 臺灣大學, 臺大, 國立台灣大學, 国立台湾大学, 台灣大學, 台大, 椰林大道, 國立台灣大學水源校區, 國立台灣大學宿舍, 國立臺灣大學]");
    }
}
