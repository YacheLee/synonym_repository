package uk.ac.warwick;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.ac.warwick.exceptions.ThisPageDoesNotExist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {
    final static String WIKI_API_PREFIX = "https://zh.wikipedia.org/w/api.php";
    final static RestTemplate restTemplate = new RestTemplate();

    public static String getPageContent(String title) throws ThisPageDoesNotExist {
        String url = WIKI_API_PREFIX+"?action=query&prop=revisions&rvlimit=1&rvprop=content&format=json&titles=" + title;
        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode pagesNode = forEntity.getBody().get("query").get("pages");
        if(pagesNode.has("-1") || !pagesNode.iterator().hasNext())
            throw new ThisPageDoesNotExist();

        return pagesNode.iterator().next().get("revisions").elements().next().get("*").asText();
    }

    public static boolean isRedirected(String title) throws ThisPageDoesNotExist {
        String url = WIKI_API_PREFIX+"?action=query&format=json&redirects&titles=" + title;
        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        return forEntity.getBody().get("query").has("redirects");
    }

    public static String getRedirectTitle(String title) {
        String url = WIKI_API_PREFIX+"?action=query&format=json&redirects&titles=" + title;
        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode queryNode = forEntity.getBody().get("query");
        if(!queryNode.has("redirects"))
            return title;

        return queryNode.get("redirects").elements().next().get("to").asText();
    }

    public static List<String> getSynonyms(String title) throws ThisPageDoesNotExist {
        List<String> res = new ArrayList();
        if(!isRedirected(title)){
            String url = WIKI_API_PREFIX+"?action=query&prop=redirects&format=json&titles=" + title;
            ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
            Iterator<JsonNode> redirects = forEntity.getBody().get("query").get("pages").elements().next().get("redirects").elements();
            while(redirects.hasNext()){
                ObjectNode redirect = (ObjectNode)redirects.next();
                res.add(redirect.get("title").asText());
            }
            return res;
        }
        else{
            String redirectTitle = getRedirectTitle(title);
            List<String> redirects = getSynonyms(redirectTitle);
            redirects.add(redirectTitle);
            return redirects;
        }
    }
}