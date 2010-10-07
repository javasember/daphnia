package org.daphnia.json;

import java.io.Writer;
import java.util.Collection;

import org.daphnia.json.Serializer.ElementSerializer;

public class ArraySerializer extends ElementSerializer {

    public ArraySerializer(Serializer s) {
        super(s);
    }
    
    public boolean fits(Object o) {
        return (o != null && (o instanceof Collection || o.getClass().isArray()));
    }
    
    public void serialize(Writer out, Object o) throws Exception {
        out.write('[');
        if (o instanceof Collection)
            serializeCollection(out, (Collection<?>) o);
        else
            serializeArray(out, (Object[]) o);
        out.write(']');
    }

    public void serializeCollection(Writer out, Collection<?> c) throws Exception {
        int i = 0;
        for (Object o : c) {
            if (i > 0)
                out.write(',');
            super.serialize(out, o);
            i++;
        }
    }

    public void serializeArray(Writer out, Object[] a) throws Exception {
        int i = 0;
        for (Object o : a) {
            if (i > 0)
                out.write(',');
            super.serialize(out, o);
            i++;
        }
    }
}
