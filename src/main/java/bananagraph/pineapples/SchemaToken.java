package bananagraph.pineapples;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class SchemaToken {

    private Keyword keyword;
    private String name;
    private List<String> comments;
    private List<SchemaBlockElement> body;

    public SchemaToken() {
        this.comments = new ArrayList<>();
        this.body = new ArrayList<>();
    }

    public SchemaToken merge(SchemaToken schemaToken) {

        this.comments.addAll(schemaToken.getComments());

        Map<String, SchemaBlockElement> newBody = new TreeMap<>();

        Stream.concat(body.stream(), schemaToken.body.stream())
                .filter(b -> b.getBody() != null)
                .forEach(b -> newBody.merge(b.getBody(), b, (o1, o2) -> {
                            o1.getComments().addAll(o2.getComments());
                            return o1;
                        }
                ));

        this.body = new ArrayList<>(newBody.values());
        return this;
    }

    public String toGraphQL() {

        if (keyword.equals(Keyword.SCALAR)) {
            return keyword.name().toLowerCase() + " " + name;
        }

        return concat(

                concat(comments.stream(),

                        Stream.of(keyword.name().toLowerCase() + " " + (name != null ? (name + " ") : "") + "{")),

                concat(body.stream().filter(e -> e.getBody() != null).map(SchemaBlockElement::toGraphQL),

                        Stream.of("}\n"))

        ).collect(Collectors.joining("\n"));

    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<SchemaBlockElement> getBody() {
        return body;
    }

    public void setBody(List<SchemaBlockElement> body) {
        this.body = body;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
