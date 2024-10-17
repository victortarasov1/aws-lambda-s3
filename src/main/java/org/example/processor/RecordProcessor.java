package org.example.processor;

import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

public interface RecordProcessor {
    void process(S3EventNotification.S3EventNotificationRecord record);
}
