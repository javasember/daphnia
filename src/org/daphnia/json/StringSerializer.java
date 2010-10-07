package org.daphnia.json;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import org.daphnia.json.Serializer.ElementSerializer;

public class StringSerializer extends ElementSerializer {

    public boolean fits(Object o) {
        return (o != null && o instanceof String);
    }

    public void serialize(Writer out, Object o) throws Exception {
        out.write('"');
        Reader r = new StringReader(o.toString());
        int c;
        while ((c = r.read()) > -1) {
            switch (c) {
            case '\\':
                out.write("\\\\");
                break;
            case '\"':
                out.write("\\\"");
                break;
            case '\b':
                out.write("\\b");
                break;
            case '\f':
                out.write("\\f");
                break;
            case '\n':
                out.write("\\n");
                break;
            case '\r':
                out.write("\\r");
                break;
            case '\t':
                out.write("\\t");
                break;
            default:
                out.write(c);
                break;
            }
        }
        out.write('"');
    }
}
