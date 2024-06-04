package com.marin.fileconversionbackend.controllers;

import com.marin.fileconversionbackend.models.ConversionResult;
import com.marin.fileconversionbackend.services.FileConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FileConversionController {

    private final FileConversionService fileConversionService;

    @PostMapping("convert/word-to-pdf")
    public CompletableFuture<ResponseEntity<ConversionResult>> convertWordToPdf(@RequestParam("file") MultipartFile file) {
        try {
            File inputFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(inputFile);
            return fileConversionService.convertWordToPdf(inputFile)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.status(500).body(new ConversionResult("Conversion failed: " + ex.getMessage(), null)));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body(new ConversionResult("Failed to process file: " + e.getMessage(), null)));
        }
    }

    @PostMapping("convert/pdf-to-image")
    public CompletableFuture<ResponseEntity<ConversionResult>> convertPdfToImage(@RequestParam("file") MultipartFile file) {
        try {
            File inputFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(inputFile);
            return fileConversionService.convertPdfToImages(inputFile)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.status(500).body(new ConversionResult("Conversion failed: " + ex.getMessage(), null)));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body(new ConversionResult("Failed to process file: " + e.getMessage(), null)));
        }
    }

    @PostMapping("convert/excel-to-csv")
    public CompletableFuture<ResponseEntity<ConversionResult>> convertExcelToCsv(@RequestParam("file") MultipartFile file) {
        try {
            File inputFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(inputFile);
            return fileConversionService.convertExcelToCsv(inputFile)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.status(500).body(new ConversionResult("Conversion failed: " + ex.getMessage(), null)));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body(new ConversionResult("Failed to process file: " + e.getMessage(), null)));
        }
    }

    @PostMapping("convert/text-to-pdf")
    public CompletableFuture<ResponseEntity<ConversionResult>> convertTextToPdf(@RequestParam("file") MultipartFile file) {
        try {
            File inputFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(inputFile);
            return fileConversionService.convertTextToPdf(inputFile)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.status(500).body(new ConversionResult("Conversion failed: " + ex.getMessage(), null)));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body(new ConversionResult("Failed to process file: " + e.getMessage(), null)));
        }
    }

    @PostMapping("convert/image-to-pdf")
    public CompletableFuture<ResponseEntity<ConversionResult>> convertImageToPdf(@RequestParam("file") MultipartFile file) {
        try {
            File inputFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(inputFile);
            return fileConversionService.convertImageToPdf(inputFile)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> ResponseEntity.status(500).body(new ConversionResult("Conversion failed: " + ex.getMessage(), null)));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body(new ConversionResult("Failed to process file: " + e.getMessage(), null)));
        }
    }
}

// Define other endpoints for other conversion functionalities
