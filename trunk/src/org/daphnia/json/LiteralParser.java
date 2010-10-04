package org.daphnia.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LiteralParser extends Parser.ElementParser {
    private final Map<String, Object> map;
    
    public LiteralParser() {
        map = new HashMap<String, Object>();
        map.put("true", true);
        map.put("false", false);
        map.put("null", null);
    }

    public boolean starts(int c) {
        return fits(c);
    }

    public boolean fits(int c) {
        return (c >= 'a' && c <= 'z');
    }
    
    public Object parse(StepReader in) throws Exception {
        StringBuilder sb = new StringBuilder();
        while (fits(in.peek()))
            sb.append((char) in.pop());

        return map.get(sb.toString());
    }
    
    protected Object map(StepReader in, Object t) throws Exception {
        Object o = parse(in);
        if (t instanceof Type) t = t.getClass();
        if (t instanceof Class) {
            Class<?> c = (Class<?>) t;
            if (c == String.class) {
                o = "" + o;
            } else if (Number.class.isAssignableFrom(c)) {
                if (o != null && o.getClass() == Boolean.class) {
                    o = ((Boolean) o) ? 1 : 0;
                } else {
                    o = (o == null ? 0 : 1);
                }
            }
        }
        return o;
    }
}
