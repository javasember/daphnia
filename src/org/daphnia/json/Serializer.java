package org.daphnia.json;

import java.io.Writer;

public class Serializer {
    private NumberSerializer  NS = new NumberSerializer();
    private StringSerializer  SS = new StringSerializer();
    private LiteralSerializer LS = new LiteralSerializer();
    private ArraySerializer   AS = new ArraySerializer(this);
    private ObjectSerializer  OS = new ObjectSerializer(this);
    private ElementSerializer ES = new ElementSerializer(this);
    private ElementSerializer[] sers = new ElementSerializer[] {NS, SS, LS, AS, OS};
    
    public void serialize(Writer out, Object o) throws Exception {
        ES.serialize(out, o);
        out.flush();
    } 

    protected static class ElementSerializer {
        protected Serializer s;
        
        public ElementSerializer() {}

        public ElementSerializer(Serializer s) {
            this.s = s;
        }

        public boolean fits(Object o) {
            return true;
        }
        
        public void serialize(Writer out, Object o) throws Exception {
            for (ElementSerializer es : s.sers) {
                if (es.fits(o)) {
                    es.serialize(out, o);
                    return;
                }
            }
        }
    }
}
