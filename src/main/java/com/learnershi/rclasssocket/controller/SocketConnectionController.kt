package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.entity.*
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.entity.enums.MiniWindowType
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.service.ClassRoomService
import com.learnershi.rclasssocket.service.SocketService
import org.springframework.data.domain.PageImpl
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono


@Controller
class SocketConnectionController(
    private val classRoomService: ClassRoomService,
    private val socketService: SocketService,
    private val envelopSendService: EnvelopSendService
) {

    val log = Log.of(this.javaClass)

    /**
     *  Log for connection
     *
     *  ## Client Example (js)
     *  ``` javascript
     *  async function createClient() {
     *         const client = new RSocketClient({
     *             serializers: {
     *                 data: JsonSerializer,
     *                 metadata: IdentitySerializer
     *             },
     *             setup: {
     *                 dataMimeType: 'application/json',
     *                 metadataMimeType: 'message/x.rsocket.routing.v0',
     *             },
     *             responder: new MyResponder((payload) =>{
     *                 console.log(payload);
     *             }),
     *             transport: new RSocketWebsocketClient ({
     *                 url: "ws://localhost:8080/rsocket"
     *             })
     *         })
     *             .connect().subscribe({
     *             onComplete: socket => {
     *             // for additional metadata
     *                socket.metadataPush({
     *                     metadata: String.fromCharCode("route".length) + "route"
     *                 }).subscribe()
     *             },
     *             onclose: () => {
     *                 ...
     *             }
     *         });
     *     }
     *     ```
     */
    @ConnectMapping
    fun connect(requester: RSocketRequester) {
        log.info("connected")
        requester.rsocket()!!
            .onClose()
            .doFirst {
                log.info("welcome, stranger!")
            }
            .doFinally {
                log.info("goodbye, stranger!")
            }
            .subscribe()
    }

    /**
     * user connection
     * connect 된 유저 정보 전파 & 리턴
     */
    @MessageMapping(*["connect/{classRoomId}"])
    fun userConnect(
        requester: RSocketRequester,
        @DestinationVariable classRoomId: String,
        @Payload user: User
    ): Mono<User?> {
        return classRoomService.getClassRoom(classRoomId).then(
            socketService.connect(classRoomId, user, requester).doOnSuccess {
                envelopSendService.sendMessageQueue(it)
            }
        ).thenReturn(user)
    }

    /**
     * 학습 관련 메세지를 공유한다.
     * (server -> kafka -> server -> client)
     */
    @MessageMapping("share/studyAction")
    fun shareStudyAction(envelop: Envelop) {
        log.info("Share action: {}", envelop)
        envelopSendService.sendMessageQueue(envelop)
    }

    /**
     * 클래스룸 정보 조회
     * @param classRoomId 클래스룸 아이디
     * @return 클래스룸 정보
     */
    @MessageMapping("{classRoomId}/classRoom")
    fun getClassRoomInfo(
        @DestinationVariable classRoomId: String,
    ): Mono<ClassRoom?> {
        log.info("getClassRoomInfo: {}", classRoomId)
        return classRoomService.getClassRoom(classRoomId)
    }

    /**
     * 클래스룸 접속자 정보 조회
     * @param classRoomId 클래스룸 아이디
     * @return 클래스룸 접속자 정보
     */
    @MessageMapping("{classRoomId}/users")
    fun getClassRoomSessionUsers(
        @DestinationVariable classRoomId: String,
    ): Mono<Collection<User>?> {
        log.info("getClassRoomSessionUsers: {}", classRoomId)
        return classRoomService.getClassRoomSessionUsers(classRoomId)
    }

    /**
     * 클래스룸 학습 데이터 조회
     * @param classRoomId 클래스룸 아이디
     * @return 클래스룸 학습 데이터
     */
    @MessageMapping("{classRoomId}/studyDataMap")
    fun getStudyDataMap(
        @DestinationVariable classRoomId: String,
    ): Mono<ClassStudyDataMap?> {
        log.info("getStudyDataMap: {}", classRoomId)
        return classRoomService.getStudyDataMap(classRoomId)
    }


    /**
     * 수업 상태 변경
     * @param classRoomId 클래스룸 아이디
     * @param classRoom 클래스룸 정보
     * @return 업데이트 결과
     */
    @MessageMapping("{classRoomId}/classRoomStatus")
    fun changeClassRoomStatus(
        @DestinationVariable classRoomId: String,
        @Payload classRoom: ClassRoom
    ): Mono<ClassRoom> {
        log.info("changeClassRoomStatus: {}", classRoomId)
        return classRoomService.updateClassRoom(classRoomId, classRoom)
    }

    /**
     * 클래스룸 로그 조회
     * @param classRoomId 클래스룸 아이디
     * @return 클래스룸 로그
     */
    @MessageMapping("{classRoomId}/log")
    fun getClassRoomLog(
        @DestinationVariable classRoomId: String,
    ): Mono<MutableList<ClassRoomLog?>> {
        log.info("getClassRoomLog: {}", classRoomId)
        return classRoomService.getClassLogList(classRoomId)
    }

    /**
     * 클래스룸 메모 조회
     * @param classRoomId 클래스룸 아이디
     * @return 클래스룸 메모 리스트
     */
    @MessageMapping("{classRoomId}/memo/{tabIndex}/{pageIndex}")
    fun getClassRoomMemo(
        @DestinationVariable classRoomId: String,
        @DestinationVariable tabIndex: Int,
        @DestinationVariable pageIndex: Int,
    ): Mono<PostIt?> {
        log.info("getClassRoomMemo: {}", classRoomId)
        return classRoomService.getMemo(classRoomId, tabIndex, pageIndex)
    }

    /**
     * 클래스룸 메모 전달 & 저장
     * @param classRoomId 클래스룸 아이디
     * @param postIt 메모 정보
     */
    @MessageMapping("{classRoomId}/memo")
    fun sendClassRoomMemo(
        @DestinationVariable classRoomId: String,
        @Payload postIt: PostIt
    ): Mono<PostIt?> {
        log.info("sendClassRoomMemo: {}", classRoomId)
        return classRoomService.sendMemo(classRoomId, postIt)
    }

    /**
     * 클래스룸 메모 삭제
     * @param classRoomId 클래스룸 아이디
     * @param postIt 메모 정보
     *
     */
    @MessageMapping("{classRoomId}/memo/delete")
    fun deleteClassRoomMemo(
        @DestinationVariable classRoomId: String,
        @Payload postIt: PostIt
    ): Mono<PostIt?> {
        log.info("deleteClassRoomMemo: {}", classRoomId)
        return classRoomService.deleteMemo(classRoomId, postIt)
    }

    /**
     * 클래스룸 목표 조회
     * @param classRoomId 클래스룸 아이디
     * @return 클래스룸 목표 리스트
     */
    @MessageMapping("{classRoomId}/classGoal")
    fun getClassGoalList(
        @DestinationVariable classRoomId: String,
    ): Mono<MutableList<ClassGoal?>> {
        log.info("getClassGoalList: {}", classRoomId)
        return classRoomService.getClassGoalList(classRoomId)
    }

    /**
     * 클래스룸 목표 생성 & 전달
     * @param classRoomId 클래스룸 아이디
     * @param classGoal 클래스룸 목표 정보
     * @return 생성된 목표 정보
     */
    @MessageMapping("{classRoomId}/classGoal/create")
    fun createClassGoal(
        @DestinationVariable classRoomId: String,
        @Payload classGoal: ClassGoal
    ): Mono<ClassGoal?> {
        log.info("createClassGoal: {}", classRoomId)
        return classRoomService.createClassGoal(classRoomId, classGoal)
    }

    /**
     * 클래스룸 목표 완료/미완료 처리 & 전달
     * @param classRoomId 클래스룸 아이디
     * @param classGoal 클래스룸 목표 정보
     * @return 업데이트된 목표 정보
     */
    @MessageMapping("{classRoomId}/classGoal/update")
    fun updateClassGoal(
        @DestinationVariable classRoomId: String,
        @Payload classGoal: ClassGoal
    ): Mono<ClassGoal?> {
        log.info("updateClassGoal: {}", classRoomId)
        return classRoomService.updateClassGoal(classRoomId, classGoal)
    }

    /**
     * 클래스룸 목표 삭제 & 전달
     * @param classRoomId 클래스룸 아이디
     * @param classGoal 클래스룸 목표 정보
     * @return 삭제된 목표 정보
     */
    @MessageMapping("{classRoomId}/classGoal/delete")
    fun deleteClassGoal(
        @DestinationVariable classRoomId: String,
        @Payload classGoal: ClassGoal
    ): Mono<ClassGoal?> {
        log.info("deleteClassGoal: {}", classRoomId)
        return classRoomService.deleteClassGoal(classRoomId, classGoal)
    }


    /**
     * 가리기 조회
     * @param classRoomId 클래스룸 아이디
     * @param tabIndex 탭 인덱스
     * @param pageIndex 페이지 인덱스
     * @return 가리기 정보
     */
    @MessageMapping("{classRoomId}/reveal/{tabIndex}/{pageIndex}")
    fun getReveal(
        @DestinationVariable classRoomId: String,
        @DestinationVariable tabIndex: Int,
        @DestinationVariable pageIndex: Int,
    ): Mono<Reveal?> {
        log.info("getReveal: {}", classRoomId)
        return classRoomService.getReveal(classRoomId, tabIndex, pageIndex)
    }

    /**
     * 가리기 전달 & 저장
     * @param classRoomId 클래스룸 아이디
     * @param reveal 가리기 정보
     */
    @MessageMapping("{classRoomId}/reveal")
    fun sendReveal(
        @DestinationVariable classRoomId: String,
        @Payload reveal: Reveal
    ): Mono<Reveal?> {
        log.info("sendReveal: {}", classRoomId)
        return classRoomService.sendReveal(classRoomId, reveal)
    }

    /**
     * 가리기 임시 데이터 저장 & 수정
     * @param classRoomId 클래스룸 아이디
     * @param reveal 가리기 정보
     * @return 수정된 가리기 정보
     */
    @MessageMapping("{classRoomId}/reveal/save")
    fun saveRevealTmpData(
        @DestinationVariable classRoomId: String,
        @Payload reveal: Reveal
    ): Mono<Reveal?> {
        log.info("saveReveal: {}", classRoomId)
        return classRoomService.saveRevealTmpData(classRoomId, reveal)
    }

    /**
     * 가리기 데이터 삭제
     * @param classRoomId 클래스룸 아이디
     * @param reveal 가리기 정보
     * @return 삭제된 가리기 정보
     */
    @MessageMapping("{classRoomId}/reveal/delete")
    fun deleteReveal(
        @DestinationVariable classRoomId: String,
        @Payload reveal: Reveal
    ): Mono<Reveal?> {
        log.info("deleteReveal: {}", classRoomId)
        return classRoomService.deleteReveal(classRoomId, reveal)
    }

    /**
     * 이해도 설문 전달
     * @param classRoomId 클래스룸 아이디
     * @param comprehensionQuestion 이해도 설문 정보
     * @return 전달한 이해도 설문 정보
     */
    @MessageMapping("{classRoomId}/comprehension/question")
    fun sendComprehensionQuestion(
        @DestinationVariable classRoomId: String,
        @Payload comprehensionQuestion: ComprehensionQuestion
    ): Mono<ComprehensionQuestion?> {
        log.info("sendComprehensionQuestion: {}", classRoomId)
        return classRoomService.sendComprehensionQuestion(classRoomId, comprehensionQuestion)
    }

    /**
     * 이해도 설문 답안 제출
     * @param classRoomId 클래스룸 아이디
     * @param comprehensionAnswer 이해도 설문 답안 정보
     * @return 전달한 이해도 설문 답안 정보
     */
    @MessageMapping("{classRoomId}/comprehension/answer")
    fun submitComprehensionAnswer(
        @DestinationVariable classRoomId: String,
        @Payload comprehensionAnswer: ComprehensionAnswer
    ): Mono<ComprehensionAnswer?> {
        log.info("sendComprehensionAnswer: {}", classRoomId)
        return classRoomService.submitComprehensionAnswer(classRoomId, comprehensionAnswer)
    }

    /**
     * 이해도 설문 답안 조회
     * @param classRoomId 클래스룸 아이디
     * @return 이해도 설문 답안 정보
     */
    @MessageMapping("{classRoomId}/comprehension/answerList")
    fun getComprehensionAnswerList(
        @DestinationVariable classRoomId: String
    ): Mono<MutableList<ComprehensionAnswer?>> {
        log.info("getComprehensionAnswerList: {}", classRoomId)
        return classRoomService.getComprehensionAnswerList(classRoomId)
    }

    /**
     * 배지 리스트 조회
     * @param classRoomId 클래스룸 아이디
     * @return 배지 리스트
     */
    @MessageMapping("{classRoomId}/badgeList")
    fun getBadgeList(
        @DestinationVariable classRoomId: String
    ): Mono<MutableList<Badge?>> {
        log.info("getBadgeList: {}", classRoomId)
        return classRoomService.getBadgeList(classRoomId)
    }

    /**
     * 배지 전달
     * @param classRoomId 클래스룸 아이디
     * @param badge 배지 정보
     */
    @MessageMapping("{classRoomId}/badge")
    fun sendBadge(
        @DestinationVariable classRoomId: String,
        @Payload badge: Badge
    ): Mono<Badge?> {
        log.info("sendBadge: {}", classRoomId)
        return classRoomService.sendBadge(classRoomId, badge)
    }

    /**
     * studyData 전달 & 저장
     * @param classRoomId 클래스룸 아이디
     * @param studyData studyData 정보
     * @return 수정된 studyData 정보
     */
    @MessageMapping("{classRoomId}/studyData")
    fun sendStudyData(
        @DestinationVariable classRoomId: String,
        @Payload studyData: StudyData
    ): Mono<ClassStudyDataMap> {
        log.info("sendStudyData: {}", classRoomId)
        return classRoomService.sendStudyData(classRoomId, studyData)
    }


    /**
     * activityLog 저장
     * @param classRoomId 클래스룸 아이디
     * @param miniWindowType 미니윈도우 타입
     * @param entity 저장할 activity의 데이터
     * @return 저장된 activity 정보
     */
    @MessageMapping("{classRoomId}/activity/{miniWindowType}/save")
    fun saveActivityLog(
        @DestinationVariable classRoomId: String,
        @DestinationVariable miniWindowType: String,
        @Payload entity: Any?
    ): Mono<Activity?> {
        log.info("saveActivityLog: {}", classRoomId)
        return classRoomService.saveActivityLog(classRoomId, MiniWindowType.valueOf(miniWindowType), entity)
    }

    /**
     * activityLog 조회
     * @param classRoomId 클래스룸 아이디
     * @return activity 정보
     */
    @MessageMapping("{classRoomId}/activity/{page}/{size}")
    fun getActivityLog(
        @DestinationVariable classRoomId: String,
        @DestinationVariable page: Int,
        @DestinationVariable size: Int
    ): Mono<PageImpl<Activity?>> {
        log.info("getActivityLog: {}", classRoomId)
        return classRoomService.getActivityLogs(classRoomId, page, size)
    }


    /**
     * 클래스룸 드로잉 정보 조회
     * @param classRoomId 클래스룸 아이디
     * @return 클래스룸 드로잉 정보
     */
    @MessageMapping("{classRoomId}/drawPathList")
    fun getDrawPathList(
        @DestinationVariable classRoomId: String,
    ): Mono<MutableList<CanvasDraw?>> {
        log.info("getDrawPathList: {}", classRoomId)
        return classRoomService.getCanvasDrawPathList(classRoomId)
    }

    /**
     * 테스트용: envelop 구조의 메세지를 전송하고 받는다.
     *
     * @param envelop 메세지
     */
    @MessageMapping("/test/{classRoomId}")
    fun sendMessage(envelop: Envelop) {
        envelopSendService.sendMessageQueue(envelop)
    }


}