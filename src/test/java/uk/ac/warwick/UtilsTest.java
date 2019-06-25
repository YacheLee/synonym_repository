package uk.ac.warwick;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.warwick.exceptions.ThisPageDoesNotExist;

public class UtilsTest {
    @Test
    public void getPageContentTest() throws ThisPageDoesNotExist {
        Assert.assertEquals(Utils.getPageContent("台大"), "#REDIRECT [[國立臺灣大學]]");
    }

    @Test(expected = ThisPageDoesNotExist.class)
    public void getPageContentTestForEmptyPage() throws ThisPageDoesNotExist {
        Utils.getPageContent("Warwick");
    }

    @Test
    public void isRedirectedTest_True() throws ThisPageDoesNotExist {
        Assert.assertTrue(Utils.isRedirected("台灣"));
        Assert.assertTrue(Utils.isRedirected("台大"));
    }

    @Test
    public void isRedirectedTest_False() throws ThisPageDoesNotExist {
        Assert.assertFalse(Utils.isRedirected("國立臺灣大學"));
    }

    @Test
    public void getRedirectTitleTest(){
        Assert.assertEquals(Utils.getRedirectTitle("台大"), "國立臺灣大學");
        Assert.assertEquals(Utils.getRedirectTitle("國立臺灣大學"), "國立臺灣大學");
    }

    @Test
    public void testGetCategories(){
        Assert.assertEquals(Utils.getCategories("台大").toString(),"[]");
        Assert.assertEquals(Utils.getCategories("北大").toString(),"[Category:二字亞洲相關消歧義, Category:全部主條目消歧義頁面, Category:全部消歧義頁面]");
        Assert.assertEquals(Utils.getCategories("彰化").toString(),"[Category:二字臺灣地名消歧義, Category:全部主條目消歧義頁面, Category:全部消歧義頁面]");
    }

    @Test
    public void getSynonymsWithoutRedirection() throws ThisPageDoesNotExist {
        Assert.assertEquals(Utils.getSynonyms("國立臺灣大學").toString(), "[台湾大学, 臺灣大學, 臺大, 國立台灣大學, 国立台湾大学, 台灣大學, 台大, 椰林大道, 國立台灣大學水源校區, 國立台灣大學宿舍]");
    }

    @Test
    public void getSynonymsWithoutRedirection_Empty() throws ThisPageDoesNotExist {
        Assert.assertEquals(Utils.getSynonyms("彰化").toString(), "[]");
    }

    @Test
    public void getSynonymsWithRedirection() throws ThisPageDoesNotExist {
        Assert.assertEquals(Utils.getSynonyms("台大").toString(), "[台湾大学, 臺灣大學, 臺大, 國立台灣大學, 国立台湾大学, 台灣大學, 台大, 椰林大道, 國立台灣大學水源校區, 國立台灣大學宿舍, 國立臺灣大學]");
    }
}