public class Pair<T, U> {
    public T first;
    public U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("Pair(%s %s)", first.toString(), second.toString());
    }
}
