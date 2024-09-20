package passwordsaver.progettotwebpasswordsaver.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import passwordsaver.progettotwebpasswordsaver.model.LogManagerDB;

@WebListener
public class StartupContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        LogManagerDB.getManager().addNewLog("not-logged-user", "SERVER STARTUP");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        LogManagerDB.getManager().addNewLog("not-logged-user", "SERVER SHUTDOWN");
    }
}
