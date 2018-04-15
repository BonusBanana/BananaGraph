package bananagraph.pineapples;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaBlockElement implements Comparable<SchemaBlockElement> {

    private List<String> comments;
    private String body;

    public SchemaBlockElement() {
        this.comments = new ArrayList<>();
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String toGraphQL() {
        return Stream.concat(comments.stream(), Stream.of(body))
                .map(s -> "  " + s)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public int compareTo(SchemaBlockElement o) {
        return this.body.compareTo(o.body);
    }
}
