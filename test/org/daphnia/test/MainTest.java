package org.daphnia.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class MainTest {

    public List<String> list;
    public List<String> get() {
        return null;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
//        ServicePeer sp = new ServicePeer("testService", new TestService());
//        System.out.println(sp.send("sitty()"));

        try {
            System.out.println(Modifier.isAbstract(AbstractTransformer.class.getModifiers()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
