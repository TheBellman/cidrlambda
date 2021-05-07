package net.parttimepolymath;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.google.gson.Gson;

public final class Util {
    public static void logEnvironment(final APIGatewayV2HTTPEvent event, final Context context, final Gson gson)
    {
        LambdaLogger logger = context.getLogger();

        // log execution details
//        logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
        logger.log("CONTEXT: " + gson.toJson(context));

        // log event details
//        logger.log("EVENT: " + gson.toJson(event));
//        logger.log("EVENT TYPE: " + event.getClass().toString());

        logger.log("RAW PATH: " + event.getRawPath());
        logger.log("RAW QUERY STRING: " + event.getRawQueryString());
        logger.log("ROUTE KEY: " + event.getRouteKey());
        logger.log("VERSION: " + event.getVersion());
    }
}
