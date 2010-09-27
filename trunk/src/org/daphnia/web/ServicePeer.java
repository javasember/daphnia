package org.daphnia.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.daphnia.web.Server.Peer;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ServicePeer implements Peer {
    private @Getter @Setter String channel;
    private @Getter @Setter String name;
    private @Getter Object service;
    private @Getter Server server;
    private Map<String, Method> methods;

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
        System.out.println("*************** " + this.name);
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
                Object o = new JSONDeserializer<Object>().deserialize('[' + s.substring(idx + 1, idy) + ']');
                try {
                    response = m.invoke(this.service, ((List<?>) o).toArray());
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return response == null ? null : new JSONSerializer().serialize(response);
    }

}
