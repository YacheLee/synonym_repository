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
        Assert.assertEquals(Utils.getPolysemy(content, 3).toString(), "[[上军, 中军, 下军], [陆军, 海军, 空军], [前军, 中军, 后军]]");
    }

    @Test
    public void testGetPolysemy_three_high(){
        String content = "'''三高'''可以指：\\n\\n* [[三高 (擇偶條件)]]，部份[[女性]]的擇偶條件：高收入、高學歷、高身長\\n* [[三高女性]]，高收入、高學歷、高职位的女性\\n* [[三高 (疾病)]]，「高[[血脂]]、高[[血壓]]、高[[血糖]]」的合稱，為中年人常見[[疾病]]\\n* [[三高 (明治時代)]]，[[日本]][[明治時代]]的「高志」、「高德」、「高潔」價值觀\\n* [[三高城市]]，高浪費、高消耗、高污染的[[城市]]\\n*[[舊制第三高等學校 (日本)]]\\n\\n{{disambig|S|Cat=二字消歧义}}";
        Assert.assertEquals(Utils.getPolysemy(content, 3).toString(), "[[高收入, 高學歷, 高职位], [高血脂, 高血壓, 高血糖], [高志, 高德, 高潔], [高收入, 高學歷, 高身長], [高浪費, 高消耗, 高污染]]");
    }

    @Test
    public void testGetPolysemy_three_nation(){
        //三國
        //曹魏、蜀漢及孫吳
        String content = "{{Otheruses|subject=中國歷史上的三國時期|三國 (消歧義)}}\\n\\n{{NoteTA\\n|1=zh-hans:杰; zh-hant:杰;\\n|2=zh-hans:于; zh-hant:于;\\n}}\\n\\n{| class=\\\"infobox bordered\\\" style=\\\"align: right; width: 25em; font-size: 90%; text-align: left; float: right;\\\" cellpadding=\\\"4\\\"\\n|-\\n! colspan=\\\"4\\\"  style=\\\"background:#fca; font-size:120%; text-align:center;\\\"| '''三國'''\\n|-\\n| '''國家''' || [[蜀漢]] || [[曹魏]] || [[孫吳|東吳]]\\n|-\\n| '''首都''' || [[成都]] || [[洛陽]] || [[南京|建業]]\\n|-\\n| '''君主'''<br />&nbsp;-開國君主<br /> &nbsp;-亡國君主\\n| 2帝<br />[[劉備]]<br />[[劉禪]]  || 5帝<br />[[曹丕]]<br />[[曹奐]]  ||  4帝<br />[[孫權]]<br />[[孫皓]]\\n|-\\n| '''成立''' || 221年<br />於[[成都]]立國 || 220年<br />[[曹丕]]篡[[東漢]] || 229年<br />於[[建業]]立國\\n|-\\n| '''滅亡''' || 263年<br />[[魏滅蜀之戰]] || 265年<br />[[司馬炎篡魏]] || 280年<br />[[晉滅吳之戰]]\\n|-\\n| colspan=\\\"4\\\" style=\\\"text-align:center;\\\"|{{File|zh-hant=三国行政区划(繁).png|zh-hans=三国行政区划(简).png|280px}}\\n|-\\n|colspan=4 |三國疆域圖：<br/>{{colorbox|#b3cc94}} 綠色為曹魏疆域<br />{{colorbox|#d4a45c}} 黃色為蜀漢疆域<br />{{colorbox|#cc7883}} 紅色為孫吳疆域\\n|}\\n\\n'''三國'''（狹義220年－280年，廣義184年、190年、208年－280年{{efn|三國時期開始的時間，一般分成狹義及廣義。\\n*狹義是220年曹丕逼漢獻帝禪讓，建國曹魏，使東漢滅亡開始<ref name=\\\"start\\\"/><ref name=\\\"鄒紀萬魏晉南北朝的政治變遷\\\">鄒紀萬（1992年）:《中國通史 魏晉南北朝史》第一章〈魏晉南北朝的政治變遷〉，第12頁：「大致說，赤壁戰後，纔有三國分立形式的醞釀；到了建安二十四年（220年），三國分立成為定局。次年，曹丕篡漢，步入了中國歷史上所謂的『魏晉南北朝』時期。但是嚴格定義的三國分立，則要到孫權稱帝的那一年纔算正式開始」</ref><ref>柏楊：《中國人史綱》，2007年7月 初版11刷 ISBN 978-957-32-4752-4 及 [[柏楊]]：中國歷史年表</ref>。\\n*廣義分別有：\\n**184年[[汉灵帝]]光和七年，这是正史《[[三国志]]》开始记述的年份，[[黃巾之亂]]在这一年開始。東漢歷經黃巾之亂後動盪不安，各地有叛亂。187年[[劉焉 (益州牧)|劉焉]]向朝廷建議以宗室及重臣為[[州牧]]來安定地方。這個制度被[[劉昭 (南梁)|刘昭]]诟病为地方割據與天下分裂的開端<ref>《续汉书》志第二十八百官五刘昭注：至孝灵在位，横流既及，刘焉徼伪，自为身谋，非有忧国之心，专怀狼据之策，抗论昏世，荐议愚主，盛称宜重牧伯，谓足镇压万里，挟奸树算，苟罔一时，岂可永为国本，长期胜术哉？夫圣主御世，莫不大庇生民，承其休谋，传其典制。犹云事久獘生，无或通贯，故变改正服，革异质文，分爵三五，参差不一。况在竖騃之君，挟奸诈之臣，共所创置，焉可仍因？大建尊州之规，竟无一日之治。故焉牧益土，造帝服于岷、峨；袁绍取冀，下制书于燕、朔；刘表荆南，郊天祀地；魏祖据兖，遂构皇业：汉之殄灭，祸源乎此。</ref><ref name=\\\"st\\\">葛劍雄，《統一與分裂》（三聯書店1994年版ISBN 978-7 -108-00607-3）：“而到中平元年（184年）黃巾軍起，割據分裂已成事實。建安十三年赤壁之戰後，三國鼎立的形勢已經形成。</ref>。\\n**187年董卓率軍進入洛陽開始，東漢皇帝便陷入軍閥的挾持之中，此時東漢朝廷已經崩潰<ref>馬植杰（2006年）:《三國史》第一章〈東漢王朝的衰落〉，第一節〈東漢後期宦官的擅政〉，第1頁：「實際上，從漢靈帝中平六年（187年）董卓率軍進入洛陽開始，東漢皇帝便陷入軍閥的挾持之中，全國一統的局面也隨著瓦解，所以我們說東漢王朝的壽命實際只有165年。」</ref>。189年[[漢靈帝]]死，由[[刘辩|漢少帝]]即位<ref name=\\\"屑\\\"/>{{rp|1}}。外戚首領大将军[[何进]]聽取部將[[袁紹]]建議，征調[[軍閥]][[董卓]]入京剿滅[[宦官]]勢力，造成董卓亂政<ref name=\\\"屑\\\"/>{{rp|2}}。9月，董卓廢少帝[[劉辨|漢少帝]]為弘農王，立陳留王[[劉協]]為帝<ref name=\\\"屑\\\"/>{{rp|2}}。12月，曹操作檄文號召各地[[諸侯]]共起[[董卓討伐戰|討伐董卓]]<ref name=\\\"屑\\\"/>{{rp|2}}。\\n**190年群雄[[討伐董卓]]，董卓挾漢獻帝離開洛陽開始，群雄不再聽令朝廷，全國一統局面瓦解<ref name=\\\"start\\\">「一般認為，三國的歷史應從公元二二〇年曹丕稱帝算起，… …。在魏、蜀、吳三個政權正式建立之前，三國鼎立的格局就形成。因此三國的歷史包括三國正式建立前二十年的軍閥混戰時期，大概從漢獻帝初二年（公元190年）董卓之亂開始。」《中國文明史第四卷魏晉南北朝上冊》〈1.政治發展大勢〉，〈分裂的地點\u200B\u200B--三國鼎立〉，第3頁</ref>。\\n**208年赤壁之戰後開始，此時三國鼎立形成雛型<ref name=\\\"鄒紀萬魏晉南北朝的政治變遷\\\"/>。 \\n**歷史學家多注重三國鼎立的形成與過程，自184年東漢皇室失去政權實體及群雄割據，形成了三國雛型至魏代漢為止，所以往往將184年到220年的時間納入三國時期加以討論\\n<ref name=\\\"start\\\" /><ref>趙翼，《廿二史劄記·第七卷·三國志晉書》：“人才莫盛於三國，亦惟三國之主各能用人，故得眾力相扶，以成鼎足之勢。而其用人亦各有不同者，大概曹操以權術相馭，劉備以性情相契，孫氏兄弟以意氣相投。”</ref><ref>黎東方，《細說三國》（上海人民出版社，2000年版，ISBN 978-7-208-03442-6），408-409頁：“三國時代的經學、文學、史學、藝術以及科學，都十分發達。……論文學，我們只須再提一下曹操的‘對酒當歌’與曹植的‘翩若驚鴻，婉若游龍，榮曜秋菊，華茂春松’不就夠了嗎？再說，[[建安七子]]怎麼樣？東漢有沒有？……談到科學，三國時代有過會用麻醉藥，甚至有把握進行‘神經解剖’的名醫[[華佗]]。”</ref>。}}。）是[[中國歷史]]一段三個國家並立的時期。一般認為是從[[建安 (東漢)|建安]][[元年]]起算<ref name=\\\"屑\\\">{{cite book|author=李異嗚編著|title=《三國的碎屑》|location=[[哈爾濱]]|publisher=北方文藝出版社|isbn=9787531721222|date=2007年4月}}</ref>{{rp|154}}。三國是指[[曹魏]]、[[蜀漢]]及[[孫吳]]<ref name=\\\"屑\\\"/>{{rp|154}}。[[東漢]]末年戰爭不斷，使得人口急劇下降，經濟嚴重損害，因此三國皆重視經濟發展，加上戰爭帶來的需求，各種技術都有許多進步<ref name=\\\"三國鼎立\\\"/>。\\n\\n[[東漢末年]]，漢廷因[[黃巾之亂]]、[[北宫伯玉]]之乱、[[黑山军]]起义、[[王芬]]谋废[[汉灵帝|灵帝]]、[[張舉 (東漢)|张举]][[張純 (東漢末)|张纯]]叛乱、[[何进|外戚]][[十常侍|宦官]]火拼等一系列事件而动荡不安。184年[[漢靈帝]]時期，以[[張角]]三兄弟為首爆发[[黃巾之亂]]<ref name=\\\"屑\\\"/>{{rp|1}}。為鎮壓黃巾，一方面放權到[[州牧]]、[[太守]]，一方面縱容地主組織私人武裝，對抗黃巾<ref name=\\\"屑\\\"/>{{rp|1}}。[[董卓]]亂政並與[[董卓討伐戰|關東諸勢力對抗]]後遷都[[長安]]，使得朝廷威信喪失，地方长官演变为独立军阀割據混戰。其中[[曹操]]擁護逃回[[洛陽]]的[[漢獻帝]]，遷都[[許昌|許]]。他擊敗多股勢力，最後在200年的[[官渡之戰]]擊敗北方最大勢力[[袁紹]]，大致掌控中國北方。曹操以優勢兵力南征[[荊州刺史部|荊州]]，但在208年冬天的[[赤壁之戰]]被[[孫權]]和[[劉備]]聯軍擊敗，形成三國鼎立的雛型。220年曹操病逝，其子[[曹丕]]迫漢獻帝[[禪讓]]稱帝，國號「魏」，史稱'''[[曹魏]]'''，至此東漢滅亡，正式進入三國時期。隔年以[[益州]]為主的劉備也以漢室宗親的身份稱帝，國號續為「漢」，史稱'''[[蜀漢]]'''。劉備與孫權在赤壁之戰後積極拓展勢力，為了荊州問題多次發生糾紛與戰爭，最後劉備在[[夷陵之戰]]戰敗，孫權獲得整個荊州南部。劉備病死後，輔佐其子[[劉禪]]的[[諸葛亮]]於同年再與孫權恢復同盟。據有[[揚州刺史部|揚州]]、荊州及[[交趾刺史部|交州]]等地的孫權遲至229年正式稱帝，國號「吳」，史稱'''[[孫吳]]'''<ref name=\\\"三國鼎立\\\">鄒紀萬（1990年）:《中國通史·魏晉南北朝史》第一章〈魏晉南北朝的政治變遷〉，第一節〈三國鼎立〉</ref>{{rp|6-21}}。此後三國局勢主要為蜀吳同盟對抗曹魏，各國疆域變化不大。而曹魏朝廷漸漸的被司馬氏掌控。263年[[司馬昭]]為建立軍功準備篡位，[[魏滅蜀之戰|出兵伐蜀]]，蜀漢亡。兩年後司馬昭病死，其子[[司馬炎]]廢[[魏元帝]]自立，國號為「晉」，史稱[[西晉]]，曹魏亡。西晉最後於280年發起[[晉滅吳之戰]]，滅亡孫吳，統一中國。至此三國時期結束，進入[[晉朝]]<ref name=\\\"三國鼎立\\\" />{{rp|22-27}}。\\n\\n三國時期人才輩出，後世常常追思當時風雲人物。[[陳壽]]所著、[[裴松之]]作注的《[[三國志]]》，是二十四史中評價最高的「前四史」之一，成為研究三國歷史的重點書籍。而[[羅貫中]]結合歷史、民間故事，撰寫的《[[三國演義]]》[[章回小說]]，成為中國[[四大名著]]之一。";
        Assert.assertEquals(Utils.getPolysemy(content, 3).toString(), "[[曹魏, 蜀漢, 孫吳], [184年, 190年, 208年], [魏, 蜀, 吳]]");
    }


    @Test
    public void testGetPolysemy_three_measurements(){
        //三圍
        String content = "[[File:PostureFoundationGarments15fig12.png|thumb|upright|測量胸圍、腰圍、臀圍線條的緊身胸衣圖]]\\n'''三圍'''在人體[[量度]]上是指[[胸圍]]（上圍）、腰圍和臀圍（下圍）（即bust/waist/hips，通常简写为B/W/H，為男性則簡寫為C/W/H），也就是[[胸部]]、[[腰部]]及[[髋部|臀部]]的[[周長]]，量度單位為[[厘米]]或者[[英寸]]。三圍經常用於時裝界，通常-{只}-用於[[女性]]身上<ref>{{Cite web|last=Khamsi |first=Roxanne |title=The hourglass figure is truly timeless |publisher=NewScientist.com news service |date=2007-01-10 |url=https://www.newscientist.com/article/dn10927}}</ref>。\\n\\n== 區別 ==\\n很多人誤會胸圍的數字越大，代表該人的[[乳房]][[罩杯]]越大。而事實上，罩杯決定是看上胸圍減下胸圍的差；三圍中的胸圍係指上胸圍，也就是以[[乳頭]]為圓周上的點所測出的長度，而下胸圍是指乳房基部的圍長。因此只看三圍中的數字是完全無法判斷罩杯大小。\\n\\n==相關資料==\\n{{reflist}}\\n\\n== 參見 ==\\n*[[身體比例]]\\n*[[身高]]\\n*[[人体测量学]]\\n*{{le|體態吸引力|Physical attractiveness}}\\n*{{le|女性身材|Female body shape}}\\n*[[頭身]]\\n*[[腰臀比]]\\n{{Life-stub}}\\n\\n[[Category:度量]]\\n[[Category:解剖学]]";
        Assert.assertEquals(Utils.getPolysemy(content, 3).toString(), "[[胸圍, 腰圍, 臀圍]]");
    }

    @Test
    public void testIsChinesePunctuation(){
        char[] arr = {'、','。','？'};
        for(char c: arr){
            Assert.assertTrue(Utils.isChinesePunctuation(c));
        }

        char[] arr2 = {'1','a','我'};
        for(char c: arr2){
            Assert.assertFalse(Utils.isChinesePunctuation(c));
        }
    }

    @Test
    public void testIfNextPunctuationIsComma(){
        Assert.assertTrue(Utils.ifNextPunctuationIsComma("123、556", 0));
        Assert.assertTrue(Utils.ifNextPunctuationIsComma("123、556", 3));
        Assert.assertFalse(Utils.ifNextPunctuationIsComma("123、556", 4));
        Assert.assertFalse(Utils.ifNextPunctuationIsComma("123。56、5566.", 3));
    }
}