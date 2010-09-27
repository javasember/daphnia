package org.daphnia.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

public class Server {
    private @Getter @Setter String channel;
    private @Getter @Setter Map<String, Peer> peers;
    
    public Server() {
        this.peers = new ConcurrentHashMap<String, Peer>();
    }
    
    public String send(String peer, String msg) {
        Peer l = this.peers.get(peer);
        return l != null ? l.send(msg) : null;
    }
    
    public void addPeer(String name, Peer l) {
        this.peers.put(name, l);
    }
    
    public void removePeer(String name) {
        this.peers.remove(name);
    }
    
    public interface Peer {
        public String send(String s);
    } 
}
