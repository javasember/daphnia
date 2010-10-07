package org.daphnia.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.daphnia.json.Parser;
import org.daphnia.json.Serializer;

public class MainTest {

    public List<String> list;

    public List<String> get() {
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // ServicePeer sp = new ServicePeer("testService", new TestService());
        // System.out.println(sp.send("sitty()"));

        try {
            InputStream in = MainTest.class.getResourceAsStream("/org/daphnia/test/test.json");
            Object o = new Parser().map(new InputStreamReader(in), Test.class);
            new Serializer().serialize(new PrintWriter(System.out), o);
/*            Class c = Test.class.getField("members").getType();
            Array.newInstance(c.getComponentType(), 0);
            System.out.println(Array.newInstance(c.getComponentType(), 0));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Test {
        private @Getter @Setter Object name;
        private @Getter @Setter String version;
        private @Getter @Setter Test2[] members;
    }

    public static class Test2 {
        private @Getter @Setter String name;
        private @Getter @Setter Integer age;
    }

}
