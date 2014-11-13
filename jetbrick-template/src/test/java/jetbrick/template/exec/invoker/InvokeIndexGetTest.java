package jetbrick.template.exec.invoker;

import java.util.*;
import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.runtime.InterpretException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("serial")
public class InvokeIndexGetTest extends AbstractJetxTest {
    private static Map<String, Object> ctx;

    static {
        ctx = new HashMap<String, Object>();
        ctx.put("index", 1);
        ctx.put("key", "bb");

        ctx.put("array", new Object[] { "aa", 12 });
        ctx.put("list", Arrays.asList("aa", "bb"));
        ctx.put("map", new HashMap<String, Object>() {
            {
                put("aa", "aa");
                put("bb", 12);
            }
        });
    }

    @Test
    public void testArray() {
        Assert.assertEquals("aa", eval("${array[0]}", ctx));
        Assert.assertEquals("12", eval("${array[index]}", ctx));
    }

    @Test
    public void testArray_args_error() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.containsString("undefined for the argument type(s)"));

        eval("${array['a']}", ctx);
    }

    @Test
    public void testList() {
        Assert.assertEquals("aa", eval("${list[0]}", ctx));
        Assert.assertEquals("bb", eval("${list[index]}", ctx));
    }

    @Test
    public void testList_args_error() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.containsString("undefined for the argument type(s)"));

        eval("${list['a']}", ctx);
    }

    @Test
    public void testMap() {
        Assert.assertEquals("aa", eval("${map['aa']}", ctx));
        Assert.assertEquals("12", eval("${map[key]}", ctx));
    }

    @Test
    public void testMap_args_error() {
        thrown.expect(InterpretException.class);
        thrown.expectMessage(CoreMatchers.containsString("undefined for the argument type(s)"));

        eval("${map[1]}", ctx);
    }
}
