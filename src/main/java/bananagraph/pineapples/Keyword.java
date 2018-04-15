package bananagraph.pineapples;

public enum Keyword {

    SCALAR(1), SCHEMA(2), TYPE(3), INPUT(4), ENUM(5);

    private int ordering;

    Keyword(int ordering) {
        this.ordering = ordering;
    }

    public int getOrdering() {
        return ordering;
    }
}
