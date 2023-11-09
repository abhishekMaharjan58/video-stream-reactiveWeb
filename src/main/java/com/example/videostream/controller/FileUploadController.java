package com.example.videostream.controller;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

@RestController
public class FileUploadController {

    private static final String UPLOAD_DIR = "myvideos";     // Directory to store uploaded files

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFile(@RequestPart("file") Flux<DataBuffer> fileData,
                                   @RequestPart("file") FilePart filePart) {
        Path uploadPath = Path.of(System.getProperty("user.home") + File.separator + UPLOAD_DIR);    // Create the directory if it doesn't exist
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            return Mono.error(e);
        }
        Path targetPath = uploadPath.resolve(filePart.filename());

        AsynchronousFileChannel fileChannel;
        try {
            fileChannel = AsynchronousFileChannel.open(targetPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            return Mono.error(e);
        }

        return fileData
                .flatMap(dataBuffer -> writeAsync(fileChannel, dataBuffer))
                .doFinally(signalType -> closeFileChannel(fileChannel))
                .then(Mono.just("Received and processed part of the file."));
    }

    private Mono<Void> writeAsync(AsynchronousFileChannel fileChannel, DataBuffer dataBuffer) {
        ByteBuffer buffer = dataBuffer.asByteBuffer();
        System.out.println("WRITING");
        CompletableFuture<Integer> future = new CompletableFuture<>();

        fileChannel.write(buffer, 0, null, new FileWriteCompletionHandler(buffer, future));

        return Mono.fromFuture(() -> future).then();
    }

    private void closeFileChannel(AsynchronousFileChannel fileChannel) {
        try {
            fileChannel.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing file channel", e);
        }
    }

    private static class FileWriteCompletionHandler implements java.nio.channels.CompletionHandler<Integer, Void> {

        private final ByteBuffer buffer;
        private final CompletableFuture<Integer> future;

        public FileWriteCompletionHandler(ByteBuffer buffer, CompletableFuture<Integer> future) {
            this.buffer = buffer;
            this.future = future;
        }

        @Override
        public void completed(Integer result, Void attachment) {
            buffer.clear();
            future.complete(result);
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            buffer.clear();
            future.completeExceptionally(exc);
        }
    }
}


