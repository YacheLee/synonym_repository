package uk.ac.warwick;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.warwick.exceptions.ThisPageDoesNotExist;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    final static String WIKI_API_PREFIX = "https://zh.wikipedia.org/w/api.php";
    final static RestTemplate restTemplate = new RestTemplate();

    public static String getPageContent(String title) throws ThisPageDoesNotExist {
        String url = getUrlBase(title)
                .queryParam("prop", "revisions")
                .queryParam("rvlimit", "1")
                .queryParam("rvprop", "content")
                .build().toString();

        ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode pagesNode = forEntity.getBody().get("query").get("pages");
        if (pagesNode.has("-1") || !pagesNode.iterator().hasNext())
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
        if (!queryNode.has("redirects"))
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
        if (page.has("categories")) {
            Iterator<JsonNode> iterator = page.get("categories").iterator();
            while (iterator.hasNext()) {
                JsonNode categoryNode = iterator.next();
                categories.add(categoryNode.get("title").asText());
            }
        }
        return categories;
    }

    public static boolean isDisambiguousTerm(String title) {
        List<String> categories = getCategories(title);
        return categories.stream().anyMatch(s -> s.contains("消歧義"));
    }

    public static Set<String> getSynonyms(String keyword) throws ThisPageDoesNotExist {
        Set<String> res = new HashSet<>();
        res.add(keyword);

        if (isRedirected(keyword)) {
            String redirectTitle = getRedirectTitle(keyword);
            Set<String> redirects = getSynonyms(redirectTitle);
            redirects.add(redirectTitle);
            return redirects;
        } else if (isDisambiguousTerm(keyword)) {
            return getDisambiguousTerms(keyword);
        } else {
            String url = getUrlBase(keyword).queryParam("prop", "redirects").build().toString();
            ResponseEntity<JsonNode> forEntity = restTemplate.getForEntity(url, JsonNode.class);
            Iterator<JsonNode> pages = forEntity.getBody().get("query").get("pages").elements();
            if (pages.hasNext()) {
                JsonNode page = pages.next();
                if (page.has("redirects")) {
                    Iterator<JsonNode> redirects = page.get("redirects").elements();
                    while (redirects.hasNext()) {
                        ObjectNode redirect = (ObjectNode) redirects.next();
                        res.add(redirect.get("title").asText());
                    }
                }
            }
            return res;
        }
    }

    public static Set<List<String>> getPolysemy(String content, int n) {
        Set<List<String>> set = new HashSet();
        if(!content.isEmpty()){
            String insidePattern = ".*?";
            content = removeSquares(content);
            set.addAll(getPolysemy(content, "、" + insidePattern + "、", "、(" + insidePattern + ")、"));
            set.addAll(getPolysemy(content, "、" + insidePattern + "及", "、(" + insidePattern + ")及"));
            set.addAll(getPolysemy(content, "、" + insidePattern + "或", "、(" + insidePattern + ")或"));
        }
        return set;
    }

    public static Set<List<String>> getPolysemy(String content, String centralPattern, String replacedPattern) {
        Pattern pattern = Pattern.compile(centralPattern);
        Matcher matcher = pattern.matcher(content);

        Set res = new HashSet();
        while (matcher.find()) {
            List<String> polysemy = new ArrayList<>();

            int start = matcher.start();
            int end = matcher.end();
            String word = matcher.group()
                    .replaceAll(replacedPattern, "$1");

            if (isNextPunctuationIsComma(content, end)) {
                continue;
            }

            int length = word.length();
            word = removeSquares(word);
            if (word.length() > 7) continue;
            String preWord = content.substring(start - length, start);
            String nexWord = content.substring(end, end + length);

            polysemy.add(removeSquares(preWord));
            polysemy.add(word);
            polysemy.add(removeSquares(nexWord));

            res.add(polysemy);
        }
        return res;
    }

    private static String removeSquares(String txt) {
        return txt
                .replaceAll("\\[\\[(.*?)\\]\\]", "$1")
                .replaceAll("\\[(.*?)\\]", "$1")
                .replaceAll("「(.*?)」", "$1");
    }

    public static Set<String> getDisambiguousTerms(String title) throws ThisPageDoesNotExist {
        String content = getPageContent(title);
        String insidePattern = ".+";
        String completePattern = "\\[\\[" + insidePattern + "\\]\\]";
        Pattern pattern = Pattern.compile(completePattern);
        Matcher matcher = pattern.matcher(content);

        Set<String> list = new HashSet<>();
        while (matcher.find()) {
            list.add(removeSquares(matcher.group()));
        }
        return list;
    }

    private static UriComponentsBuilder getUrlBase(String title) {
        return UriComponentsBuilder.fromHttpUrl(WIKI_API_PREFIX)
                .queryParam("action", "query")
                .queryParam("format", "json")
                .queryParam("titles", title);
    }

    private static UriComponents getRedirectUriComponents(String title) {
        return getUrlBase(title)
                .queryParam("redirects", "")
                .build();
    }

    public static boolean isChinesePunctuation(char c){
        Pattern pattern = Pattern.compile("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]$");
        Matcher matcher = pattern.matcher(String.valueOf(c));
        return matcher.find();
    }

    public static boolean isNextPunctuationIsComma(String content, int start) {
        int n = content.length();
        char[] arr = content.toCharArray();
        while (start < n) {
            char c = arr[start];
            if (isChinesePunctuation(c)) {
                return c == '、';
            }
            start++;
        }
        return false;
    }
}