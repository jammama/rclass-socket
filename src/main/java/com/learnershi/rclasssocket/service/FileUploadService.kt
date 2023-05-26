package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.entity.enums.UploadStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

@Service
class FileUploadService {

    // 업로드 될 파일 경로
    @Value("\${file.upload.path}")
    private lateinit var uploadPath: Path

    // 업로드 파일 baseUrl
    @Value("\${file.upload.base-url}")
    private lateinit var baseUrl: String

    /**
     * 파일 업로드
     *
     * @param path 파일 경로
     * @param bufferFlux 파일 데이터
     * @return Flux<String> 파일 업로드 상태
     */
    fun uploadFile(path: Path, bufferFlux: Flux<DataBuffer>): Flux<String> {
        val opPath: Path = uploadPath.resolve(path)
        val channel = AsynchronousFileChannel.open(opPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)
        return DataBufferUtils.write(bufferFlux, channel)
            .map { UploadStatus.CHUNK_COMPLETED.toString() }
    }

    fun uploadPath(path: Path): String {
        return "$baseUrl/$path"
    }
}