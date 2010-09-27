package org.daphnia.test;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class MainTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        ServicePeer sp = new ServicePeer("testService", new TestService());
//        System.out.println(sp.send("sitty()"));
        
/*        Date d = new Date();
        System.out.println(new JSONSerializer().transform(new DT(), Date.class).serialize(new Datter()));
        
        Date s = (Date) new JSONDeserializer().use(Date.class, new DT()).deserialize("{\"class\":\"org.daphnia.test.MainTest$Datter\",\"date\":\"2010-09-27T12:39:34.027Z\",\"name\":\"Sitty\"}");
        System.out.println(s);*/
        
        Map<String, Object> mapA = new HashMap<String, Object>();
        Map<String, Object> mapB = new HashMap<String, Object>();
        mapA.put("name", "Name");
        mapA.put("b", mapB);
        mapB.put("text", "Name");
        mapB.put("value", "Value");
        
        new BeanMap(new A()).putAll(mapA);
    }
    
    public static class Datter {
        private @Getter @Setter Date date = new Date();
        private @Getter @Setter String name = "Sitty";
    }
    
    public static class B {
        private @Getter @Setter String text;
        private @Getter @Setter String value;
    }

    public static class A {
        private @Getter @Setter String name;
        private @Getter @Setter B b;
    }
    
    public static class DT extends AbstractTransformer implements ObjectFactory {
        SimpleDateFormat sdf = new SimpleDateFormat("\"yyyy-MM-dd'T'hh:mm:ss.SSS'Z'\"");

        @Override
        public Object instantiate(ObjectBinder ob, Object o, Type t, Class cls) {
            try {
                return sdf.parse((String) o);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public void transform(Object o) {
            getContext().write(sdf.format(o));
        }
        
    }

}
