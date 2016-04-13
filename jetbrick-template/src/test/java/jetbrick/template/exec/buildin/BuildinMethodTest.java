package jetbrick.template.exec.buildin;

import jetbrick.template.exec.AbstractJetxTest;
import jetbrick.template.runtime.InterpretException;
import jetbrick.util.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class BuildinMethodTest extends AbstractJetxTest {

    @Test
    public void testJson() {
        Assert.assertEquals("123", eval("${123.asJson()}"));
        Assert.assertEquals("false", eval("${false.asJson()}"));
        Assert.assertEquals("\"a\"", eval("${'a'.asJson()}"));
        Assert.assertEquals("[]", eval("${[].asJson()}"));
        Assert.assertEquals("[1,false]", eval("${[1,false].asJson()}"));
        Assert.assertEquals("{}", eval("${{}.asJson()}"));
        Assert.assertEquals("{\"a\":1}", eval("${{a:1}.asJson()}"));
    }

    @Test
    public void testFormatNumber() {
        Assert.assertEquals("123.00", eval("${123.format()}"));
        Assert.assertEquals("123.57", eval("${123.567.format()}"));
    }

    @Test
    public void testFormatDate() {
        Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put("d", DateUtils.parse("2014-01-01 12:00:00"));
        Assert.assertEquals("2014-01-01 12:00:00", eval("${d.format()}", ctx));
        Assert.assertEquals("140101", eval("${d.format('yyMMdd')}", ctx));
    }

    @Test
    public void testAsDate() {
        Assert.assertEquals("2014-01-01 12:00:00", eval("${'2014-01-01 12:00:00'.asDate().format()}"));
        Assert.assertEquals("2014-01-01 00:00:00", eval("${'2014-01-01'.asDate('yyyy-MM-dd').format()}"));
    }

    @Test
    public void testWord() {
        Assert.assertEquals("User", eval("${'user'.capitalize()}"));
        Assert.assertEquals("user_info", eval("${'UserInfo'.toUnderlineName()}"));
        Assert.assertEquals("userInfo", eval("${'user_info'.toCamelCase()}"));
        Assert.assertEquals("UserInfo", eval("${'user_info'.toCapitalizeCamelCase()}"));
    }

    @Test
    public void testEscapeString() {
        Assert.assertEquals("\\n", eval("${'\\n'.escapeJava()}"));
        Assert.assertEquals("\\n", eval("${'\\n'.escapeJavaScript()}"));
        Assert.assertEquals("&lt;&gt;", eval("${'<>'.escapeXml()}"));
        Assert.assertEquals("+", eval("${' '.escapeUrl()}"));
    }

    @Test
    public void testString() {
        // String::abbreviate
        Assert.assertEquals("123...", eval("${'1234567'.abbreviate(6)}"));
        Assert.assertEquals("1234567", eval("${'1234567'.abbreviate(7)}"));
        Assert.assertEquals("1234567", eval("${'1234567'.abbreviate(8)}"));
        Assert.assertEquals("1...", eval("${'1234567'.abbreviate(4)}"));

        // String::md5Hex
        Assert.assertEquals("1102ea8656d577d8f73fcc9e8cf9b182", eval("${'yingzhuo'.md5Hex()}"));
        Assert.assertEquals("1102ea8656d577d8f73fcc9e8cf9b182", eval("${'yingzhuo'.md5Hex('UTF-8')}"));

        // String::sha1Hex
        Assert.assertEquals("aa8109651bbe5e60870190d93be97880edf954b1", eval("${'yingzhuo'.sha1Hex()}"));
        Assert.assertEquals("aa8109651bbe5e60870190d93be97880edf954b1", eval("${'yingzhuo'.sha1Hex('UTF-8')}"));
    }

    @Test(expected = InterpretException.class)
    public void testStringWithException() {
        eval("${'1234567'.abbreviate(3)}");
    }

}
