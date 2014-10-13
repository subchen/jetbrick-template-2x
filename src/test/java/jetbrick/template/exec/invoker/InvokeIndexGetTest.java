package jetbrick.template.exec.invoker;

import java.util.*;
import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.template.runtime.InterpretException;
import org.junit.*;

public class InvokeIndexGetTest extends AbstractJetxSourceTest {
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

    @Test(expected = InterpretException.class)
    public void testArray_args_error() {
        eval("${array['a']}", ctx);
    }

    @Test
    public void testList() {
        Assert.assertEquals("aa", eval("${list[0]}", ctx));
        Assert.assertEquals("bb", eval("${list[index]}", ctx));
    }

    @Test(expected = InterpretException.class)
    public void testList_args_error() {
        eval("${list['a']}", ctx);
    }

    @Test
    public void testMap() {
        Assert.assertEquals("aa", eval("${map['aa']}", ctx));
        Assert.assertEquals("12", eval("${map[key]}", ctx));
    }

    @Test(expected = InterpretException.class)
    public void testMap_args_error() {
        eval("${map[1]}", ctx);
    }
}
