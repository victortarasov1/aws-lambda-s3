package org.example.processor;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import lombok.RequiredArgsConstructor;
import org.example.service.S3Service;
import org.springframework.stereotype.Service;

import static org.example.config.constant.Constants.INPUT_PREFIX;

@Service
@RequiredArgsConstructor
public class RecordProcessorImpl implements RecordProcessor {

    private final S3Service service;
    private final LambdaLogger logger = LambdaRuntime.getLogger();

    @Override
    public void process(S3EventNotification.S3EventNotificationRecord record) {
        try {
            processRecord(record);
        } catch (Exception ex) {
            logger.log("exception received");
        }
    }

    private void processRecord(S3EventNotification.S3EventNotificationRecord record) {
        var objectKey = record.getS3().getObject().getKey();
        logger.log("received key: " + objectKey);
        if (isValid(objectKey)) processValidRecord(record, objectKey);
        else logger.log("wrong path!");
    }

    private void processValidRecord(S3EventNotification.S3EventNotificationRecord record, String objectKey) {
        logger.log("processing");
        var bucketName = record.getS3().getBucket().getName();
        var content = service.getObjectData(bucketName, objectKey).toUpperCase();
        service.saveContent(bucketName, objectKey, content);
    }

    private static boolean isValid(String objectKey) {
        return objectKey.startsWith(INPUT_PREFIX) && !objectKey.equals(INPUT_PREFIX);
    }

}
