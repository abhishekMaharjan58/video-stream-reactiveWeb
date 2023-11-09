package com.example.videostream.service.serviceImpl;

import com.example.videostream.service.StreamService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class StreamServiceImpl implements StreamService {

    private final ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(StreamServiceImpl.class);

    private static final String UPLOAD_DIR = "myvideos";     // Directory to store uploaded files


    public Mono<FileSystemResource> getVideo(String filename) {
        Path filePath = Paths.get(System.getProperty("user.home") + File.separator+ UPLOAD_DIR + File.separator + filename);
        logger.debug("File path: {}", filePath);
        return Mono.fromSupplier(() -> new FileSystemResource(filePath));
    }
}
