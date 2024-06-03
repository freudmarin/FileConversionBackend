package com.marin.fileconversionbackend.controllers;

import com.marin.fileconversionbackend.models.ConversionResult;
import com.marin.fileconversionbackend.services.FileConversionService;
import com.marin.fileconversionbackend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @PostMapping("convert/excel-to-csv")
    public CompletableFuture<ResponseEntity<ConversionResult>> convertExcelToCsv(@RequestParam("file") MultipartFile file) {
        try {
            File inputFile = FileUtils.convertMultipartFileToFile(file);
            CompletableFuture<ConversionResult> resultFuture = fileConversionService.convertExcelToCsv(inputFile);

            return resultFuture.thenApply(result -> {
                if (result.getFilePath() != null) {
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            });
        } catch (IOException e) {
            ConversionResult result = new ConversionResult("Failed to convert file: " + e.getMessage(), null);
            return CompletableFuture.completedFuture(new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


    @PostMapping("convert/word-to-pdf")
    public CompletableFuture<ResponseEntity<ConversionResult>> convertWordToPdf(@RequestParam("file") MultipartFile file) {
        try {
            File inputFile = FileUtils.convertMultipartFileToFile(file);
            CompletableFuture<ConversionResult> resultFuture = fileConversionService.convertWordToPdf(inputFile);

            return resultFuture.thenApply(result -> {
                if (result.getFilePath() != null) {
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            });
        } catch (IOException e) {
            ConversionResult result = new ConversionResult("Failed to convert file: " + e.getMessage(), null);
            return CompletableFuture.completedFuture(new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("convert/pdf-to-image")
    public CompletableFuture<ResponseEntity<ConversionResult>> convertPdfToImage(@RequestParam("file") MultipartFile file) {
        try {
            File inputFile = FileUtils.convertMultipartFileToFile(file);
            CompletableFuture<ConversionResult> resultFuture = fileConversionService.convertPdfToImage(inputFile);

            return resultFuture.thenApply(result -> {
                if (result.getFilePath() != null) {
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            });
        } catch (IOException e) {
            ConversionResult result = new ConversionResult("Failed to convert file: " + e.getMessage(), null);
            return CompletableFuture.completedFuture(new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}

// Define other endpoints for other conversion functionalities
