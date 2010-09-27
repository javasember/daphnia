package org.daphnia.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daphnia.web.Server;
import org.daphnia.web.ServicePeer;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DaphniaNamespaceHandler extends NamespaceHandlerSupport {
    private static Log log = LogFactory.getLog(DaphniaNamespaceHandler.class);

    @Override
    public void init() {
        registerBeanDefinitionParser("server", new ServerBeanDefinitionParser());
        registerBeanDefinitionDecorator("service", new ServiceBeanDefinitionDecorator());
        registerBeanDefinitionDecoratorForAttribute("channel", new ChannelAttributeBeanDefinitionDecorator());
        registerBeanDefinitionDecoratorForAttribute("publish", new PublishAttributeBeanDefinitionDecorator());
    }

    public static class ServerBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return Server.class;
        }
        
        @Override
        protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
            log.info("parsing server");
            String chn = element.getAttribute("channel");
            builder.addPropertyValue("channel", chn);
        }
    }
    
    public static class ChannelAttributeBeanDefinitionDecorator implements BeanDefinitionDecorator {

        @Override
        public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder holder, ParserContext ctx) {
            log.info("parsing channel");
            String channel = ((Attr) source).getValue();
            String beanName = holder.getBeanName() + "Peer";
            if (!ctx.getRegistry().containsBeanDefinition(beanName)) {
               BeanDefinitionBuilder initializer = BeanDefinitionBuilder.rootBeanDefinition(ServicePeer.class);
               initializer.addPropertyValue("channel", channel);
               initializer.addPropertyReference("service", holder.getBeanName());
               ctx.getRegistry().registerBeanDefinition(beanName, initializer.getBeanDefinition());
            }
            return holder;
        }
        
    }

    public static class PublishAttributeBeanDefinitionDecorator implements BeanDefinitionDecorator {

        @Override
        public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder holder, ParserContext ctx) {
            log.info("parsing publish");
            String server = ((Attr) source).getValue();
            String beanName = holder.getBeanName() + "Peer";
            if (!ctx.getRegistry().containsBeanDefinition(beanName)) {
               BeanDefinitionBuilder initializer = BeanDefinitionBuilder.rootBeanDefinition(ServicePeer.class);
               initializer.addPropertyValue("name", beanName);
               initializer.addPropertyReference("server", server);
               initializer.addPropertyReference("service", holder.getBeanName());
               ctx.getRegistry().registerBeanDefinition(beanName, initializer.getBeanDefinition());
            }
            return holder;
        }
        
    }

    public static class ServiceBeanDefinitionDecorator implements BeanDefinitionDecorator {
        @Override
        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder bdh, ParserContext ctx) {
            String beanName = bdh.getBeanName() + "peer";
            
            if (!ctx.getRegistry().containsBeanDefinition(beanName)) {
               BeanDefinitionBuilder initializer = BeanDefinitionBuilder.rootBeanDefinition(ServicePeer.class);
               initializer.addPropertyReference("service", bdh.getBeanName());
               ctx.getRegistry().registerBeanDefinition(beanName, initializer.getBeanDefinition());
            }
            return null;
        }
    }
}
