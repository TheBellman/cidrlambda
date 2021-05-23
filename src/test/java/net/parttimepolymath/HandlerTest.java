package net.parttimepolymath;

import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Unit test for simple Handler
 */
public class HandlerTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerTest.class);

    @Test
    public void testHandler() {
        logger.info("testHandler()");
        APIGatewayV2HTTPEvent event = APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/cidr/eu-west-2/S3")
                .withPathParameters(Collections.singletonMap("region", "eu-west-2"))
                .withQueryStringParameters(Collections.singletonMap("ipv6", "true"))
                .build();
        Context context = new TestContext();
        String requestId = context.getAwsRequestId();
        Handler handler = new Handler();
        APIGatewayV2HTTPResponse result = handler.handleRequest(event, context);
        assertEquals(200, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
    };

}
