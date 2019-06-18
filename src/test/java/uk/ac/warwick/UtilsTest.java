package uk.ac.warwick;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class UtilsTest {
    @Test
    public void getDocumentTest() throws IOException {
        Document document = Utils.getDocument("https://www.wikipedia.org");
        Assert.assertEquals(document.select("title").text(), "Wikipedia");
    }

    @Test
    public void isRedirectionTest() throws IOException {
        Assert.assertTrue(Utils.isRedirection("台大"));
        Assert.assertFalse(Utils.isRedirection("國立臺灣大學"));
    }
}
