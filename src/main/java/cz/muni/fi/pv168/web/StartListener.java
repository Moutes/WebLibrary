package cz.muni.fi.pv168.web;

import org.library.managers.BookManagerImpl;
import org.library.managers.CustomerManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartListener implements ServletContextListener {

    final static Logger log = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        log.info("aplikace inicializována");
        ServletContext servletContext = ev.getServletContext();
        System.out.println(servletContext.getAttributeNames());
        servletContext.setAttribute("customerManager", new CustomerManagerImpl());
        servletContext.setAttribute("bookManager", new BookManagerImpl());
        System.out.println(servletContext.getAttributeNames());
        log.info("vytvoøeny manažery");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        log.info("aplikace konèí");
    }
}