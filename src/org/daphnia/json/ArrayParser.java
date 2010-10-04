package org.daphnia.json;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class ArrayParser extends Parser.ElementParser {
    public boolean starts(int c) {
        return (c == '[');
    }

    public boolean fits(int c) {
        return (c != ']');
    }
    
    public List<?> parse(StepReader in) throws Exception {
        List<Object> list = new ArrayList<Object>();
        in.pop();
        skip(in);
        while (fits(in.peek())) {
            list.add(super.parse(in));
            skip(in);
            switch (in.peek()) {
                case ',':
                    in.pop();
                    skip(in);
                case ']' :
                    continue;
                default :
                    throw new Exception("Error parsing JSON array, expected ',' or ']'");
            }
        }
        in.pop();

        return list;
    }

    public Object map(StepReader in, Object t) throws Exception {
        TypeIterator ti = new TypeIterator(t);
        
        List<Object> list = new ArrayList<Object>();
        in.pop();
        skip(in);
        while (fits(in.peek())) {
            list.add(super.map(in, ti.next()));
            skip(in);
            switch (in.peek()) {
                case ',':
                    in.pop();
                    skip(in);
                case ']' :
                    continue;
                default :
                    throw new Exception("Error parsing JSON array, expected ',' or ']'");
            }
        }
        in.pop();

        return list;
    }
    
    private class TypeIterator {
        Object[] clsa = null;
        Object cls = null;
        int cnt = 0;
        
        public TypeIterator(Object t) {
            if (t instanceof Type) {
                if (t instanceof ParameterizedType) {
                    Type[] ta = ((ParameterizedType) t).getActualTypeArguments();
                    if (ta != null && ta.length > 0) {
                        cls = ta[0];
                    }
                } else t = t.getClass();
            }
            if (clsa == null && cls == null && t instanceof Class) {
                Class<?> c = (Class<?>) t;
                if (c.isArray()) {
                    cls = c.getComponentType();
                } else if (Collection.class.isAssignableFrom(c)) {
                    cls = null;
                } else {
                    cls = c;
                }
            }
            if (clsa == null && cls == null) {
                if (t.getClass().isArray()) {
                    if (t.getClass().getComponentType() instanceof Class || t.getClass().getComponentType() instanceof Type) {
                        clsa = (Object[]) t;
                    } else {
                        cls = t.getClass().getComponentType();
                    }
                } else {
                    cls = t.getClass();
                }
            }
        }
        
        public Object next() {
            return clsa == null ? (cls == null ? null : cls) : (cnt < clsa.length ? clsa[cnt++] : null);
        }

    }
}
