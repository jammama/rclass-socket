package com.learnershi.rclasssocket.service

import com.learnershi.rclasssocket.controller.EnvelopSendService
import com.learnershi.rclasssocket.entity.*
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.enums.ClassState
import com.learnershi.rclasssocket.entity.enums.MessageType
import com.learnershi.rclasssocket.entity.enums.MiniWindowType
import com.learnershi.rclasssocket.entity.enums.UserType
import com.learnershi.rclasssocket.exception.BadRequestException
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.repository.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDateTime

@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
    private val canvasDrawRepository: CanvasDrawRepository,
    private val classRoomLogRepository: ClassRoomLogRepository,
    private val studyDataMapRepository: StudyDataMapRepository,
    private val classGoalRepository: ClassGoalRepository,
    private val revealRepository: RevealRepository,
    private val memoRepository: MemoRepository,
    private val badgeRepository: BadgeRepository,
    private val comprehensionAnswerRepository: ComprehensionAnswerRepository,
    private val classUserSessionsRepository: ClassUserSessionsRepository,
    private val envelopSendService: EnvelopSendService,
    private val activityRepository: ActivityRepository
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
        return classRoomRepository.findById(classRoomId).switchIfEmpty { Mono.error(BadRequestException("not found")) }
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
    fun getMemo(classRoomId: String, tabIndex: Int, pageIndex: Int): Mono<Memo?> {
        return memoRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, tabIndex, pageIndex)
    }

    /**
     * 포스트잇 저장 | 수정 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param memo : 포스트잇 정보
     * @return Mono<PostIt?> 저장된 포스트잇
     */
    fun sendMemo(classRoomId: String, memo: Memo): Mono<Memo?> {
        return memoRepository.findByClassRoomIdAndTabIndexAndPageIndex(
            classRoomId,
            memo.tabIndex,
            memo.pageIndex
        )
            .defaultIfEmpty(
                memo.apply {
                    this.classRoomId = classRoomId
                }
            )
            .map { it!!.modify(memo) }
            .flatMap { memoRepository.save(it) }
            .doOnSuccess {
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.MEMO,
                        data = it,
                        userType = UserType.STUDENT
                    )
                )
            }
    }

    /**
     * 포스트잇 삭제 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param memo : 포스트잇 정보
     * @return Mono<PostIt?> 삭제된 포스트잇
     */
    fun deleteMemo(classRoomId: String, memo: Memo): Mono<Memo?> {
        return memoRepository.findByClassRoomIdAndTabIndexAndPageIndex(
            classRoomId,
            memo.tabIndex,
            memo.pageIndex
        )
            .flatMap { it?.id?.let { it1 -> memoRepository.deleteById(it1).thenReturn(it) } }
            .doOnSuccess {
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.MEMO,
                        data = it.apply { data = emptyList() },
                        userType = UserType.STUDENT
                    )
                )
            }
    }

    /**
     * 학습 목표 조회
     * @param classRoomId : 클래스룸 아이디
     * @return Mono<MutableList<ClassGoal?>> 학습 목표 목록
     */
    fun getClassGoalList(classRoomId: String): Mono<MutableList<ClassGoal?>> {
        return classGoalRepository.findAllByClassRoomId(classRoomId)
            .collectList()
    }

    /**
     * 학습 목표 생성 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param classGoal : 학습 목표
     * @return Mono<ClassGoal?> 생성된 학습 목표
     */
    fun createClassGoal(classRoomId: String, classGoal: ClassGoal): Mono<ClassGoal?> {
        return classGoalRepository.save(classGoal)
            .doOnSuccess { saved ->
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.CLASS_GOAL,
                        data = saved,
                        userType = UserType.STUDENT
                    )
                )
            }
    }

    /**
     * 학습 목표 완료/비완료처리 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param classGoal : 학습 목표
     * @return Mono<ClassGoal?> 완료/비완료처리된 학습 목표
     */
    fun updateClassGoal(classRoomId: String, classGoal: ClassGoal): Mono<ClassGoal?> {
        return classGoalRepository.findById(classGoal.id!!)
            .map { it?.setDone(classGoal.done)!! }
            .flatMap { classGoalRepository.save(it) }
            .doOnSuccess { saved ->
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.CLASS_GOAL,
                        data = saved,
                        userType = UserType.STUDENT
                    )
                )
            }
    }


    /**
     * 학습 목표 삭제처리 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param classGoal : 학습 목표
     * @return Mono<ClassGoal?> 삭제된 학습 목표
     */
    fun deleteClassGoal(classRoomId: String, classGoal: ClassGoal): Mono<ClassGoal?> {
        return classGoalRepository.deleteById(classGoal.id!!).thenReturn(classGoal)
            .doOnSuccess {
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.CLASS_GOAL,
                        data = it,
                        userType = UserType.STUDENT
                    )
                )
            }
    }

    /**
     * 가리기 조회
     * @param classRoomId : 클래스룸 아이디
     * @param tabIndex : 탭 인덱스
     * @param pageIndex : 페이지 인덱스
     * @return Mono<Reveal?> 가리기 정보
     */
    fun getReveal(classRoomId: String, tabIndex: Int, pageIndex: Int): Mono<Reveal?> {
        return revealRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, tabIndex, pageIndex)
    }

    /**
     * 가리기 저장 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param reveal : 가리기 정보
     * @return Mono<Reveal?> 저장된 가리기 정보
     */
    fun sendReveal(classRoomId: String, reveal: Reveal): Mono<Reveal?> {
        return revealRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, reveal.tabIndex, reveal.pageIndex)
            .defaultIfEmpty(reveal.apply { this.classRoomId = classRoomId })
            .flatMap {
                revealRepository.save(
                    it!!.apply {
                        data = reveal.tmpData
                        tmpData = reveal.tmpData
                    })
            }
            .doOnSuccess {
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.REVEAL,
                        data = it,
                        userType = UserType.STUDENT
                    )
                )
            }
    }

    /**
     * 가리기 임시 저장
     * @param classRoomId : 클래스룸 아이디
     * @param reveal : 가리기 정보
     * @return Mono<Reveal?> 저장된 가리기 정보
     */
    fun saveRevealTmpData(classRoomId: String, reveal: Reveal): Mono<Reveal?> {
        return revealRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, reveal.tabIndex, reveal.pageIndex)
            .defaultIfEmpty(reveal.apply { this.classRoomId = classRoomId })
            .flatMap { revealRepository.save(it!!.apply { tmpData = reveal.tmpData!! }) }
    }

    /**
     * 가리기 삭제 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param reveal : 가리기 정보
     * @return Mono<Reveal?> 삭제된 가리기 정보
     */
    fun deleteReveal(classRoomId: String, reveal: Reveal): Mono<Reveal?> {
        return revealRepository.findByClassRoomIdAndTabIndexAndPageIndex(classRoomId, reveal.tabIndex, reveal.pageIndex)
            .flatMap { revealRepository.deleteById(it!!.id!!).thenReturn(it) }
            .doOnSuccess {
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.REVEAL,
                        data = it.apply { data = emptyList() },
                        userType = UserType.STUDENT
                    )
                )
            }
    }

    /**
     * 이해도 설문 전달
     * @param classRoomId : 클래스룸 아이디
     * @param comprehensionQuestion : 이해도 설문
     * @return Mono<ComprehensionQuestion?> 전달된 이해도 설문
     */
    fun sendComprehensionQuestion(
        classRoomId: String,
        comprehensionQuestion: ComprehensionQuestion
    ): Mono<ComprehensionQuestion?> {
        envelopSendService.sendMessageQueue(
            Envelop(
                classRoomId = classRoomId,
                msgType = MessageType.COMPREHENSION_QUESTION,
                data = comprehensionQuestion,
                userType = UserType.STUDENT
            )
        )
        return Mono.just(comprehensionQuestion)
    }

    /**
     * 이해도 설문 답안 제출
     * @param classRoomId : 클래스룸 아이디
     * @param answer : 이해도 설문 답안
     * @return Mono<ComprehensionAnswer?> 제출된 이해도 설문 답안
     */
    fun submitComprehensionAnswer(classRoomId: String, answer: ComprehensionAnswer): Mono<ComprehensionAnswer?> {
        return comprehensionAnswerRepository.findByIdAndUserSeq(answer.id, answer.userSeq)
            .handle { find, sink ->
                if (find != null) {
                    sink.error(BadRequestException("이미 설문에 응답하였습니다."))
                } else {
                    sink.next(answer.apply { this.classRoomId = classRoomId })
                }
            }
            .flatMap { comprehensionAnswerRepository.save(it) }
            .doOnSuccess {
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.COMPREHENSION_ANSWER,
                        data = it,
                        userType = UserType.TEACHER
                    )
                )
            }
    }

    /**
     * 이해도 설문 답안 조회
     * @param classRoomId : 클래스룸 아이디
     * @return Mono<MutableList<ComprehensionAnswer?>> 이해도 설문 답안 리스트
     */
    fun getComprehensionAnswerList(classRoomId: String): Mono<MutableList<ComprehensionAnswer?>> {
        return comprehensionAnswerRepository.findAllByClassRoomId(classRoomId).collectList()
    }

    /**
     * 클래스 룸 내 유저의 배지리스트 조회
     * @param classRoomId : 클래스룸 아이디
     * @return Mono<MutableList<Badge?>> 배지 리스트
     */
    fun getBadgeList(classRoomId: String): Mono<MutableList<Badge?>> {
        return badgeRepository.findAllByClassRoomId(classRoomId).collectList()
    }

    /**
     * 배지 발급
     * @param classRoomId : 클래스룸 아이디
     * @param badge : 배지 정보
     */
    fun sendBadge(classRoomId: String, badge: Badge): Mono<Badge?> {
        return badgeRepository.findByClassRoomIdAndUserSeqAndBadgeType(classRoomId, badge.userSeq, badge.badgeType)
            .handle { find, sink ->
                if (find != null) {
                    sink.error(BadRequestException("이미 배지를 획득하였습니다."))
                } else {
                    sink.next(badge.apply { this.classRoomId = classRoomId })
                }
            }
            .switchIfEmpty { badgeRepository.save(badge.apply { this.classRoomId = classRoomId }) }
            .doOnSuccess {
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.BADGE,
                        data = it,
                        userType = UserType.STUDENT
                    )
                )
            }
    }

    /**
     * studyData 및 공유되는 페이지 인덱스 저장 & 전달
     * @param classRoomId : 클래스룸 아이디
     * @param studyData : studyData 정보
     * @return Mono<ClassStudyDataMap?> 저장된 studyData 정보
     */
    fun sendStudyData(classRoomId: String, studyData: StudyData): Mono<ClassStudyDataMap> {
        return studyDataMapRepository.findById(classRoomId)
            .defaultIfEmpty(ClassStudyDataMap(classRoomId = classRoomId))
            .flatMap { studyDataMapRepository.save(it!!.apply { this.studyDataMap[studyData.tabIndex] = studyData }) }
            .doOnSuccess { saveSharedIndex(classRoomId, studyData) }
            .doOnSuccess {
                envelopSendService.sendMessageQueue(
                    Envelop(
                        classRoomId = classRoomId,
                        msgType = MessageType.STUDY_DATA,
                        data = it,
                        userType = UserType.STUDENT
                    )
                )
            }
    }

    /**
     * 공유되는 페이지 인덱스 저장
     */
    private fun saveSharedIndex(classRoomId: String, studyData: StudyData) {
        classRoomRepository.findById(classRoomId).map {
            it!!.apply {
                sharePageIndex = studyData.pageIndex
                shareTabIndex = studyData.tabIndex
            }
        }.flatMap { classRoomRepository.save(it) }
    }

    /**
     * activity 로그 저장
     * @param classRoomId : 클래스룸 아이디
     * @param miniWindowType : 미니윈도우 타입
     * @param entity : activity 정보
     * @return Mono<Activity?> 저장된 activity 정보
     */
    fun saveActivityLog(classRoomId: String, miniWindowType: MiniWindowType, entity: Any?): Mono<Activity?> {
        return activityRepository.save(
            Activity(
                classRoomId = classRoomId,
                miniWindowType = miniWindowType,
                entity = entity
            )
        )
    }

    /**
     * activity 로그 조회
     * @param classRoomId : 클래스룸 아이디
     * @param page : 페이지 번호
     * @param size : 페이지 사이즈
     * @return Mono<PageImpl<Activity?>> activity 로그 리스트
     */
    fun getActivityLogs(classRoomId: String, page: Int, size: Int): Mono<PageImpl<Activity?>> {
        val pageable: Pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "endTime")
        return activityRepository.countByClassRoomId(classRoomId)
            .zipWith(
                activityRepository.findAllByClassRoomId(classRoomId, pageable)
                    .collectList()
            )
            .map { PageImpl(it.t2, Pageable.unpaged(), it.t1) }
    }

}