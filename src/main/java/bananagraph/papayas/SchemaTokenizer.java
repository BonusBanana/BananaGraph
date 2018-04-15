package bananagraph.papayas;

import bananagraph.pineapples.Keyword;
import bananagraph.pineapples.SchemaBlockElement;
import bananagraph.pineapples.SchemaToken;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaTokenizer {


    /**
     * @param schemaSource line by line GraphQL schema source
     * @return tokenized stream
     */
    public static Stream<SchemaToken> readTokens(Stream<String> schemaSource) {

        AtomicReference<SchemaToken> token = new AtomicReference<>();

        return schemaSource
                .map(StringUtils::normalizeSpace)
                .map(nextLine -> extractToken(token, nextLine))
                .filter(Optional::isPresent)
                .map(Optional::get);

    }

    /**
     * @param schemaTokens
     * @return written GraphQL schema
     */
    public static String writeTokens(Stream<SchemaToken> schemaTokens) {
        return schemaTokens.map(SchemaToken::toGraphQL).collect(Collectors.joining("\n"));
    }


    private static Optional<SchemaToken> extractToken(AtomicReference<SchemaToken> reference, String nextLine) {

        if (nextLine.isEmpty()) {
            return Optional.empty();
        }

        SchemaToken schemaToken = reference.get();
        Optional<SchemaToken> output;

        if (schemaToken == null) {
            schemaToken = new SchemaToken();
            // gut ?
            output = definitionLine(schemaToken, nextLine);
        } else {
            output = blockLine(schemaToken, nextLine);
        }

        if (output.isPresent()) {
            reference.set(null);
        } else {
            reference.set(schemaToken);
        }

        return output;
    }

    private static Optional<SchemaToken> definitionLine(SchemaToken schemaToken, String line) {

        String[] input = line.split(" ");

        Keyword keyword = Keyword.valueOf(input[0].toUpperCase());

        schemaToken.setKeyword(keyword);

        if (Keyword.SCHEMA.equals(keyword)) {
            return Optional.empty();
        }

        if (Keyword.SCALAR.equals(keyword)) {
            schemaToken.setName(input[1]);
            return Optional.of(schemaToken);
        }

        schemaToken.setName(input[1]);

        return Optional.empty();
    }

    private static Optional<SchemaToken> blockLine(SchemaToken schemaToken, String line) {

        String[] input = line.split(" ");

        if ("}".equals(input[input.length - 1])) {
            return Optional.of(schemaToken);
        }

        List<SchemaBlockElement> blockElements = schemaToken.getBody();
        if (blockElements.isEmpty()) {
            blockElements.add(new SchemaBlockElement());
        }

        if (line.startsWith("#")) {
            schemaToken.getBody().get(schemaToken.getBody().size() - 1).getComments().add(line);
            return Optional.empty();

        } else {
            schemaToken.getBody().get(schemaToken.getBody().size() - 1).setBody(line);
            blockElements.add(new SchemaBlockElement());
            return Optional.empty();
        }
    }

}
