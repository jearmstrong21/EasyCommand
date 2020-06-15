package p0nki.easycommand;

import org.junit.Test;
import p0nki.easycommand.utils.Optional;

@SuppressWarnings("unused")
public class CommandDispatcherTests {

    // TODO optional arguments

    private void run(CommandDispatcher dispatcher, Object source, String str) {
        Optional<String> err = dispatcher.run(source, str);
        if (err.isPresent()) System.out.println("ERR " + err.get());
    }

    // expected output:
    //
    //5: [1, 2, 3, 4, 5]
    //CharSequence str
    //Str str a
    //INSTR str
    //ININT 5
    //returnSource 233
    //returnSource null
    //returnSource p0nki.easycommand.utils.Optional@998c
    //@everyone
    //testGreedy no args
    //testGreedy no args
    //testGreedy arg=ih
    //testGreedy arg=a b
    //testSource 3
    //testRequirement 5
    //ERR Expected 5, got 6

    @Test
    public void test() {
        CommandDispatcher dispatcher = new CommandDispatcher();
        dispatcher.addPrimitives();
        dispatcher.createCog(new TestCog(), TestCog.class);
        run(dispatcher, null, "testArray 1 2 3 4 5");
        run(dispatcher, null, "testCharSequence str");
        run(dispatcher, null, "testGreedyString str a");
        run(dispatcher, null, "overlap str");
        run(dispatcher, null, "overlap 5");
        run(dispatcher, "233", "returnSource");
        run(dispatcher, null, "returnSource");
        run(dispatcher, Optional.empty(), "returnSource");
        run(dispatcher, null, "echo @everyone");
        run(dispatcher, null, "testGreedy");
        run(dispatcher, null, "testGreedy ");
        run(dispatcher, null, "testGreedy ih");
        run(dispatcher, null, "testGreedy a b");
        run(dispatcher, 3, "testSource");
        run(dispatcher, 5, "testRequirement");
        run(dispatcher, 6, "testRequirement");
    }

}
