package org.daphnia.json;

import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanMap;
import org.daphnia.json.Serializer.ElementSerializer;

public class ObjectSerializer extends ElementSerializer {

    public ObjectSerializer(Serializer s) {
        super(s);
    }
    
    public boolean fits(Object o) {
        return (o != null);
    }

    public void serialize(Writer out, Object o) throws Exception {
        out.write('{');
        int i = 0;
//        PropertyDescriptor[] prop = Introspector.getBeanInfo(o.getClass()).getPropertyDescriptors();
        Map<?, ?> oo = new BeanMap(new Object());
        for (Object eo : new BeanMap(o).entrySet()) {
            Entry<?, ?> e = (Entry<?, ?>) eo;
            if (!oo.containsKey(e.getKey()))
            {
                if (i > 0)
                    out.write(',');
                out.write('"');
                out.write(e.getKey().toString());
                out.write("\" : ");
                super.serialize(out, e.getValue());
                i++;
            }
        }
        out.write('}');
    }
}
