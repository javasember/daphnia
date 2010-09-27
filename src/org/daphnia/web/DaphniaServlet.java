package org.daphnia.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class DaphniaServlet extends WebSocketServlet {
    private static final long serialVersionUID = -5028239895110549502L;
    
    private static final transient Log log;
    private final transient Map<String, Server> servers;
    
    static {
        log = LogFactory.getLog(DaphniaServlet.class);
    }

    public DaphniaServlet() {
        servers = new HashMap<String, Server>();
    }
    
    @Override
    public void init() throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        if (ctx != null) {
            AutowireCapableBeanFactory bf = ctx.getAutowireCapableBeanFactory();
            if (bf != null) {
                bf.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
            }
        }
        
        log.info("ctx wired");
        Map<String, Server> map = ctx.getBeansOfType(Server.class);
        log.info("found " + map.size() + " server(s)");
        for (Map.Entry<String, Server> e : map.entrySet()) {
            log.info("server '" + e.getKey() + "' on channel '" + e.getValue().getChannel() + '\'');
            this.servers.put(e.getValue().getChannel(), e.getValue());
        }

        super.init();
    }

    @Override
    protected WebSocket doWebSocketConnect(HttpServletRequest reuqest, String channel) {
        log.info("channel requested: " + channel);
        Server s = this.servers.get(channel);
        return (s != null) ? new WebSocketPeer(s, Long.toHexString(new Date().getTime())) : null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
        getServletContext().getNamedDispatcher("default").forward(request,response);
    }
}
