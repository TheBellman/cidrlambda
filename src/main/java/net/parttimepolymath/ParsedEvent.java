package net.parttimepolymath;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

/**
 * This class is used to simplify understanding of the incoming event.
 * @author Robert
 * @since 21/05/2021
 */
public final class ParsedEvent {
    private final static String REGIONS = "/v1/regions";
    private final static String SERVICES = "/v1/services";
    private final static String CIDR = "/v1/cidr";
    private final boolean ipv6;
    private final boolean regions;
    private final boolean services;
    private final boolean cidr;
    private final String region;
    private final String service;

    public ParsedEvent(final APIGatewayV2HTTPEvent event) {
        String rawPath = event.getRawPath().strip();

        if (event.getQueryStringParameters() != null) {
            ipv6 = Boolean.parseBoolean(event.getQueryStringParameters().getOrDefault("ipv6", "false"));
        } else {
            ipv6 = false;
        }

        regions = rawPath.equals(REGIONS);
        services = rawPath.equals(SERVICES);
        cidr = rawPath.startsWith(CIDR);

        if (event.getPathParameters() != null) {
            region = event.getPathParameters().getOrDefault("region", null);
            service = event.getPathParameters().getOrDefault("service", null);
        } else {
            region = null;
            service = null;
        }
    }

    public boolean isIpv6() {
        return ipv6;
    }

    public boolean isRegions() {
        return regions;
    }

    public boolean isServices() {
        return services;
    }

    public boolean isCidr() {
        return cidr;
    }

    public String getRegion() {
        return region;
    }

    public String getService() {
        return service;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ipv6", ipv6)
                .append("regions", regions)
                .append("services", services)
                .append("cidr", cidr)
                .append("region", region)
                .append("service",service).toString();
    }

}
