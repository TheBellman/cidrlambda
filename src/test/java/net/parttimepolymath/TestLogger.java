package net.parttimepolymath;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stolen from https://github.com/awsdocs/aws-lambda-developer-guide
 */
public class TestLogger implements LambdaLogger {
    private static final Logger logger = LoggerFactory.getLogger(TestLogger.class);
    public void log(String message){
        logger.info(message);
    }
    public void log(byte[] message){
        logger.info(new String(message));
    }
}