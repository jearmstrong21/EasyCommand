package p0nki.easycommand;

import java.util.function.Predicate;

public class CommandReader {

    private final String buffer;
    private int index;

    public CommandReader(String buffer) {
        this.buffer = buffer;
        index = 0;
    }

    public String readWord() {
        return readWhile(ch -> ch != ' ');
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String readAll() {
        return readWhile(ch -> true);
    }

    public boolean canRead() {
        return index < buffer.length();
    }

    private String readWhile(Predicate<Character> predicate) {
        StringBuilder str = new StringBuilder();
        char ch;
        while (canRead() && predicate.test(ch = next())) {
            str.append(ch);
        }
        return str.toString().trim();
    }

    public char next() {
        return buffer.charAt(index++);
    }

}
