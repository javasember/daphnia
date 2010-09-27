package org.daphnia.web;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

import org.daphnia.web.Server.Peer;
import org.eclipse.jetty.websocket.WebSocket;

public class WebSocketPeer implements WebSocket, Peer {
    private @Getter @Setter String name;
    private Server server;
    private Outbound out;

    public WebSocketPeer(Server s, String n) {
        this.server = s;
        this.name = n;
        
        this.server.addPeer(this.name, this);
    }
    
    @Override
    public String send(String s) {
        try {
            this.out.sendMessage(s);
        } catch (IOException e) {
            // TODO log something
        }
        return null;
    }

    @Override
    public void onConnect(Outbound o) {
        this.out = o;
    }

    @Override
    public void onDisconnect() {
        this.server.removePeer(this.name);
        this.name = null;
        this.out = null;
        this.server = null;
    }

    @Override
    public void onMessage(byte frame, String s) {
        int idx = s.indexOf('.');
        if (idx > -1) {
            int ti = s.indexOf('#');
            final String ts = (ti > -1) ? s.substring(0, ti + 1) : null;
            final String response = this.server.send(s.substring(ti + 1, idx), s.substring(idx + 1));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    send(ts == null ? response : ts + response);
                }
            }).start();
        }
    }

    @Override
    public void onMessage(byte frame, byte[] b, int off, int len) {
        this.onMessage(frame, new String(b, off, len));
    }

}
