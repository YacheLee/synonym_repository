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
        Assert.assertEquals(Utils.getSynonyms("彰化").toString(), "[彰化縣, 彰化縣 (清朝), 彰化郡, 彰化市, 彰化市 (省轄市), 彰化市 (州轄市), 彰化街]");
        Assert.assertEquals(Utils.getSynonyms("北大").toString(), "[北京大學, 國立臺北大學, 东北大学 (日本), 北海道大學, 北陸大學, 北大鎮, 北大路 (新竹市)]");
        Assert.assertEquals(Utils.getSynonyms("中大").toString(), "[香港中文大學, 香港中文大學（深圳）, 中山大學, 國立中央大學 (南京), 國立中央大學 (汪精衛政府), 國立中央大學, 中央大學 (日本), 中央大學 (韓國), 江苏中大集团, 物产中大集团, 浙江中大集团, 中大建築股份有限公司|中大建築股份]");
    }

    @Test
    public void isDisambiguousTermTest() {
        Assert.assertTrue(Utils.isDisambiguousTerm("北大"));
        Assert.assertTrue(Utils.isDisambiguousTerm("彰化"));
        Assert.assertTrue(Utils.isDisambiguousTerm("中大"));
        Assert.assertFalse(Utils.isDisambiguousTerm("台大"));
    }

    @Test
    public void getSynonymsWithRedirection() throws ThisPageDoesNotExist {
        Assert.assertEquals(Utils.getSynonyms("台大").toString(), "[台湾大学, 臺灣大學, 臺大, 國立台灣大學, 国立台湾大学, 台灣大學, 台大, 椰林大道, 國立台灣大學水源校區, 國立台灣大學宿舍, 國立臺灣大學]");
    }
}