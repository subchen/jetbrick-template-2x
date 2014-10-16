package jetbrick.template.parser.ast;

import org.junit.Assert;
import org.junit.Test;

public class ALUGetNumberTypeTest {

    @Test
    public void testNumberType() {
        Assert.assertEquals(ALU.BYTE, ALU.getNumberType(Byte.class));
        Assert.assertEquals(ALU.SHORT, ALU.getNumberType(Short.class));
        Assert.assertEquals(ALU.INTEGER, ALU.getNumberType(Integer.class));
        Assert.assertEquals(ALU.LONG, ALU.getNumberType(Long.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Float.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Double.class));
        Assert.assertEquals(ALU.NaN, ALU.getNumberType(Object.class));
        Assert.assertEquals(ALU.NaN, ALU.getNumberType(String.class));
    }

    @Test
    public void testNumberType_byte() {
        Assert.assertEquals(ALU.BYTE, ALU.getNumberType(Byte.class, Byte.class));
        Assert.assertEquals(ALU.SHORT, ALU.getNumberType(Byte.class, Short.class));
        Assert.assertEquals(ALU.INTEGER, ALU.getNumberType(Byte.class, Integer.class));
        Assert.assertEquals(ALU.LONG, ALU.getNumberType(Byte.class, Long.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Byte.class, Float.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Byte.class, Double.class));
        Assert.assertEquals(ALU.NaN, ALU.getNumberType(Byte.class, Object.class));
    }

    @Test
    public void testNumberType_short() {
        Assert.assertEquals(ALU.SHORT, ALU.getNumberType(Short.class, Byte.class));
        Assert.assertEquals(ALU.SHORT, ALU.getNumberType(Short.class, Short.class));
        Assert.assertEquals(ALU.INTEGER, ALU.getNumberType(Short.class, Integer.class));
        Assert.assertEquals(ALU.LONG, ALU.getNumberType(Short.class, Long.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Short.class, Float.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Short.class, Double.class));
        Assert.assertEquals(ALU.NaN, ALU.getNumberType(Short.class, Object.class));
    }

    @Test
    public void testNumberType_int() {
        Assert.assertEquals(ALU.INTEGER, ALU.getNumberType(Integer.class, Byte.class));
        Assert.assertEquals(ALU.INTEGER, ALU.getNumberType(Integer.class, Short.class));
        Assert.assertEquals(ALU.INTEGER, ALU.getNumberType(Integer.class, Integer.class));
        Assert.assertEquals(ALU.LONG, ALU.getNumberType(Integer.class, Long.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Integer.class, Float.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Integer.class, Double.class));
        Assert.assertEquals(ALU.NaN, ALU.getNumberType(Integer.class, Object.class));
    }

    @Test
    public void testNumberType_long() {
        Assert.assertEquals(ALU.LONG, ALU.getNumberType(Long.class, Byte.class));
        Assert.assertEquals(ALU.LONG, ALU.getNumberType(Long.class, Short.class));
        Assert.assertEquals(ALU.LONG, ALU.getNumberType(Long.class, Integer.class));
        Assert.assertEquals(ALU.LONG, ALU.getNumberType(Long.class, Long.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Long.class, Float.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Long.class, Double.class));
        Assert.assertEquals(ALU.NaN, ALU.getNumberType(Long.class, Object.class));
    }

    @Test
    public void testNumberType_float() {
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Float.class, Byte.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Float.class, Short.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Float.class, Integer.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Float.class, Long.class));
        Assert.assertEquals(ALU.FLOAT, ALU.getNumberType(Float.class, Float.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Float.class, Double.class));
        Assert.assertEquals(ALU.NaN, ALU.getNumberType(Float.class, Object.class));
    }

    @Test
    public void testNumberType_double() {
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Double.class, Byte.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Double.class, Short.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Double.class, Integer.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Double.class, Long.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Double.class, Float.class));
        Assert.assertEquals(ALU.DOUBLE, ALU.getNumberType(Double.class, Double.class));
        Assert.assertEquals(ALU.NaN, ALU.getNumberType(Double.class, Object.class));
    }
}
