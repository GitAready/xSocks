package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Paddy
 */
public class SocksAgent {

    private Logger logger = LoggerFactory.getLogger(SocksAgent.class);

    public static void main(String[] args) {
        SocksAgent agent = new SocksAgent();
        agent.start();
    }

    private void start() {
        logger.info("Starting xSocks agent...");
        SocksEngine engineer = SocksEngineImpl.createNewEngineer();
        engineer.startup(new Context());

    }

}
