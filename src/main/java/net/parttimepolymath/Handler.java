package net.parttimepolymath;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import net.parttimepolymath.iplib.IPRange;
import net.parttimepolymath.iplib.Ranges;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * API gateway handler.
 * @author Robert
 * @since 21/05/2021
 */
public class Handler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private final AtomicReference<Ranges> rangeHolder = new AtomicReference<Ranges>();

    private Ranges getRanges(final Context context) {
        try {
            rangeHolder.compareAndSet(null, new IPRange());
        } catch (IOException e) {
            context.getLogger().log("IOException creating IPRanges object: " + e.getMessage());
        } catch (InterruptedException e) {
            context.getLogger().log("InterruptedEception creating IPRanges object: " + e.getMessage());
        }

        return rangeHolder.get();
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(final APIGatewayV2HTTPEvent event, final Context context) {
        ParsedEvent parsedEvent = new ParsedEvent(event);
        context.getLogger().log(parsedEvent.toString());

        Ranges ranges = getRanges(context);
        List<String> responseData = Collections.emptyList();
        if (ranges!=null) {
            if (parsedEvent.isRegions()) {
                responseData = ranges.getRegions();
            } else if (parsedEvent.isServices()) {
                responseData = ranges.getServices();
            } else if (parsedEvent.isCidr()) {
                if (parsedEvent.getRegion() != null && parsedEvent.getService() != null) {
                    responseData = ranges.getPrefixes(parsedEvent.isIpv6(), parsedEvent.getRegion(), parsedEvent.getService());
                } else if (parsedEvent.getRegion() != null) {
                    responseData = ranges.getPrefixes(parsedEvent.isIpv6(), parsedEvent.getRegion());
                } else {
                    responseData = ranges.getPrefixes(parsedEvent.isIpv6());
                }
            }
        }
        // else return a 503

        APIGatewayV2HTTPResponse response = APIGatewayV2HTTPResponse.builder()
                .withIsBase64Encoded(false)
                .withStatusCode(200)
                .withHeaders(Map.of("Content-Type", "text/html"))
                .withBody("<!DOCTYPE html><html><head><title>AWS Lambda sample</title></head><body>"+
                        responseData.toString() +
                        "</body></html>")
                .build();

        return response;
    }
}
