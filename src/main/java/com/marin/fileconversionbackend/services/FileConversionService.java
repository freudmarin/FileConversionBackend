package com.marin.fileconversionbackend.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.marin.fileconversionbackend.models.ConversionResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FileConversionService {

    @Async
    public CompletableFuture<ConversionResult> convertExcelToCsv(File inputFile) {
        String fileName = inputFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String outputFileName = baseName + ".csv";
        Path outputPath = Paths.get(outputFileName);

        try (InputStream fis = Files.newInputStream(inputFile.toPath());
             OutputStream fos = Files.newOutputStream(outputPath);
             BufferedWriter bw = Files.newBufferedWriter(outputPath)) {

            Workbook workbook;

            if ("xlsx".equalsIgnoreCase(fileExtension)) {
                workbook = new XSSFWorkbook(fis);
            } else if ("xls".equalsIgnoreCase(fileExtension)) {
                workbook = new HSSFWorkbook(fis);
            } else {
                return CompletableFuture.completedFuture(
                        new ConversionResult("Unsupported file format: " + fileExtension, null));
            }

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    bw.write(cell.toString() + ",");
                }
                bw.newLine();
            }

            ConversionResult result = new ConversionResult("File converted successfully", outputPath.toString());
            return CompletableFuture.completedFuture(result);

        } catch (IOException e) {
            ConversionResult result = new ConversionResult("Failed to convert file: " + e.getMessage(), null);
            return CompletableFuture.completedFuture(result);
        }
    }

    @Async
    public CompletableFuture<ConversionResult> convertWordToPdf(File inputFile) {
        String fileName = inputFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String outputFileName = baseName + ".pdf";
        Path outputPath = Paths.get(outputFileName);

        try (InputStream docxInputStream = new FileInputStream(inputFile);
             XWPFDocument document = new XWPFDocument(docxInputStream);
             OutputStream pdfOutputStream = new FileOutputStream(outputPath.toFile())) {

            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, pdfOutputStream);
            pdfDocument.open();

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                pdfDocument.add(new Paragraph(paragraph.getText()));
            }
            pdfDocument.close();

            ConversionResult result = new ConversionResult("File converted successfully", outputPath.toString());
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            ConversionResult result = new ConversionResult("Failed to convert file: " + e.getMessage(), null);
            return CompletableFuture.completedFuture(result);
        }
    }

    @Async
    public CompletableFuture<ConversionResult> convertPdfToImages(File inputFile) {
        String fileName = inputFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        Path outputDirPath = Paths.get(System.getProperty("java.io.tmpdir"), baseName + "_images");

        // Create the output directory if it doesn't exist
        if (!outputDirPath.toFile().exists()) {
            outputDirPath.toFile().mkdirs();
        }

        try (PDDocument document = PDDocument.load(inputFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int numberOfPages = document.getNumberOfPages();

            for (int page = 0; page < numberOfPages; page++) {
                String outputFileName = baseName + "_page_" + (page + 1) + ".png";
                Path outputPath = outputDirPath.resolve(outputFileName);
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                ImageIOUtil.writeImage(bim, outputPath.toString(), 300);
            }

            ConversionResult result = new ConversionResult("File converted successfully", outputDirPath.toString());
            return CompletableFuture.completedFuture(result);

        } catch (IOException e) {
            ConversionResult result = new ConversionResult("Failed to convert file: " + e.getMessage(), null);
            return CompletableFuture.completedFuture(result);
        }
    }

    @Async
    public CompletableFuture<ConversionResult> convertTextToPdf(File inputFile) {
        String fileName = inputFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String outputFileName = baseName + ".pdf";
        Path outputPath = Paths.get(outputFileName);

        try (InputStream textInputStream = new FileInputStream(inputFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(textInputStream));
             OutputStream pdfOutputStream = new FileOutputStream(outputPath.toFile())) {

            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, pdfOutputStream);
            pdfDocument.open();

            String line;
            while ((line = reader.readLine()) != null) {
                pdfDocument.add(new Paragraph(line));
            }
            pdfDocument.close();

            ConversionResult result = new ConversionResult("File converted successfully", outputPath.toString());
            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            ConversionResult result = new ConversionResult("Failed to convert file: " + e.getMessage(), null);
            return CompletableFuture.completedFuture(result);
        }
    }

    @Async
    public CompletableFuture<ConversionResult> convertImageToPdf(File inputFile) {
        String fileName = inputFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String outputFileName = baseName + ".pdf";
        Path outputPath = Paths.get(outputFileName);

        try (InputStream imageInputStream = new FileInputStream(inputFile);
             OutputStream pdfOutputStream = new FileOutputStream(outputPath.toFile())) {

            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, pdfOutputStream);
            pdfDocument.open();

            Image img = Image.getInstance(imageInputStream.readAllBytes());
            pdfDocument.add(img);
            pdfDocument.close();

            ConversionResult result = new ConversionResult("File converted successfully", outputPath.toString());
            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            ConversionResult result = new ConversionResult("Failed to convert file: " + e.getMessage(), null);
            return CompletableFuture.completedFuture(result);
        }

        // Add other conversion methods as needed
    }
}
