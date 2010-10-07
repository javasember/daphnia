package org.daphnia.json;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArrayParser extends Parser.ElementParser {
    protected ArrayParser(Parser p) {
        super(p);
    }

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

        return morph(list, t);
    }
    
    private Object morph(List<Object> list, Object t) {
        if (t instanceof ParameterizedType)
            t = ((ParameterizedType) t).getRawType();
        if (t instanceof Class) {
            Class<?> c = (Class<?>) t;
            if (c.isArray()) {
                Object[] a = (Object[]) Array.newInstance(c.getComponentType(), list.size());
                return list.toArray(a);
            }
            if (Set.class.isAssignableFrom(c))
                return new HashSet<Object>(list);
        }
        
        return list;
    }
    
    private class TypeIterator {
        Object[] clsa = null;
        Object cls = null;
        int cnt = 0;
        
        public TypeIterator(Object t) {
            if (t instanceof ParameterizedType) {
                Type[] ta = ((ParameterizedType) t).getActualTypeArguments();
                if (ta != null && ta.length > 0) {
                    cls = ta[0];
                }
            }
            if (clsa == null && cls == null) {
                if (t instanceof Class) {
                    Class<?> c = (Class<?>) t;
                    if (c.isArray()) {
                        cls = c.getComponentType();
                    } else if (Collection.class.isAssignableFrom(c)) {
                        cls = null;
                    } else {
                        cls = c;
                    }
                } else {
                    if (t.getClass().isArray()) {
                        clsa = (Object[]) t;
                    } else {
                        cls = t.getClass();
                    }
                }
            }
        }
        
        public Object next() {
            return clsa == null ? (cls == null ? null : cls) : (cnt < clsa.length ? clsa[cnt++] : null);
        }

    }
}

    