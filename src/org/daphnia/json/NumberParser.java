package org.daphnia.json;

import java.lang.reflect.Type;
import java.text.NumberFormat;

public class NumberParser extends Parser.ElementParser {
    public boolean starts(int c) {
        return (c == '-' || (c >= '0' && c <= '9'));
    }

    private boolean fits(int c) {
        return (c == '-' || c == '+' || (c >= '0' && c <= '9') || c == '.' || c == 'e' || c == 'E');
    }
    
    public Number parse(StepReader in) throws Exception {
        StringBuilder sb = new StringBuilder();
        while (fits(in.peek()))
            sb.append((char) in.pop());

        return NumberFormat.getInstance().parse(sb.toString());
    }
    
    protected Object map(StepReader in, Object t) throws Exception {
        Number n = parse(in);
        if (t instanceof Type) t = t.getClass();
        if (t == Byte.class) {
            return n.byteValue();
        } else if (t == Integer.class) {
            return n.intValue();
        } else if (t == Long.class) {
            return n.longValue();
        } else if (t == Short.class) {
            return n.shortValue();
        } else if (t == Float.class) {
            return n.floatValue();
        } else if (t == Double.class) {
            return n.doubleValue();
        } else if (t == String.class) {
            return n.toString();
        }
        
        return n;
    }
}