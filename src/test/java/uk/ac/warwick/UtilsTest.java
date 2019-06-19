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
}
