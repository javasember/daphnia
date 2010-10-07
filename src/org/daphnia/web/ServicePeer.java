package org.daphnia.web;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.daphnia.json.Parser;
import org.daphnia.json.Serializer;
import org.daphnia.web.Server.Peer;

public class ServicePeer implements Peer {
    private @Getter @Setter String channel;
    private @Getter @Setter String name;
    private @Getter Object service;
    private @Getter Server server;
    private Map<String, Method> methods;
    
    private static final Parser parser;
    private static final Serializer serializer;
    
    static {
        parser = new Parser();
        serializer = new Serializer();
    }

    public ServicePeer() {
        methods = new HashMap<String, Method>();
    }
    
    public void setService(Object s) {
        this.service = s;
        for (Method m : s.getClass().getMethods()) {
            if (!m.getDeclaringClass().equals(Object.class) && Modifier.isPublic(m.getModifiers())) {
                this.methods.put(m.getName(), m);
            }
        }
    }
    
    public void setServer(Server s) {
        this.server = s;
        this.server.addPeer(this.name, this);
    }
    
    @Override
    public String send(String s) {
        int idx = s.indexOf('(');
        int idy = s.lastIndexOf(')');
        Object response = null;
        
        if (idx > -1 && idy > -1) {
            String mn = s.substring(0, idx).trim();
            Method m = this.methods.get(mn);
            if (m != null) {
                StringReader in = new StringReader('[' + s.substring(idx + 1, idy) + ']');
                try {
                    Object o = parser.map(in, m.getParameterTypes());
                    response = m.invoke(this.service, ((List<?>) o).toArray());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        StringWriter sw = new StringWriter();
        try {
            serializer.serialize(sw, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sw.toString();
    }

}
