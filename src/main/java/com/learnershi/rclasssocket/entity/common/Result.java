package com.learnershi.rclasssocket.entity.common;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * API JSON 결과 포맷 Entity
 *
 * @author dualcat
 */
@Slf4j
public class Result extends LinkedHashMap<String, Object> {

    Marker RESULT_MARKER = MarkerFactory.getMarker("[RESULT]");

    private static final Integer RESULT_SUCCESS = 1000;
    private static final Integer RESULT_FAIL = 3000;

    private static final String KEY_STATUS_CODE = "status_code";
    private static final String KEY_STATUS_MESSAGE = "status_message";
    private static final String KEY_DATA = "data";
    private static final String KEY_TIMESTAMP = "timestamp";
    private Long count;
    private Object data;
    private String message;
    private Publisher publisher;
    private Class publisherClass;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static Result success() {
        return buildResult(RESULT_SUCCESS);
    }

    public static Result fail() {
        return buildResult(RESULT_FAIL);
    }

    /**
     * 최종 Response 객체 생성
     *
     * @return Mono<ServerResponse>
     */
    public Mono<ServerResponse> build() {
        ServerResponse.BodyBuilder bodyBuilder = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON);

        // Message 값이 존재할 경우 기본값 덮어쓰기
        if (message != null) {
            this.put(KEY_STATUS_MESSAGE, message);
        }

        // Data 설정. null 일 경우 빈값('{}')
        if (data != null) {
            this.put(KEY_DATA, data);
        } else {
            this.put(KEY_DATA, new HashMap());
        }


        this.put(KEY_TIMESTAMP, getCurrentTime());

        // Sort
        Map result = this.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        if (publisher != null) {

            return bodyBuilder.body(BodyInserters.fromPublisher(publisher, publisherClass));
        } else {
            return bodyBuilder.body(BodyInserters.fromValue(result));
        }
    }

    /**
     * 기본 Result 객체 생성
     *
     * @param result 결과코드
     * @return Result
     */
    private static Result buildResult(Integer result) {
        Result returnResult = new Result();
        returnResult.put(KEY_STATUS_CODE, result);

        if (RESULT_SUCCESS.equals(result)) {
            returnResult.put(KEY_STATUS_MESSAGE, "Success");
        } else if (RESULT_FAIL.equals(result)) {
            returnResult.put(KEY_STATUS_MESSAGE, "Fail");
        }
        return returnResult;
    }


    private static String getCurrentTime() {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public Result count(Long count) {
        this.count = count;
        return this;
    }

    public Result data(Object data) {
        this.data = data;
        return this;
    }

    public Result producer(Publisher publisher, Class clazz) {
        this.publisher = publisher;
        this.publisherClass = clazz;
        return this;
    }

    public Result message(String message) {
        this.message = message;
        return this;
    }

    public Object put(String key, Object value) {
        super.put(key, value);
        return value;
    }
}
