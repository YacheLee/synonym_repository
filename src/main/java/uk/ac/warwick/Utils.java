package uk.ac.warwick;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    final static String WIKI_API_PREFIX = "https://zh.wikipedia.org/w/api.php";
    final static Pattern REDIRECT_PATTERN = Pattern.compile("\\#REDIRECT \\[\\[[\b\\W\\w\b]+\\]\\]");
    final static RestTemplate restTemplate = new RestTemplate();

    public static String getPageContent(String title) {
        String url = WIKI_API_PREFIX+"?action=query&prop=revisions&rvlimit=1&rvprop=content&format=json&titles=" + title;
        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        return forEntity.getBody().get("query").get("pages").elements().next().get("revisions").elements().next().get("*").asText();
    }

    public static boolean isRedirected(String title) {
        String pageContent = getPageContent(title);
        Matcher m1 = REDIRECT_PATTERN.matcher(pageContent);
        return m1.find();
    }

    public static String getRedirectTitle(String title) {
        String url = WIKI_API_PREFIX+"?action=query&format=json&redirects&titles=" + title;
        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode queryNode = forEntity.getBody().get("query");
        if(!queryNode.has("redirects"))
            return title;

        return queryNode.get("redirects").elements().next().get("to").asText();
    }
}