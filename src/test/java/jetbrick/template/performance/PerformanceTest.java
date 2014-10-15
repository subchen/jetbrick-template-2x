package jetbrick.template.performance;

import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import jetbrick.template.*;
import jetbrick.util.ExceptionUtils;

public class PerformanceTest {
    static boolean WAIT = true;
    static int warm = 20000;
    static int loop = 500000;

    public static void main(String[] args) throws Throwable {
        Properties config = new Properties();
        config.setProperty(JetConfig.IMPORT_CLASSES, Model.class.getName());
        JetEngine engine = JetEngine.create(config);

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("outputEncoding", "utf-8");
        context.put("items", Model.dummyItems());

        System.out.println("warming ....");
        for (int i = 0; i < warm; i++) {
            eval(engine, context);
        }

        Thread.sleep(2000);

        System.out.println("wait to start ....");
        while (WAIT) {
            if (System.in.read() == 10) break;
        }
        System.out.println("starting ....");

        long ts = System.nanoTime();
        for (int i = 0; i < loop; i++) {
            eval(engine, context);
        }
        ts = (System.nanoTime() - ts) / 1000000;
        long tps = loop * 1000 / ts;
        System.out.println("time = " + ts + " ms, tps = " + tps);

        while (WAIT) {
            if (System.in.read() == 10) break;
        }
    }

    protected static void eval(JetEngine engine, Map<String, Object> context) {
        JetTemplate template = engine.getTemplate("/jetbrick/template/performance/template.jetx");

        Writer out = new StringWriter();
        try {
            template.render(context, out);
            //System.out.println(out.toString());
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

}
