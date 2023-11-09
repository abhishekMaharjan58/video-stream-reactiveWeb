package com.example.videostream.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

public interface StreamService {
    Mono<FileSystemResource> getVideo(String title);
}
