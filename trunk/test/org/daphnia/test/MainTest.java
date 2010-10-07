package org.daphnia.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import org.daphnia.web.ServicePeer;

public class MainTest {

    public List<String> list;

    public List<String> get() {
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(MainTest.class.getResourceAsStream("/org/daphnia/test/test.json")));
            StringWriter sw = new StringWriter();
            String s;
            while ((s = in.readLine()) != null)
                sw.write(s);
            
            ServicePeer sp = new ServicePeer();
            sp.setName("testService");
            sp.setService(new TestService());
            System.out.println(sp.send("setTest(" + sw.toString() + ")"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
