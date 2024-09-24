package ca.quadrilateral.jua.runner;

import java.io.InputStream;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GameLauncher {
    public static void main(String[] args) {
//        initializeLog4j();
        ArgsContainer.getInstance().setArgs(args);
        initializeSpring();
    }

    private static void initializeLog4j() {
//        final DOMConfigurator configurator = new DOMConfigurator();
//        final InputStream is = GameLauncher.class.getResourceAsStream("/log4j.config.xml");

//        if (is != null) {
//        	configurator.doConfigure(is, LogManager.getLoggerRepository());
//        }
    }

    private static void initializeSpring() {
        final String[] contextPaths = new String[] {"/springContext.xml"};
        new ClassPathXmlApplicationContext(contextPaths);
    }
}
