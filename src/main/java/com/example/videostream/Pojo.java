package com.example.videostream;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Data
public class Pojo {
    private MultipartFile file;
}
