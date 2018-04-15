package bananagraph.papayas;

import bananagraph.pineapples.SchemaToken;
import org.junit.Test;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bananagraph.papayas.SchemaTokenizerTest.loadResource;
import static java.util.Arrays.stream;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;


public class SchemaAggrgatorTest {


    @Test

    public void joinSchemasTest() {

        String schema1 = "testSchema1.graphql";
        String schema2 = "testSchema2.graphql";
        String schema3 = "testSchema3.graphql";

        Stream<String> input1 = loadResource(schema1);
        Stream<String> input2 = loadResource(schema2);
        Stream<String> input3 = loadResource(schema3);

        SchemaAggregator schemaAggregator = new SchemaAggregator();

        SchemaTokenizer.readTokens(input1).forEach(schemaAggregator::add);
        SchemaTokenizer.readTokens(input2).forEach(schemaAggregator::add);
        SchemaTokenizer.readTokens(input3).forEach(schemaAggregator::add);

        String combinedSchema = schemaAggregator.fullSchema();

        assertEquals(normalizeSpace(loadResource("combinedSchema.graphql").collect(Collectors.joining("\n"))),
                normalizeSpace(combinedSchema));

        Collection<SchemaToken> tokens = SchemaTokenizer.readTokens(
                stream(combinedSchema.split("\\r?\\n")))
                .collect(Collectors.toList());

        assertNotNull(tokens);
        assertEquals(21, tokens.size());
    }
}
