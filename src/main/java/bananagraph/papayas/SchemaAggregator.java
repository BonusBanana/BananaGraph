package bananagraph.papayas;

import bananagraph.pineapples.Keyword;
import bananagraph.pineapples.SchemaToken;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SchemaAggregator {


    private Map<Keyword, Map<String, SchemaToken>> schemaDefinitions;

    public SchemaAggregator() {
        schemaDefinitions = new ConcurrentHashMap<>();
    }

    public void add(SchemaToken token) {

        Keyword keyword = token.getKeyword();

        Map<String, SchemaToken> mapForKey = schemaDefinitions.getOrDefault(token.getKeyword(),
                new TreeMap<>());

        if (Keyword.SCHEMA.equals(keyword)) {
            mapForKey.merge(token.getKeyword().toString(), token, SchemaToken::merge);
        } else if (Keyword.SCALAR.equals(keyword)) {
            mapForKey.put(token.getName(), token);
        } else {
            mapForKey.merge(token.getName(), token, SchemaToken::merge);
        }

        schemaDefinitions.put(token.getKeyword(), mapForKey);
    }

    public String fullSchema() {
        return schemaDefinitions.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().getOrdering()))
                .map(Map.Entry::getValue)
                .map(Map::values)
                .flatMap(Collection::stream)
                .map(SchemaToken::toGraphQL)
                .collect(Collectors.joining("\n"));
    }


}
