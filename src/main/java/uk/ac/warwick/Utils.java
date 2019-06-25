package uk.ac.warwick;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.warwick.exceptions.ThisPageDoesNotExist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {
    final static String WIKI_API_PREFIX = "https://zh.wikipedia.org/w/api.php";
    final static RestTemplate restTemplate = new RestTemplate();

    public static String getPageContent(String title) throws ThisPageDoesNotExist {
        String url = getUrlBase(title)
                .queryParam("prop","revisions")
                .queryParam("rvlimit","1")
                .queryParam("rvprop","content")
                .build().toString();

        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode pagesNode = forEntity.getBody().get("query").get("pages");
        if(pagesNode.has("-1") || !pagesNode.iterator().hasNext())
            throw new ThisPageDoesNotExist();

        return pagesNode.iterator().next().get("revisions").elements().next().get("*").asText();
    }

    public static boolean isRedirected(String title) throws ThisPageDoesNotExist {
        String url = getRedirectUriComponents(title).toString();
        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        return forEntity.getBody().get("query").has("redirects");
    }

    public static String getRedirectTitle(String title) {
        String url = getRedirectUriComponents(title).toString();

        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode queryNode = forEntity.getBody().get("query");
        if(!queryNode.has("redirects"))
            return title;

        return queryNode.get("redirects").elements().next().get("to").asText();
    }

    public static List<String> getCategories(String title) {
        String url = getUrlBase(title)
                .queryParam("prop", "categories")
                .queryParam("rvlimit", "1")
                .queryParam("rvprop", "content").build().toString();

        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode queryNode = forEntity.getBody().get("query");
        JsonNode page = queryNode.get("pages").elements().next();

        List<String> categories = new ArrayList<>();
        if(page.has("categories")){
            Iterator<JsonNode> iterator = page.get("categories").iterator();
            while(iterator.hasNext()){
                JsonNode categoryNode = iterator.next();
                categories.add(categoryNode.get("title").asText());
            }
        }
        return categories;
    }

    public static boolean isDisambiguousTerm(String title) throws ThisPageDoesNotExist {
        List<String> categories = getCategories(title);
        return categories.stream().anyMatch(s -> s.contains("消歧義"));
    }

    public static List<String> getSynonyms(String title) throws ThisPageDoesNotExist {
        List<String> res = new ArrayList();
        if(isRedirected(title)){
            String redirectTitle = getRedirectTitle(title);
            List<String> redirects = getSynonyms(redirectTitle);
            redirects.add(redirectTitle);
            return redirects;
        }
        else{
            String url = getUrlBase(title).queryParam("prop", "redirects").build().toString();
            ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
            Iterator<JsonNode> pages = forEntity.getBody().get("query").get("pages").elements();
            if (pages.hasNext()) {
                JsonNode page = pages.next();
                if(page.has("redirects")){
                    Iterator<JsonNode> redirects = page.get("redirects").elements();
                    while(redirects.hasNext()){
                        ObjectNode redirect = (ObjectNode)redirects.next();
                        res.add(redirect.get("title").asText());
                    }
                }
            }
            return res;
        }
    }

    private static UriComponentsBuilder getUrlBase(String title){
        return UriComponentsBuilder.fromHttpUrl(WIKI_API_PREFIX)
                .queryParam("action","query")
                .queryParam("format","json")
                .queryParam("titles",title);
    }

    private static UriComponents getRedirectUriComponents(String title){
        return getUrlBase(title)
                .queryParam("redirects","")
                .build();
    }
}