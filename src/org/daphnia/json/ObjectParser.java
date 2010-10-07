package org.daphnia.json;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;

public class ObjectParser extends Parser.ElementParser {
    protected ObjectParser(Parser p) {
        super(p);
    }

    public boolean starts(int c) {
        return (c == '{');
    }

    public boolean fits(int c) {
        return (c != '}');
    }
    
    public Map<String, Object> parse(StepReader in) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>(); 
//step over {
        in.pop();
        skip(in);
        String key;
        Object value;
        while (fits(in.peek())) {
//parse key
            skip(in);
            key = p.parseString(in);
            skip(in);
//step over :
            if (in.peek() != ':') throw new Exception("Error parsing JSON object, expected ':'");
            in.pop();
//parse value
            skip(in);
            value = super.parse(in);
            map.put(key, value);
            skip(in);
//check ,
            switch (in.peek()) {
                case ',':
                    in.pop();
                    skip(in);
                case '}' :
                    continue;
                default :
                    throw new Exception("Error parsing JSON object, expected ',' or '}'");
            }
        }
//step over }
        in.pop();
        
        return map;
    }
    
    protected Object map(StepReader in, Object t) throws Exception {
        Object o;
        Map<String, Object> map = null;
        BeanMap bm = null;
        try {
            bm = new BeanMap(o = ((Class<?>) t).newInstance());
        } catch (Exception e) {
            o = map = new HashMap<String, Object>();
        }

//step over {
        in.pop();
        skip(in);
        String key;
        Object value;
        while (fits(in.peek())) {
//parse key
            skip(in);
            key = p.parseString(in);
            skip(in);
//step over :
            if (in.peek() != ':') throw new Exception("Error parsing JSON object, expected ':'");
            in.pop();
//parse value
            skip(in);
            if (bm != null) {
                if (bm.containsKey(key)) {
                    value = super.map(in, bm.getWriteMethod(key).getGenericParameterTypes()[0]);
                    bm.put(key, value);
                } else {
                    super.parse(in);
                }
            } else {
                map.put(key, super.parse(in));
            }
            skip(in);
//check ,
            switch (in.peek()) {
                case ',':
                    in.pop();
                    skip(in);
                case '}' :
                    continue;
                default :
                    throw new Exception("Error parsing JSON object, expected ',' or '}'");
            }
        }
//step over }
        in.pop();
        
        return o;
    }
}
