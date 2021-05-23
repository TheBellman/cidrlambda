package net.parttimepolymath;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.parttimepolymath.iplib.Ranges;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * API gateway handler.
 *
 * @author Robert
 * @since 21/05/2021
 */
public class Handler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * use a lazy singleton pattern to try to avoid building the IPRange() object repeatedly.
     * TODO: fix this, it's still constructing every time of course
     * @param context the context of the lambda event.
     * @return an instance of Ranges if one could be built. May be null on error.
     */
    private Ranges getRanges(final Context context) {
        try {
            return RangeFactory.getRanges();
        } catch (IOException e) {
            context.getLogger().log("IOException creating IPRanges object: " + e.getMessage());
        } catch (InterruptedException e) {
            context.getLogger().log("InterruptedException creating IPRanges object: " + e.getMessage());
        }

        return null;
    }

    /**
     * construct a 503 response to return.3
     *
     * @return a well formed APIGatewayV2HTTPResponse
     */
    private APIGatewayV2HTTPResponse errorResponse() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(503)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody("{}").build();
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(final APIGatewayV2HTTPEvent event, final Context context) {
        ParsedEvent parsedEvent = new ParsedEvent(event);
        context.getLogger().log(parsedEvent.toString());

        Ranges ranges = getRanges(context);
        if (ranges == null) {
            return errorResponse();
        }

        List<String> responseData = Collections.emptyList();

        if (parsedEvent.isRegions()) {
            responseData = ranges.getRegions();
        } else if (parsedEvent.isServices()) {
            responseData = ranges.getServices();
        } else if (parsedEvent.isCidr()) {
            if (parsedEvent.getRegion() != null && parsedEvent.getService() != null) {
                responseData = ranges.getPrefixes(parsedEvent.isIpv6(), parsedEvent.getRegion(),
                        parsedEvent.getService());
            } else if (parsedEvent.getRegion() != null) {
                responseData = ranges.getPrefixes(parsedEvent.isIpv6(), parsedEvent.getRegion());
            } else {
                responseData = ranges.getPrefixes(parsedEvent.isIpv6());
            }
        }

        /*
         * note <https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-develop-integrations-lambda.html>
         * indicates we could just return a simple JSON string from this method, and the API gateway will wrap it,
         * but the APIGatewayV2HTTPResponse is a clear and unambiguous way of managing output so may as well use it.
         */

        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(200)
                .withHeaders(Map.of("Content-Type","application/json"))
                .withBody(gson.toJson(responseData))
                .build();
    }
}
