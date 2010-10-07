package org.daphnia.json;

import java.io.Writer;

import org.daphnia.json.Serializer.ElementSerializer;

public class NumberSerializer extends ElementSerializer {

    public boolean fits(Object o) {
        return (o != null && o instanceof Number);
    }
    
    public void serialize(Writer out, Object o) throws Exception {
        out.write(o.toString());
    }
}
