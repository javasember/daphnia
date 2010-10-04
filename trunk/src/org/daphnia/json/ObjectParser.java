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
        Object o = t;
        if (t instanceof Class) {
            o = ((Class<?>) t).newInstance();
        }
        BeanMap map = new BeanMap(o);

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
            if (map.containsKey(key)) {
                value = super.map(in, map.getType(key));
                map.put(key, value);
            } else {
                super.parse(in);
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
        
        return map.getBean();
    }
}
