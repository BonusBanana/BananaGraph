package bananagraph.papayas;

import bananagraph.pineapples.SchemaToken;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.*;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

public class SchemaTokenizerTest {


    @Test
    public void parseTestSchema1() {

        String schema = "testSchema1.graphql";

        Stream<String> input = loadResource(schema);

        Collection<SchemaToken> tokens = SchemaTokenizer.readTokens(input).collect(Collectors.toList());

        assertNotNull(tokens);
        assertEquals(7, tokens.size());

        assertEquals(normalizeSpace(loadResource(schema).collect(Collectors.joining("\n"))),
                normalizeSpace(SchemaTokenizer.writeTokens(tokens.stream()))
        );


    }

    @Test
    public void parseTestSchema2() {

        String schema = "testSchema2.graphql";

        Stream<String> input = loadResource(schema);

        Collection<SchemaToken> tokens = SchemaTokenizer.readTokens(input).collect(Collectors.toList());

        assertNotNull(tokens);
        assertEquals(7, tokens.size());

        assertEquals(normalizeSpace(loadResource(schema).collect(Collectors.joining("\n"))),
                normalizeSpace(SchemaTokenizer.writeTokens(tokens.stream()))
        );

    }

    @Test
    public void parseTestSchema3() {

        String schema = "testSchema3.graphql";

        Stream<String> input = loadResource(schema);

        Collection<SchemaToken> tokens = SchemaTokenizer.readTokens(input).collect(Collectors.toList());

        assertNotNull(tokens);
        assertEquals(11, tokens.size());

        assertEquals(normalizeSpace(loadResource(schema).collect(Collectors.joining("\n"))),
                normalizeSpace(SchemaTokenizer.writeTokens(tokens.stream()))
        );

    }


    public static Stream<String> loadResource(String fileName) {

        try {
            return Files.lines(Paths.get(SchemaTokenizerTest.class.getClassLoader()
                    .getResource(fileName).toURI()));
        } catch (URISyntaxException | IOException e) {
            fail();
        }
        return Stream.empty();
    }

}
