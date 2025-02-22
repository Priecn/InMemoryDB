package parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Validator {

    Set<String> validOperations = new HashSet<>(Arrays.asList("CREATE", "INSERT", "UPDATE", "DELETE"));

    public boolean isValid(List<String> tokens) {
        return !tokens.isEmpty() && validOperations.contains(tokens.getFirst().toUpperCase());
    }
}
