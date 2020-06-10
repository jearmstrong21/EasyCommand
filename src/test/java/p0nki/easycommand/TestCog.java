package p0nki.easycommand;

import p0nki.easycommand.annotations.*;
import p0nki.easycommand.arguments.PrimitiveParserFactory;

@CommandCog("test")
@SuppressWarnings("unused")
public class TestCog {

    @Command
    public void acceptSource(
            @Source String source,
            @Argument(name = "my_argument") int integer,
            @Argument(name = "my_str", modifiers = {PrimitiveParserFactory.MODIFIER_GREEDY_STRING}) String str) {
        System.out.println("acceptSource CALLED WITH SOURCE " + source + " AND my_argument " + integer + " AND my_str " + str);
    }

    @Command(literals = {@Literal({"first1", "first2"}), @Literal({"second1", "second2"}), @Literal("third")}, names = {"name1", "name2"})
    public void noSource(
            @Argument(name = "i1") Integer i1,
            @Argument(name = "v1") String v1,
            @Argument(name = "v2") String v2,
            @Argument(name = "v3") @GreedyString String v3) {
        System.out.println("noSource called with i1=" + i1 + " v1=" + v1 + " v2=" + v2 + " v3=" + v3);
    }

}
