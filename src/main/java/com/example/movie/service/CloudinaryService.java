package com.example.movie.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${CLOUDINARY_CLOUD_NAME}") String cloudName,
            @Value("${CLOUDINARY_API_KEY}") String apiKey,
            @Value("${CLOUDINARY_API_SECRET}") String apiSecret
    ) {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // MIME 타입 확인
        String mimeType = file.getContentType();
        String resourceType;

        if (mimeType.startsWith("image/")) {
            resourceType = "image"; // 이미지 업로드
        } else if (mimeType.startsWith("video/")) {
            resourceType = "video"; // 비디오 업로드
        } else {
            throw new IllegalArgumentException("Uploaded file is neither an image nor a video.");
        }

        // 임시 파일 생성
        File tempFile = File.createTempFile("temp", file.getOriginalFilename());
        file.transferTo(tempFile); // MultipartFile을 임시 파일로 전송

        // 업로드
        Map<?, ?> uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap("resource_type", resourceType));

        // 임시 파일 삭제
        tempFile.delete();

        return uploadResult.get("url").toString(); // 업로드된 파일의 URL 반환
    }
}
