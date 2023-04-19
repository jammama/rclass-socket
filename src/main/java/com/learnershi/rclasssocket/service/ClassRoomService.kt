package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.controller.EnvelopSendService
import com.learnershi.rclasssocket.entity.Activity
import com.learnershi.rclasssocket.entity.ClassRoom
import com.learnershi.rclasssocket.entity.Reveal
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.common.ServerResult
import com.learnershi.rclasssocket.entity.enums.ClassState
import com.learnershi.rclasssocket.entity.enums.MessageType
import com.learnershi.rclasssocket.entity.enums.MiniWindowType
import com.learnershi.rclasssocket.entity.enums.UserType
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.repository.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
    private val canvasDrawRepository: CanvasDrawRepository,
    private val classRoomLogRepository: ClassRoomLogRepository,
    private val classRoomStudyDataMapRepository: ClassRoomStudyDataMapRepository,
    private val activityRepository: ActivityRepository,
    private val classGoalRepository: ClassGoalRepository,
    private val revealRepository: RevealRepository,
    private val postItRepository: PostItRepository,
    private val badgeRepository: BadgeRepository,
    private val comprehensionAnswerRepository: ComprehensionAnswerRepository,
    private val classUserSessionsRepository: ClassUserSessionsRepository,
    private val envelopSendService: EnvelopSendService
) {
    private val log = Log.of(this.javaClass)

    /**
     * ClassRoom을 새로 생성한다.
     *
     * @param userSeq 사용자 시퀀스
     * @return Mono<ServerResponse> 응답
     */
    fun createClassRoom(request: ServerRequest): Mono<ServerResponse> {
        val userSeq = request.pathVariable("userSeq")
        val userName = request.queryParam("userName").orElse("익명의 강사")
        log.info("create class - {} {}", userSeq, userName)
        val newClassRoom = ClassRoom(
            teacherSeq = userSeq,
            teacherName = userName,
            roomState = ClassState.WAIT,
            startDate = LocalDateTime.now()
        )
        return classRoomRepository.save(newClassRoom).flatMap { r -> ServerResult.success().data(r).build() }
    }

    /**
     * ClassRoom를 조회한다
     *
     * @param classRoomId 클래스룸 아이디
     * @return Mono<ServerResponse> 응답
     */
    fun getClassRoom(request: ServerRequest): Mono<ServerResponse> {
        val classRoomId = request.pathVariable("classRoomId")
        log.info("get class - {}", classRoomId)
        return classRoomRepository.findById(classRoomId).flatMap { r -> ServerResult.success().data(r).build() }
    }


    /**
     * ClassRoom을 수정한다.
     *
     * @param classRoomId 클래스룸 아이디
     * @param patchRoom 수정할 클래스룸
     * @return Mono<ServerResponse> 응답
     */
    fun updateClassRoom(request: ServerRequest): Mono<ServerResponse> {
        val classRoomId = request.pathVariable("classRoomId")
        val patchRoom = request.bodyToMono<ClassRoom>()
        return classRoomRepository.findById(classRoomId)
            .defaultIfEmpty(ClassRoom())
            .flatMap { classRoom ->
                patchRoom.flatMap {
                    classRoomRepository.save( classRoom!!.modify(it) )
                        .doOnSuccess {
                        r -> envelopSendService.sendMessageQueue(
                        Envelop(
                            msgType = MessageType.PATCH_ROOM,
                            classRoomId = classRoomId,
                            data = r,
                            userType = UserType.ALL
                        ))
                    }.flatMap { r ->
                        ServerResult.success().data(r).build()
                    }
                }
            }
    }

    /**
     * 저장된 draw path List전송
     *
     * @param classRoomId 클래스룸 아이디
     * @return Mono<ServerResponse> 결과
    </ServerResponse> */
    fun sendCanvasDrawPathList(request: ServerRequest): Mono<ServerResponse> {
        val classRoomId = request.pathVariable("classRoomId")
        return canvasDrawRepository.findAllByClassRoomId(classRoomId)
            .collectList().flatMap { r -> ServerResult.success().data(r).build() }
    }

    /**
     * 저장된 log List전송
     *
     * @param classRoomId 클래스룸 아이디
     * @return Mono<ServerResponse> 결과
    </ServerResponse> */
    fun getClassLogList(classRoomId: String): Mono<ServerResponse> {
        return classRoomLogRepository.findAllByClassRoomId(classRoomId).collectList()
            .flatMap { r -> ServerResult.success().data(r).build() }
    }

    /**
     * miniWindow Activity 기록 저장
     *
     * @param classRoomId : 클래스룸 아이디
     * @param miniWindowType : miniWindow 타입
     * @param entity : 저장할 entity
     * @return Mono<ServerResponse> 응답
    </ServerResponse> */
    fun saveActivityLog(classRoomId: String, miniWindowType: String, entity: Any): Mono<ServerResponse>? {
        return activityRepository.save(
            Activity().apply {
                this.classRoomId = classRoomId
                this.miniWindowType = MiniWindowType.findByValue(miniWindowType)
                this.endTime = LocalDateTime.now()
                this.entity = entity
            }
        ).flatMap { activity: Any? -> ServerResult.success().data(activity).build() }
    }

    /**
     * miniWindow Activity 기록 조회
     *
     * @param classRoomId : 클래스룸 아이디
     * @param page : 페이지
     * @param size : 사이즈
     * @return Mono<ServerResponse> 응답
    </ServerResponse> */
    fun getActivityLogs(classRoomId: String, page: Int, size: Int): Mono<ServerResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "endTime")
        return activityRepository.countByClassRoomId(classRoomId)
            .zipWith(
                activityRepository.findAllByClassRoomId(classRoomId, pageable)
                    .collectList()
            )
            .flatMap { activity ->
                ServerResult.success()
                    .data(PageImpl(activity.getT2(), Pageable.unpaged(), activity.getT1()))
                    .build()
            }
    }

    /**
     * studyRoom 내의 userList 조회
     *
     * @param classRoomId : 클래스룸 아이디
     * @return Mono<ServerResponse> 응답
    </ServerResponse> */
    fun getUserList(classRoomId: String): Mono<ServerResponse> {
        return ServerResult.success().data(classUserSessionsRepository.findByClassRoomId(classRoomId)).build()
    }

    /**
     * studyRoom 정보 조회
     *
     * @param classRoomId : 클래스룸 아이디
     */
    fun getClassRoomInfo(classRoomId: String): Mono<ServerResponse> {
        return classRoomRepository.findById(classRoomId)
            .flatMap { classRoom -> ServerResult.success().data(classRoom).build() }
    }

    /**
     * studyData정보 조회
     *
     * @param classRoomId : 클래스룸 아이디
     */
    fun getStudyData(classRoomId: String): Mono<ServerResponse> {
        return classRoomStudyDataMapRepository.findById(classRoomId)
            .flatMap { studyData -> ServerResult.success().data(studyData).build() }
    }


    /**
     * classGoal정보 조회
     *
     * @param classRoomId : 클래스룸 아이디
     */
    fun getClassGoalList(classRoomId: String): Mono<ServerResponse> {
        return classGoalRepository.findAllByClassRoomId(classRoomId)
            .collectList()
            .flatMap { classGoalList -> ServerResult.success().data(classGoalList).build() }
    }

    /**
     * 가리기 임시 저장 및 수정
     *
     * @param classRoomId : 클래스룸 아이디
     * @param reveal : 가리기 정보
     * @return Mono<ServerResponse> 응답
    </ServerResponse> */
    fun saveReveal(classRoomId: String, reveal: Reveal): Mono<ServerResponse?> {
        return revealRepository.findByClassRoomIdAndTabIndexAndPageIndex(
            classRoomId,
            reveal.tabIndex,
            reveal.pageIndex
        )
            .defaultIfEmpty(reveal)
            .map { r -> r!!.apply { tmpData = reveal.tmpData } }
            .flatMap { r -> revealRepository.save(r) }
            .flatMap { saved -> ServerResult.success().data(saved).build() }
    }

    /**
     * 가리기 조회
     *
     * @param classRoomId : 클래스룸 아이디
     * @param tabIndex : 탭 인덱스
     * @param pageIndex : 페이지 인덱스
     * @return Mono<ServerResponse> 응답
    </ServerResponse> */
    fun getReveal(classRoomId: String, tabIndex: Int, pageIndex: Int): Mono<ServerResponse?>? {
        return revealRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, tabIndex, pageIndex)
            .flatMap { find -> ServerResult.success().data(find).build() }
    }

    /**
     * 포스트잇 조회
     *
     * @param classRoomId : 클래스룸 아이디
     * @param tabIndex : 탭 인덱스
     * @param pageIndex : 페이지 인덱스
     * @return Mono<ServerResponse> 응답
    </ServerResponse> */
    fun getPostIt(classRoomId: String, tabIndex: Int, pageIndex: Int): Mono<ServerResponse?>? {
        return postItRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, tabIndex, pageIndex)
            .flatMap { find -> ServerResult.success().data(find).build() }
    }

    /**
     * 클래스 룸 내의 배지 목록 조회
     * @param classRoomId : 클래스룸 아이디
     * @return Mono<ServerResponse> 응답 - badgeList : 배지 목록
    </ServerResponse> */
    fun getBadgeByClassRoom(classRoomId: String): Mono<ServerResponse?>? {
        return badgeRepository.findAllByClassRoomId(classRoomId)
            .collectList()
            .flatMap { badgeList -> ServerResult.success().data(badgeList).build() }
    }

    /**
     * 유저의 배지 목록 조회
     * @param userSeq : 유저 Seq
     * @return Mono<ServerResponse> 응답 - badgeList : 배지 목록
    </ServerResponse> */
    fun getBadgeByUser(userSeq: String): Mono<ServerResponse?>? {
        return badgeRepository.findAllByUserSeq(userSeq)
            .collectList()
            .flatMap { badgeList -> ServerResult.success().data(badgeList).build() }
    }

    /**
     * 설문 답안 목록 조회
     * @param classRoomId : 클래스룸 아이디
     * @return Mono<ServerResponse> 응답 - {questionId: comprehensionAnswerList} : 설문 답안 목록
    </ServerResponse> */
    fun getComprehensionAnswerList(classRoomId: String): Mono<ServerResponse?>? {
        return comprehensionAnswerRepository.findAllByClassRoomId(classRoomId)
            .collectList()
            .map { comprehensionAnswers ->
                comprehensionAnswers.stream().collect(Collectors.groupingBy { r -> r!!.id })
            }
            .flatMap { comprehensionAnswers -> ServerResult.success().data(comprehensionAnswers).build() }
    }
}