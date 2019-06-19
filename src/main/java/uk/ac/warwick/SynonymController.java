package uk.ac.warwick;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SynonymController {

    @GetMapping("/synonym")
    public List<String> getSynonyms(@RequestParam String title){
        return Utils.getSynonyms(title);
    }
}
