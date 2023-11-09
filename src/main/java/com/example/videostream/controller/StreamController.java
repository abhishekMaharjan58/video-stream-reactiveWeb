package com.example.videostream.controller;

import com.example.videostream.service.serviceImpl.StreamServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "/video")
@RequiredArgsConstructor
public class StreamController {

    private final StreamServiceImpl streamService;

    @GetMapping(value = "/{filename}")
    public Mono<FileSystemResource> getVideos(@PathVariable String filename, @RequestHeader("Range") String range) {
        try{
        System.out.println("range in bytes() : " + range);
        return streamService.getVideo(filename);}
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

//    @GetMapping(value = "/{filename}")
//    public Mono<Resource> streamVideo(@PathVariable String filename) {
//        Path videoPath = Paths.get("C:/Users/hp/myvideos/" + filename);
//        Resource videoResource = new FileSystemResource(videoPath);
//        return (Mono<Resource>) videoResource;
//    }
}
