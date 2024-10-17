package org.example.config.function;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import lombok.RequiredArgsConstructor;
import org.example.processor.RecordProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class MyS3LambdaFunction {
    private final RecordProcessor processor;
    private final LambdaLogger logger = LambdaRuntime.getLogger();
    @Bean
    public Consumer<S3Event> handleEvent() {
        return event -> {
            logger.log("received event");
            event.getRecords().forEach(processor::process);
        };
    }
}
