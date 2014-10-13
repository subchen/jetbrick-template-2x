package jetbrick.template.parser.ast;

import org.junit.Assert;
import org.junit.Test;

public class ALUTest {
    byte b = 1;
    short s = 1;
    int i = 1;
    long l = 1;
    float f = 1;
    double d = 1;

    @Test
    public void testPlus() {
        Assert.assertEquals(Integer.class, ALU.plus(b, b).getClass());
        Assert.assertEquals(Integer.class, ALU.plus(b, s).getClass());
        Assert.assertEquals(Integer.class, ALU.plus(b, i).getClass());
        Assert.assertEquals(Long.class, ALU.plus(b, l).getClass());
        Assert.assertEquals(Float.class, ALU.plus(b, f).getClass());
        Assert.assertEquals(Double.class, ALU.plus(b, d).getClass());
    }

    @Test
    public void testMinis() {
        Assert.assertEquals(Integer.class, ALU.minus(b, b).getClass());
        Assert.assertEquals(Integer.class, ALU.minus(b, s).getClass());
        Assert.assertEquals(Integer.class, ALU.minus(b, i).getClass());
        Assert.assertEquals(Long.class, ALU.minus(b, l).getClass());
        Assert.assertEquals(Float.class, ALU.minus(b, f).getClass());
        Assert.assertEquals(Double.class, ALU.minus(b, d).getClass());
    }

    @Test
    public void testMul() {
        Assert.assertEquals(Integer.class, ALU.mul(b, b).getClass());
        Assert.assertEquals(Integer.class, ALU.mul(b, s).getClass());
        Assert.assertEquals(Integer.class, ALU.mul(b, i).getClass());
        Assert.assertEquals(Long.class, ALU.mul(b, l).getClass());
        Assert.assertEquals(Float.class, ALU.mul(b, f).getClass());
        Assert.assertEquals(Double.class, ALU.mul(b, d).getClass());
    }

    @Test
    public void testDiv() {
        Assert.assertEquals(Integer.class, ALU.div(b, b).getClass());
        Assert.assertEquals(Integer.class, ALU.div(b, s).getClass());
        Assert.assertEquals(Integer.class, ALU.div(b, i).getClass());
        Assert.assertEquals(Long.class, ALU.div(b, l).getClass());
        Assert.assertEquals(Float.class, ALU.div(b, f).getClass());
        Assert.assertEquals(Double.class, ALU.div(b, d).getClass());
    }

}
