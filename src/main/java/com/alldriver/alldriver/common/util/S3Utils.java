package com.alldriver.alldriver.common.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Utils {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloud-front.url}")
    private String cloudFrontUrl;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = UUID.randomUUID() + multipartFile.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        String url = cloudFrontUrl + "/" + originalFilename;

        return url;
    }

    public void deleteFile(String url){
        amazonS3.deleteObject(bucket, url.split("/")[3]);
    }

}
