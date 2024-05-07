package com.clothz.aistyling.global.s3;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
    private static final String BUCKET = "ai-styling-s3";
    private final S3Operations s3Operations;

    public S3Service(final S3Operations s3Operations) {
        this.s3Operations = s3Operations;
    }

    @Transactional
    public List<String> upload(final List<MultipartFile> multipartFile) throws IOException {
        final List<String> fileNameList = new ArrayList<>();
        for(final MultipartFile file : multipartFile) {
            final String fileName = createFileName(file.getName());
            if (!MediaType.IMAGE_PNG.toString().equals(file.getContentType()) &&
                    !MediaType.IMAGE_JPEG.toString().equals(file.getContentType())) {
                throw new IllegalArgumentException("사진 파일만 업로드 가능합니다");
            }
            try (final InputStream is = file.getInputStream()) {
                final S3Resource upload = s3Operations.upload(BUCKET, fileName, is,
                        ObjectMetadata.builder().contentType(file.getContentType()).build());
                final String imgURL = upload.getURL().toString();
                fileNameList.add(imgURL);
            }
        }
        return fileNameList;
    }

    private String createFileName(final String fileName){
        return UUID.randomUUID() + fileName;
    }
}