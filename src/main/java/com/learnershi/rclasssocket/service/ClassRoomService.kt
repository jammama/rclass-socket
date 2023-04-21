package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.controller.EnvelopSendService
import com.learnershi.rclasssocket.entity.*
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
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
    private val canvasDrawRepository: CanvasDrawRepository,
    private val classRoomLogRepository: ClassRoomLogRepository,
    private val studyDataMapRepository: StudyDataMapRepository,
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


    fun createClassRoom(userSeq: String, userName: String): Mono<ClassRoom> {
        log.info("create class - {} {}", userSeq, userName)
        val newClassRoom = ClassRoom(
            teacherSeq = userSeq,
            teacherName = userName,
            roomState = ClassState.WAIT,
            startDate = LocalDateTime.now()
        )
        return classRoomRepository.save(newClassRoom)
    }

    /**
     * ClassRoom을 조회한다
     *
     * @param classRoomId 클래스룸 아이디
     * @return Mono<ClassRoom> 클래스룸
     */
    fun getClassRoom(classRoomId: String): Mono<ClassRoom?> {
        return classRoomRepository.findById(classRoomId).switchIfEmpty { Mono.error(RuntimeException("not found")) }
    }

    /**
     * ClassRoom 내 접속중인 유저 목록을 조회한다.
     *
     * @param classRoomId 클래스룸 아이디
     * @return Mono<Collections<User>> 유저 목록
     */
    fun getClassRoomSessionUsers(classRoomId: String): Mono<Collection<User>?> {
        return Mono.just(classUserSessionsRepository.findByClassRoomId(classRoomId).map { it.user })
    }

    /**
     * studyData정보 조회
     *
     * @param classRoomId : 클래스룸 아이디
     */
    fun getStudyDataMap(classRoomId: String): Mono<ClassStudyDataMap?> {
        return studyDataMapRepository.findById(classRoomId).switchIfEmpty { createStudyDataMap(classRoomId) }
    }

    /**
     * studyData 생성
     */
    fun createStudyDataMap(classRoomId: String): Mono<ClassStudyDataMap?> {
        return studyDataMapRepository.save(ClassStudyDataMap(classRoomId))
    }

    /**
     * ClassRoom을 수정한다.
     *
     * @param classRoomId 클래스룸 아이디
     * @param patchRoom 수정할 클래스룸
     * @return Mono<ServerResponse> 응답
     */

    fun updateClassRoom(classRoomId: String, patchRoom: ClassRoom): Mono<ClassRoom> {
        return classRoomRepository.findById(classRoomId)
            .defaultIfEmpty(ClassRoom())
            .flatMap { classRoom ->
                classRoomRepository.save(classRoom!!.modify(patchRoom))
                    .doOnSuccess { r ->
                        envelopSendService.sendMessageQueue(
                            Envelop(
                                msgType = MessageType.CLASS_CHANGE,
                                classRoomId = classRoomId,
                                data = r,
                                userType = UserType.ALL
                            )
                        )
                    }
            }
    }

    fun getCanvasDrawPathList(classRoomId: String): Mono<MutableList<CanvasDraw?>> {
        return canvasDrawRepository.findAllByClassRoomId(classRoomId)
            .collectList().defaultIfEmpty(emptyList())
    }

    /**
     * 저장된 log List전송
     *
     * @param classRoomId 클래스룸 아이디
     * @return Mono<ServerResponse> 결과
    </ServerResponse> */
    fun getClassLogList(classRoomId: String): Mono<MutableList<ClassRoomLog?>> {
        return classRoomLogRepository.findAllByClassRoomId(classRoomId).collectList()
    }

    /**
     * 저장된 포스트잇 조회
     * @param classRoomId : 클래스룸 아이디
     * @param tabIndex : 탭 인덱스
     * @param pageIndex : 페이지 인덱스
     * @return Mono<PostIt?> 해당 페이지의 포스트잇
     */
    fun getMemo(classRoomId: String, tabIndex: Int, pageIndex: Int): Mono<PostIt?> {
        return postItRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, tabIndex, pageIndex)
    }

    /**
     * 포스트잇 저장 | 수정 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param postIt : 포스트잇 정보
     */
//    fun sendPostIt(classRoomId: String, postIt: PostIt): Mono<PostIt?> {
//        return postItRepository.findByClassRoomIdAndTabIndexAndPageIndex(
//            classRoomId,
//            postIt.tabIndex,
//            postIt.pageIndex
//        )
//            .defaultIfEmpty(postIt.apply { classRoomId })
//            .map ( find -> find?.apply { data = postIt.data } )
//            .flatMap { postItRepository::save }
//            .flatMap { saved ->
//                envelopSendService.sendMessageQueue(classRoomId, MessageType.MEMO, saved)
//            }
//    }

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