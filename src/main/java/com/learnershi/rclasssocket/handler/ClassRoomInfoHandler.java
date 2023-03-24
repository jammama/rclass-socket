package com.learnershi.rclasssocket.handler;


import com.learnershi.rclasssocket.entity.*;
import com.learnershi.rclasssocket.entity.common.Result;
import com.learnershi.rclasssocket.entity.enums.MiniWindowType;
import com.learnershi.rclasssocket.entity.enums.UserType;
import com.learnershi.rclasssocket.repository.*;
import com.learnershi.rclasssocket.service.FileConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * ClassRoom 관련 정보 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClassRoomInfoHandler extends DefaultHandler {
    private final CanvasDrawRepository canvasDrawRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ClassRoomLogRepository classRoomLogRepository;
    private final ClassRoomUserRepository classRoomUserRepository;
    private final UserClassRoomsRepository userClassRoomsRepository;
    private final ClassRoomStudyDataMapRepository classRoomStudyDataMapRepository;
    private final ActivityRepository activityRepository;
    private final ClassGoalRepository classGoalRepository;
    private final RevealRepository revealRepository;
    private final PostItRepository postItRepository;
    private final BadgeRepository badgeRepository;
    private final FileConversionService fileConversionService;
    private final ComprehensionAnswerRepository comprehensionAnswerRepository;

    /**
     * pdf File Upload
     *
     * @param request 요청
     */
    public Mono<ServerResponse> pdfFileUpload(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        String teacherId = request.pathVariable("teacherId");
        String upload = "upload";

        Flux<String> fileUploadResult = request.multipartData()
                .map(it -> it.get(upload))
                .flatMapMany(Flux::fromIterable)
                .cast(FilePart.class)
                .flatMap(path -> fileConversionService.uploadFile(path, classRoomId, teacherId))
                .map(file -> fileConversionService.convertPdfToImg(file, classRoomId, teacherId))
                .doOnError(e -> log.error("pdfFileUpload :: {}", e.getMessage()));

        return Result.success().producer(fileUploadResult, String.class).build();
    }

    /**
     * Image File Upload
     *
     * @param request 요청
     */
    public Mono<ServerResponse> fileUpload(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        String teacherId = request.pathVariable("teacherId");
        String upload = "upload";

        Flux<String> fileUploadResult = request.multipartData()
                .map(it -> it.get(upload))
                .flatMapMany(Flux::fromIterable)
                .cast(FilePart.class)
                .flatMap(path -> fileConversionService.uploadFile(path, classRoomId, teacherId))
                .map(file -> fileConversionService.getUploadFilePath(file, classRoomId, teacherId))
                .doOnError(e -> log.error("imageFileUpload :: {}", e.getMessage()));

        return Result.success().producer(fileUploadResult, String.class).build();
    }
    /**
     * 저장된 draw path List전송
     *
     * @param request 요청
     * @return Mono<ServerResponse> 결과
     */
    public Mono<ServerResponse> sendCanvasDrawPathList(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        // DB 내 classRoomId에 해당하는 모든 draw path를 가져온다.
        Mono<List<CanvasDraw>> listMono = canvasDrawRepository.findAllByClassRoomId(classRoomId).collectList();
        return Result.success().producer(listMono, Object.class).build();
    }

    /**
     * 저장된 log List전송
     *
     * @param request 요청
     * @return Mono<ServerResponse> 결과
     */
    public Mono<ServerResponse> getClassLogList(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        // DB 내 classRoomId에 해당하는 모든 LogList를 가져온다.
        Mono<List<ClassRoomLog>> listMono = classRoomLogRepository.findAllByClassRoomId(classRoomId).collectList();
        return Result.success().producer(listMono, Object.class).build();
    }

    /**
     * 유저별 ClassRoom 목록 조회.
     *
     * @param request 요청   - userSeq : 유저 seq
     *                - page : 목록 페이지 (1~)
     *                - size : 페이지당 목록 수
     * @return Mono<ServerResponse> 응답
     */
    public Mono<ServerResponse> getClassRoomList(ServerRequest request) {
        String userId = request.pathVariable("userSeq");
        boolean isSearchTeacher = UserType.T.toString().equals(request.pathVariable("userType"));
        Optional<String> page = request.queryParam("page");
        Optional<String> size = request.queryParam("size");

        return userClassRoomsRepository.findByUserSeq(userId).flatMap(
                userClassRooms -> {
                    List<String> classRoomIds = userClassRooms.getClassRooms().stream().filter(
                            classRoomInfo -> classRoomInfo.getTeacherSeq().equals(userId) == isSearchTeacher
                    ).map(ClassRoomInfo::getId).collect(Collectors.toList());
                    // 페이징 처리
                    int pageIdx = Integer.parseInt(page.orElse("1"));
                    int sizeIdx = Math.min(classRoomIds.size(), Integer.parseInt(size.orElse("10")));

                    int startIdx = (pageIdx - 1) * sizeIdx;
                    int endIdx = Math.min(startIdx + sizeIdx, classRoomIds.size());

                    List<String> searchIds = classRoomIds.subList(startIdx, endIdx);

                    return classRoomRepository.findAllById(searchIds)
                            // classRoom 내 유저 목록 추가
                            .map(classRoom -> {
//                                classRoom.setUsers(classRoomUserRepository.findByClassRoomId(classRoom.getId()));
                                return classRoom;
                            }).collectList().flatMap(
                                    classRoomList -> {
                                        classRoomList.sort(Comparator.comparingInt(r -> searchIds.indexOf(r.getId())));
                                        Page<ClassRoom> pagingList = new PageImpl<>(classRoomList, Pageable.unpaged(), classRoomIds.size());
                                        return Result.success().data(pagingList).build();
                                    }
                            );
                }
        );

    }

    /**
     * miniWindow Activity 기록 저장
     *
     * @param request 요청
     *                - classRoomId : 클래스룸 아이디
     *                - Activity : Activity 객체
     * @return Mono<ServerResponse> 응답
     */
    public Mono<ServerResponse> saveActivityLog(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        String miniWindowType = request.pathVariable("miniWindowType");
        return request.bodyToMono(Object.class)
                .flatMap(entity -> activityRepository.save(
                        new Activity(MiniWindowType.findByValue(miniWindowType), classRoomId, entity)
                ))
                .flatMap(activity -> Result.success().data(activity).build());
    }

    /**
     * miniWindow Activity 기록 저장
     *
     * @param request 요청
     *                - classRoomId : 클래스룸 아이디
     *                - ClassRoomEntity : ClassRoomEntity 객체
     * @return Mono<ServerResponse> 응답
     */
    public Mono<ServerResponse> getActivityLogs(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        String page = request.queryParam("page").orElse("1");
        String size = request.queryParam("size").orElse("10");
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1,
                Integer.parseInt(size), DESC, "endTime");
        return activityRepository.countByClassRoomId(classRoomId)
                .zipWith(activityRepository.findAllByClassRoomId(classRoomId, pageable)
                        .collectList())
                .flatMap(activity -> Result.success().data(new PageImpl<>(activity.getT2(), Pageable.unpaged(), activity.getT1())).build());
    }

    /**
     * studyRoom 내의 userList 조회
     *
     * @param request 요청 - classRoomId : 클래스룸 아이디
     * @return Mono<ServerResponse> 응답
     */
    public Mono<ServerResponse> getUserList(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        return Result.success().data(classRoomUserRepository.findByClassRoomId(classRoomId)).build();
    }

    /**
     * studyRoom 정보 조회
     *
     * @param request 요청 - classRoomId : 클래스룸 아이디
     */
    public Mono<ServerResponse> getClassRoomInfo(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        return classRoomRepository.findById(classRoomId)
                .flatMap(classRoom ->
                        Result.success().data(classRoom).build()
                );
    }

    /**
     * studyData정보 조회
     *
     * @param request 요청 - classRoomId : 클래스룸 아이디
     */
    public Mono<ServerResponse> getStudyData(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        return classRoomStudyDataMapRepository.findById(classRoomId)
                .flatMap(studyData ->
                        Result.success().data(studyData).build()
                );
    }


    /**
     * classGoal정보 조회
     *
     * @param request 요청 - classRoomId : 클래스룸 아이디
     */
    public Mono<ServerResponse> getClassGoalList(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        return classGoalRepository.findAllByClassRoomId(classRoomId)
                .collectList()
                .flatMap(classGoalList ->
                        Result.success().data(classGoalList).build()
                );
    }

    /**
     * 가리기 임시 저장 및 수정
     *
     * @param request 요청
     *                - classRoomId : 클래스룸 아이디
     *                - Reveal : 학습 목표
     * @return Mono<ServerResponse> 응답
     */
    public Mono<ServerResponse> saveReveal(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        return request.bodyToMono(Reveal.class)
                .flatMap(reveal ->
                        revealRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, reveal.getTabIndex(), reveal.getPageIndex())
                                .defaultIfEmpty(reveal)
                                .map(r -> r.setTmpData(reveal.getTmpData())))
                .flatMap(revealRepository::save)
                .flatMap(saved -> Result.success().data(saved).build());
    }

    /**
     * 가리기 조회
     *
     * @param request 요청
     *                - classRoomId : 클래스룸 아이디
     *                - Reveal : 학습 목표
     * @return Mono<ServerResponse> 응답
     */
    public Mono<ServerResponse> getReveal(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        int tabIndex = Integer.parseInt(request.pathVariable("tabIndex"));
        int pageIndex = Integer.parseInt(request.pathVariable("pageIndex"));
        return revealRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, tabIndex, pageIndex)
                                .flatMap(find -> Result.success().data(find).build());
    }

    /**
     * 포스트잇 조회
     *
     * @param request 요청
     *                - classRoomId : 클래스룸 아이디
     *                - postIt : 포스트잇
     * @return Mono<ServerResponse> 응답
     */
    public Mono<ServerResponse> getPostIt(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        int tabIndex = Integer.parseInt(request.pathVariable("tabIndex"));
        int pageIndex = Integer.parseInt(request.pathVariable("pageIndex"));
        return postItRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, tabIndex, pageIndex)
                .flatMap(find -> Result.success().data(find).build());
    }

    /**
     * 클래스 룸 내의 배지 목록 조회
     * @param request 요청
     *                - classRoomId : 클래스룸 아이디
     * @return Mono<ServerResponse> 응답 - badgeList : 배지 목록
     */
    public Mono<ServerResponse> getBadgeByClassRoom(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        return badgeRepository.findAllByClassRoomId(classRoomId)
                .collectList()
                .flatMap(badgeList -> Result.success().data(badgeList).build());
    }

    /**
     * 유저의 배지 목록 조회
     * @param request 요청
     *                - userSeq : 유저 Seq
     * @return Mono<ServerResponse> 응답 - badgeList : 배지 목록
     */
    public Mono<ServerResponse> getBadgeByUser(ServerRequest request) {
        String userSeq = request.pathVariable("userSeq");
        return badgeRepository.findAllByUserSeq(userSeq)
                .collectList()
                .flatMap(badgeList -> Result.success().data(badgeList).build());
    }

    /**
     * 설문 답안 목록 조회
     * @param request 요청
     *                - classRoomId : 클래스룸 아이디
     * @return Mono<ServerResponse> 응답 - {questionId: comprehensionAnswerList} : 설문 답안 목록
     */
    public Mono<ServerResponse> getComprehensionAnswerList(ServerRequest request) {
        String classRoomId = request.pathVariable("classRoomId");
        return comprehensionAnswerRepository.findAllByClassRoomId(classRoomId)
                .collectList()
                .map(comprehensionAnswers -> comprehensionAnswers.stream()
                        .collect(Collectors.groupingBy(ComprehensionAnswer::getId)))
                .flatMap(comprehensionAnswers -> Result.success().data(comprehensionAnswers).build());
    }
}