package org.example;


import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

public class Main {
    public static void main(String[] args) {
        new Main().run();

    }

    private void run() {
        try(var s3client = getS3Client()) {
            var listObjects = ListObjectsV2Request.builder().bucket("victor-tarasov-s3-v1").build();
            String bucketName = "tarasov-s3-v2";

            var bucketRequest = CreateBucketRequest.builder().bucket(bucketName).build();

            s3client.createBucket(bucketRequest);
        }
    }

    private S3Client getS3Client() {
        return S3Client.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }


}