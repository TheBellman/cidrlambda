package net.parttimepolymath;

import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple Handler
 */
public class HandlerTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerTest.class);

    @Test
    public void testHandler() {
        logger.info("testHandler()");
        APIGatewayV2HTTPEvent event = APIGatewayV2HTTPEvent.builder().build();
        Context context = new TestContext();
        String requestId = context.getAwsRequestId();
        Handler handler = new Handler();
        APIGatewayV2HTTPResponse result = handler.handleRequest(event, context);
        assertEquals(200, result.getStatusCode());
    }

}
