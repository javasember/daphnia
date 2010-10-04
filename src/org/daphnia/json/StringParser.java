package org.daphnia.json;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

public class StringParser extends Parser.ElementParser {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    
    public boolean starts(int c) {
        return c == '"';
    }

    public static boolean fits(int c) {
        return c != '"';
    }
    
    public String parseString(StepReader in) throws Exception {
        int c;
        StringBuilder sb = new StringBuilder();
        in.pop();
        while (fits(c = in.peek())) {
            if (c == '\\') {
                in.pop();
                switch (in.peek()) {
                    case '"' :
                    case '\\' :
                    case '/' :
                        c = in.pop();
                        break;
                    case 'b' :
                        c = '\b';
                        in.pop();
                        break;
                    case 'f' :
                        c = '\f';
                        in.pop();
                        break;
                    case 'n' :
                        c = '\n';
                        in.pop();
                        break;
                    case 'r' :
                        c = '\r';
                        in.pop();
                        break;
                    case 't' :
                        c = '\t';
                        in.pop();
                        break;
                    case 'u' :
                        in.pop();
                        c = in.readUniChar();
                        break;
                }
            } else
                in.pop();
            sb.append((char) c);
        }
        in.pop();

        return sb.toString();
    }
    
    public Object parse(StepReader in) throws Exception {
        String s = parseString(in);
        try {
            return sdf.parse(s);
        } catch (Exception ex) {
            return s;
        }
    }
    
    protected Object map(StepReader in, Object t) throws Exception {
        Object o = parse(in);
        if (t instanceof Type) t = t.getClass();
        if (t instanceof Class && o.getClass() != t)
            try {
                return ((Class<?>) t).getDeclaredConstructor(o.getClass()).newInstance(o);
            } catch (Exception e) {}
        return o;
    }
}
