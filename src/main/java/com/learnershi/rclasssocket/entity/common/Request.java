package com.learnershi.rclasssocket.entity.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;

@Data
@Accessors(chain = true)
public class Request {

    private static final String DATE_FROM = "date_from";
    private static final String DATE_TO = "date_to";
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    // for Admin
    private static final String FILTER_PUBLISHED = "published";
    private static final String FILTER_ID = "id";
    private static final String FILTER_NAME = "name";
    private static final String FILTER_ADDRESS = "address";
    private static final String FILTER_ACCESS = "access";

    // Admin 접근 여부
    private boolean isAdmin;
    // PathVariable 여부 (Single Query)
    private boolean isSingleQuery;

    // Location ID
    private String locationId;
    // EVSE ID
    private String evseUid;
    // Connector Id
    private String connectorId;
    // Opening Id
    private String openingId;

    public Request() {
    }

    public Request(ServerRequest request) {
        this.request = request;

        // 관리자 접근 확인
        isAdmin = request.path().startsWith("/cms");

        // Path variable 설정
        if (request.pathVariables().containsKey("location_id")) {
            locationId = request.pathVariable("location_id");
        }

        if (request.pathVariables().containsKey("evse_uid")) {
            evseUid = request.pathVariable("evse_uid");
        }

        if (request.pathVariables().containsKey("connector_id")) {
            connectorId = request.pathVariable("connector_id");
        }

        if (request.pathVariables().containsKey("opening_id")) {
            openingId = request.pathVariable("opening_id");
        }

        this.isSingleQuery = !(StringUtils.isEmpty(locationId) && StringUtils.isEmpty(evseUid)
                && StringUtils.isEmpty(connectorId) && StringUtils.isEmpty(openingId));

        if (isContained(DATE_FROM)) {
            dateFrom = getValue(DATE_FROM);
        }
        if (isContained(DATE_TO)) {
            dateTo = getValue(DATE_TO);
        }
        if (isContained(OFFSET)) {
            offset = Integer.valueOf(getValue(OFFSET));
        }
        if (isContained(LIMIT)) {
            limit = Integer.valueOf(getValue(LIMIT));
        }

        // for Admin
        if (isContained(FILTER_PUBLISHED)) {
            published = Boolean.valueOf(getValue(FILTER_PUBLISHED));
        }
        if (isContained(FILTER_ID)) {
            id = getValue(FILTER_ID);
        }
        if (isContained(FILTER_NAME)) {
            name = getValue(FILTER_NAME);
        }
        if (isContained(FILTER_ADDRESS)) {
            address = getValue(FILTER_ADDRESS);
        }
        if (isContained(FILTER_ACCESS)) {
            access = getValue(FILTER_ACCESS);
        }
    }

    private ServerRequest request;
    // Optional - Only return Locations that have last_updated before this Date/Time.
    private String dateFrom;
    // Optional - Only return Locations that have last_updated before this Date/Time.
    private String dateTo;
    // Optional - The offset of the first object returned. Default is 0.
    private Integer offset;
    // Optional - Maximum number of objects to GET.
    private Integer limit;
    private Long count;

    // for Admin
    private Boolean published;
    private String id;
    private String name;
    private String address;
    private String access;

    private boolean isContained(String param) {
        return request.queryParams().containsKey(param);
    }

    private String getValue(String param) {
        return request.queryParam(param)
                .map(s -> s.isEmpty() ? null : s).get();
    }

    // published 필드 사용여부
    private boolean isPublishedUse() {
        return request.path().contains("/locations");
    }

    @Override
    public String toString() {
        return "PoiRequest{" +
                "dateFrom='" + dateFrom + '\'' +
                ", dateTo='" + dateTo + '\'' +
                ", offset='" + offset + '\'' +
                ", limit='" + limit + '\'' +
                '}';
    }
}
