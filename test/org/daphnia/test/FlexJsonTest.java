package org.daphnia.test;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.daphnia.web.Server;

import flexjson.JSONDeserializer;

public class FlexJsonTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
/*        Server s = new Server();
        s.setChannel("main");
        ServicePeer p = new ServicePeer();
        p.setName("sitty");
        s.addPeer("service", p);
        String json = new JSONSerializer().deepSerialize(new Object[] {1, 2, "3", s});
        System.out.println(json);*/
        String json = "[1,2,\"3\",{\"channel\":\"main\"}]";
        Object o = new JSONDeserializer<Object>().deserialize(json);
        List<?> l = (List<?>) o;
        BeanMap m = new BeanMap(new Server());
        m.putAll((Map) l.get(3));
        System.out.println(o);
    }

}
