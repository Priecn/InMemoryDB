package parser;

import java.util.Arrays;
import java.util.List;

public class Tokenizer {

    public List<String> tokenize(String query) {
        return Arrays.stream(query.split(" "))
                .map(String::trim).toList();
    }
}
