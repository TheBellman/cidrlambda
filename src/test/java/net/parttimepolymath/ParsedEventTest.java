package net.parttimepolymath;


import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParsedEventTest {

    private ParsedEvent instance;

    @BeforeEach
    void setUp() {
        APIGatewayV2HTTPEvent event= APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/cidr/eu-west-2/S3")
                .withPathParameters(Map.of("region", "eu-west-2", "service", "S3"))
                .withQueryStringParameters(Collections.singletonMap("ipv6", "true"))
                .build();
        instance = new ParsedEvent(event);
    }

    @Test
    void isIpv6() {
        assertTrue(instance.isIpv6());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/cidr/region/service")
                .withQueryStringParameters(Collections.singletonMap("fred", "mary"))
                .build());
        assertFalse(instance.isIpv6());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/cidr/region/service")
                .build());
        assertFalse(instance.isIpv6());
    }

    @Test
    void isRegions() {
        assertFalse(instance.isRegions());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/regions")
                .build());
        assertTrue(instance.isRegions());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/fred")
                .build());
        assertFalse(instance.isRegions());
    }

    @Test
    void isServices() {
        assertFalse(instance.isServices());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/services")
                .build());
        assertTrue(instance.isServices());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/fred")
                .build());
        assertFalse(instance.isServices());
    }

    @Test
    void isCidr() {
        assertTrue(instance.isCidr());
        assertEquals("eu-west-2", instance.getRegion());
        assertEquals("S3", instance. getService());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/cidr/eu-west-2")
                .withPathParameters(Collections.singletonMap("region", "eu-west-2"))
                .build());
        assertTrue(instance.isCidr());
        assertEquals("eu-west-2", instance.getRegion());
        assertNull(instance. getService());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withRawPath("/v1/cidr")
                .build());
        assertTrue(instance.isCidr());
        assertNull(instance.getRegion());
        assertNull(instance. getService());

        instance = new ParsedEvent(APIGatewayV2HTTPEvent.builder()
                .withPathParameters(Map.of("region", "eu-west-2", "service", "S3"))
                .withRawPath("/v1/cidr/eu-west-2/S3/some/other/junk")
                .build());
        assertTrue(instance.isCidr());
        assertEquals("eu-west-2", instance.getRegion());
        assertEquals("S3", instance. getService());
    }

    @Test
    void testToString() {
        assertEquals("ParsedEvent[ipv6=true,regions=false,services=false,cidr=true,region=eu-west-2,service=S3]", instance.toString());
    }
}