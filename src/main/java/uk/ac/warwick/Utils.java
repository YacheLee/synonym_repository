package uk.ac.warwick;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Utils {

    public static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public static boolean isRedirection(String keyword) throws IOException {
        Document document = getDocument("https://zh.wikipedia.org/w/index.php?title="+keyword+"&action=edit");
        String text = document.select("#wpTextbox1").text();
        return text.contains("#REDIRECT");
    }
}