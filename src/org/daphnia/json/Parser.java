package org.daphnia.json;

import java.io.Reader;
import java.io.StringReader;

import lombok.Getter;
import lombok.Setter;

public class Parser {
    private final NumberParser  NP = new NumberParser();
    private final StringParser  SP = new StringParser();
    private final ArrayParser   AP = new ArrayParser();
    private final ObjectParser  OP = new ObjectParser(this);
    private final LiteralParser LP = new LiteralParser();
    private final ElementParser EP = new ElementParser(this);
    private final ElementParser[] parsers = new ElementParser[] { NP, SP, AP, OP, LP };
    
    public Object parse(Reader in) throws Exception {
        return EP.parse(new StepReader(in));
    }
    
    public Object map(Reader in, Object t) throws Exception {
        return EP.map(new StepReader(in), t);
    }
    
    public String parseString(StepReader in) throws Exception {
        return SP.parseString(in);
    }
    
    protected static class ElementParser {
        protected Parser p;

        public ElementParser() {}
        
        protected ElementParser(Parser p) {
            this.p = p;
        }

        protected boolean starts(int c) {
            return c > ' ';
        }

        protected Object parse(StepReader in) throws Exception {
            skip(in);
            for (int i = 0; i < p.parsers.length; i ++) {
                if (p.parsers[i].starts(in.peek())) {
                    return p.parsers[i].parse(in);
                }
            }
            return null;
        }

        protected Object map(StepReader in, Object t) throws Exception {
            skip(in);
            for (int i = 0; i < p.parsers.length; i ++) {
                if (p.parsers[i].starts(in.peek())) {
                    return p.parsers[i].map(in, t);
                }
            }
            return null;
        }

        protected int skip(StepReader in) throws Exception {
            int c;
            while ((c = in.peek()) > -1 && c <= ' ')
                in.pop();
            return c;
        }
    }
    
    public static void main(String[] args) {
        try {
            String json = "{\"id\" : \"123\", \"name\" : true}";
            Object o = new Parser().map(new StringReader(json), Test.class);
            System.out.println(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static class Test {
        private @Getter @Setter Integer id;
        private @Getter @Setter String name;
    }
}
