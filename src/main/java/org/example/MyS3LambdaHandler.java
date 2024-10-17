package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import org.example.processor.RecordProcessor;
import org.example.processor.RecordProcessorImpl;

public class MyS3LambdaHandler implements RequestHandler<S3Event, String> {
    private final RecordProcessor processor = new RecordProcessorImpl();
    private final LambdaLogger logger = LambdaRuntime.getLogger();

    @Override
    public String handleRequest(S3Event event, Context context) {
        logger.log("received event");
        event.getRecords().forEach(processor::process);
        return "Success";
    }


}
