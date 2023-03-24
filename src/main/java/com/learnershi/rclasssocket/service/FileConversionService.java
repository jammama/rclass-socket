package com.learnershi.rclasssocket.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * FileConversionService
 *
 */
@Service
@Slf4j
public class FileConversionService {

    @Value("${file.return-url}")
    private String returnUrl;

    @Value("${file.save-path}")
    private String savePath;

    /**
     * Image File Upload
     *
     * @param teacherId 선생님 아이디
     * @param classRoomId 강의실 아이디
     * @param multipartData 파일
     * @return Flux<String> 변환된 이미지 경로
     */
    public Flux<String> uploadImageFile(String teacherId, String classRoomId, MultiValueMap<String, Part> multipartData) {
        System.out.println("FileConversionService :: imageFileUpload :: ");

        String upload = "upload";
        String saveFilePath = getPath(savePath, teacherId, classRoomId, "studyData");
        String uploadFilePath = getPath(returnUrl, teacherId, classRoomId, "studyData");

        return Mono.just(multipartData)
                .map(it -> it.get(upload))
                .flatMapMany(Flux::fromIterable)
                .cast(FilePart.class)
                .flatMap(path -> uploadFile(path, saveFilePath))
                .map(file -> uploadFilePath + file.getName())
                .doOnError(e -> log.error("pdfFileUpload error", e));
    }

    private String getPath(String basePath, String teacherId, String classRoomId, String type) {
        return String.format("%s/%s/%s/", basePath + teacherId, classRoomId, type);
    }

    /**
     * 파일 업로드
     *
     * @param filePart 업로드 될 파일
     *
     */
    public Mono<File> uploadFile(FilePart filePart, String teacherId, String classRoomId){
        String saveFilePath = getPath(savePath, teacherId, classRoomId, "studyData");
        return uploadFile(filePart, saveFilePath);
    }

    /**
     * 파일 업로드
     *
     * @param filePart 업로드 될 파일
     * @param saveFilePath 저장될 경로
     * @return 저장된 파일
     */
    // TODO: to NonBlocking
    public Mono<File> uploadFile(FilePart filePart, String saveFilePath) {
        try {
            // Create file path when no path exists
            if (!Files.exists(Paths.get(saveFilePath))) {
                Files.createDirectories(Paths.get(saveFilePath));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 파일 저장
        File uploadFile = new File(saveFilePath + UUID.randomUUID());
        return filePart.transferTo(uploadFile).thenReturn(uploadFile);
    }

    /**
     * 업로드 된 파일 경로 반환
     *
     * @param file 업로드 된 파일
     * @return 업로드 된 파일 경로
     *
     */
    public String getUploadFilePath(File file, String teacherId, String classRoomId){
        String uploadFilePath = getPath(returnUrl, teacherId, classRoomId, "studyData");
        return uploadFilePath + file.getName();
    }

    /**
     * PDF 파일을 이미지로 변환
     *
     * @param uploadFile 변환할 파일
     * @param teacherId 선생님 아이디
     * @param classRoomId 강의실 아이디
     * @return 변환된 이미지 경로
     */
    public String convertPdfToImg(File uploadFile, String teacherId, String classRoomId) {
        String saveFilePath = getPath(savePath, teacherId, classRoomId, "studyData");
        String uploadFilePath = getPath(returnUrl, teacherId, classRoomId, "studyData");
        String id = UUID.randomUUID().toString();

        return convertPdfToImg(uploadFile, saveFilePath, uploadFilePath, id);
    }

    /**
     * PDF 파일을 이미지로 변환
     *
     * @param uploadFile 변환할 파일
     * @param saveFilePath 저장할 경로
     * @param uploadPath 저장된 경로
     * @param id 파일명
     *
     * @return 변환된 이미지 경로
     */
    private String convertPdfToImg(File uploadFile, String saveFilePath, String uploadPath, String id) {
        List<String> jpgPathList = new ArrayList<>();

        try {
            PDDocument document = PDDocument.load(uploadFile);
            int pageCount = document.getNumberOfPages();

            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int i = 0; i < pageCount; i++) {
                log.info(id + "_" + i + ".png");
                // 변환 결과 파일 생성
                File outputFile = new File(saveFilePath + id + "_" + i + ".png");
                // 저장된 PDF 파일을 이미지로 변환
                RenderedImage renderedImage = pdfRenderer.renderImageWithDPI(i, 130, ImageType.RGB);
                ImageIO.write(renderedImage, "png", outputFile);
                jpgPathList.add(uploadPath + id + "_" + i + ".png");
            }
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
        return jpgPathList.toString();
    }

}