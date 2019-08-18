package uk.ac.warwick;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.warwick.exceptions.ThisPageDoesNotExist;

import java.util.Set;

@RestController
public class SynonymController {

    @GetMapping("/api/synonym")
    public Set<String> getSynonyms(@RequestParam String keyword) throws ThisPageDoesNotExist {
        return Utils.getSynonyms(keyword);
    }
}