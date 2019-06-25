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

    @Test
    public void testGetPolysemy_three_army(){
        String content =  "'''三军'''，是中文对于军队称呼的一种，其历史最早可上溯到[[春秋]]时代，但是随着历史的发展，其意义有明显的变化。\n\n最早的三军指春秋[[诸侯]]的军队，按照[[周朝|周]]制，天子可以有六军，大的诸侯国可以有三军，三军常称为上军、中军、下军，每军传统上按一万二千五百人制。<ref>《周礼·夏官司马》：“凡制军，万有二千五百人为军。王六军，大国三军，次国二军，小国一军。”</ref>军的统帅也称为[[将军 (中国古代)|将军]]，诸侯常自领上军。此时期另一种三军则是指[[兵种]]不同，即合指[[步兵]]、[[骑兵]]和[[战车]]。\n\n[[秦朝]]统一以后，这种体制也不复存在；而随着中国军事情况演变，战车也逐渐退出历史舞台，所以三军可以指代一支出征的部队前军、中军、后军的。而原来的国分三军的做法从[[三国]]之后，更加被中军、外军之分所取代，更多的是设“中、前、后、左、右”五军甚至更多；“三”字泛指众多，作为整个部队的统称，并不是准确的数字。\n\n到了现代战争，军队兵种的分化趋现[[陆军]]、[[海军]]、[[空军]]三个方面，所以三军又被可以代指这三个大[[军种]]。但是三军原来的通指整个部队的含义仍然存在，并有明显的拓展，被广泛的于[[体育]]赛事中的队伍。\n\n== 参考文献 ==\n{{Reflist}}\n\n[[Category:中国古代军事]]\n[[Category:中国并称]]\n[[Category:中国名数3]]\n\n{{中国历史小作品}}";
        Assert.assertEquals(Utils.getPolysemy(content, 3).toString(), "[[上军, 中军, 下军], [前军, 中军, 后军], [从三国之后，更加被中军, 外军之分所取代，更多的是设“中, 前、后、左、右”五军甚至更多；], [前, 后, 左], [陆军, 海军, 空军]]");
    }

    @Test
    public void testGetPolysemy_three_high(){
        String content = "'''三高'''可以指：\\n\\n* [[三高 (擇偶條件)]]，部份[[女性]]的擇偶條件：高收入、高學歷、高身長\\n* [[三高女性]]，高收入、高學歷、高职位的女性\\n* [[三高 (疾病)]]，「高[[血脂]]、高[[血壓]]、高[[血糖]]」的合稱，為中年人常見[[疾病]]\\n* [[三高 (明治時代)]]，[[日本]][[明治時代]]的「高志」、「高德」、「高潔」價值觀\\n* [[三高城市]]，高浪費、高消耗、高污染的[[城市]]\\n*[[舊制第三高等學校 (日本)]]\\n\\n{{disambig|S|Cat=二字消歧义}}";
        Assert.assertEquals(Utils.getPolysemy(content, 3).toString(), "[[高收入, 高學歷, 高身長], [高收入, 高學歷, 高职位], [高血脂, 高血壓, 高血糖], [高志, 高德, 高潔], [高浪費, 高消耗, 高污染]]");
    }
}