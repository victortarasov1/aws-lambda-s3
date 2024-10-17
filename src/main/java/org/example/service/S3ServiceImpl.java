package org.example.service;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.example.constant.Constants.INPUT_PREFIX;
import static org.example.constant.Constants.OUTPUT_PREFIX;

public class S3ServiceImpl implements S3Service {
    private final S3Client s3Client = S3Client.builder().build();
    private final LambdaLogger logger = LambdaRuntime.getLogger();

    @Override
    public String getObjectData(String bucketName, String objectKey) {
        var request = createGetRequest(bucketName, objectKey);
        try (var inputStream = s3Client.getObject(request)) {
            return readObjectData(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveContent(String bucketName, String objectKey, String content) {
        var outputObjectKey = OUTPUT_PREFIX + objectKey.substring(INPUT_PREFIX.length());
        var putObjectRequest = createPutRequest(bucketName, outputObjectKey);
        s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
        logger.log("File content processed and saved to: " + outputObjectKey);
        logger.log("content: " + content);
    }

    private static GetObjectRequest createGetRequest(String bucketName, String objectKey) {
        return GetObjectRequest.builder().bucket(bucketName)
                .key(objectKey).build();
    }

    private static String readObjectData(ResponseInputStream<?> inputStream) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        var content = new StringBuilder();
        var line = "";
        while ((line = reader.readLine()) != null) content.append(line);
        return content.toString();
    }

    private static PutObjectRequest createPutRequest(String bucketName, String outputObjectKey) {
        return PutObjectRequest.builder().bucket(bucketName)
                .key(outputObjectKey).build();
    }

}
