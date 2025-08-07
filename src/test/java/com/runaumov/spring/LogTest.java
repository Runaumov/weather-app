package com.runaumov.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        logger.debug("Debug log example");
        logger.info("Info log example");
        logger.error("Error log example");
    }
}
