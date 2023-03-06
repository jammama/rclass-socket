package com.learnershi.rclasssocket.entity.common;

import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.function.Consumer;

public class RequestHeadersBuilder {

    private static final String HEADER_TOTAL_COUNT = "X-Total-Count";
    private static final String HEADER_LIMIT = "X-Limit";
    private static final String HEADER_LINK = "Link";

    private Request request = new Request();

    public static RequestHeadersBuilder getInstance() {
        return new RequestHeadersBuilder();
    }

    public RequestHeadersBuilder request(Request request) {
        this.request
                .setRequest(request.getRequest())
                .setDateFrom(request.getDateFrom())
                .setDateTo(request.getDateTo())
                .setOffset(request.getOffset())
                .setLimit(request.getLimit());

        return this;
    }

    public RequestHeadersBuilder count(Long count) {
        this.request.setCount(count);
        return this;
    }

    public Consumer<HttpHeaders> build() {
        return httpHeaders -> {
            // 전체 페이지 수 헤더 세팅
            if (request.getCount() != null) {
                httpHeaders.set(HEADER_TOTAL_COUNT, String.valueOf(request.getCount()));
            }
            // 조회 제한 수 세팅
            if (request.getLimit() != null) {
                httpHeaders.set(HEADER_LIMIT, String.valueOf(request.getLimit()));
            }

            // Offset 이 포함되어 있고 다음 페이지가 존재할 경우 추가
            if (request.getOffset() != null && request.getLimit() != null &&
                    (request.getOffset() + request.getLimit()) < request.getCount()) {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>(request.getRequest().queryParams());
                params.set("offset", String.format("%d", request.getOffset() + request.getLimit()));

                UriComponents uriComponents = UriComponentsBuilder.newInstance()
                        .scheme(request.getRequest().uri().getScheme())
                        .host(request.getRequest().uri().getHost())
                        .port(request.getRequest().uri().getPort())
                        .path(request.getRequest().uri().getPath())
                        .queryParams(params).build();

                httpHeaders.set(HEADER_LINK, String.format("<%s>; rel=\"next\"", uriComponents.toUriString()));
            }

        };
    }
}
