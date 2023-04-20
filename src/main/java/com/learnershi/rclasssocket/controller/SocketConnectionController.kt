package com.learnershi.rclasssocket.controller

import com.learnershi.rclasssocket.entity.CanvasDraw
import com.learnershi.rclasssocket.entity.ClassRoom
import com.learnershi.rclasssocket.entity.ClassStudyDataMap
import com.learnershi.rclasssocket.entity.User
import com.learnershi.rclasssocket.entity.common.Envelop
import com.learnershi.rclasssocket.log.Log
import com.learnershi.rclasssocket.repository.ClassUserSessionsRepository
import com.learnershi.rclasssocket.service.ClassRoomService
import com.learnershi.rclasssocket.service.SocketService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono


@Controller
class SocketConnectionController(
    private val classUserSessionsRepository: ClassUserSessionsRepository,
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
        return classRoomService.getClassRoom(classRoomId).doOnSuccess {
            socketService.connect(classRoomId, user, requester).doOnSuccess {
                envelopSendService.sendMessageQueue(it)
            }.subscribe()
        }.thenReturn(user)
    }
    @MessageMapping("{classRoomId}/classRoom")
    fun getClassRoomInfo(
        @DestinationVariable classRoomId: String,
    ): Mono<ClassRoom?> {
        log.info("getClassRoomInfo: {}", classRoomId)
        return classRoomService.getClassRoom(classRoomId)
    }

    @MessageMapping("{classRoomId}/users")
    fun getClassRoomSessionUsers(
        @DestinationVariable classRoomId: String,
    ): Mono<Collection<User>?> {
        log.info("getClassRoomSessionUsers: {}", classRoomId)
        return classRoomService.getClassRoomSessionUsers(classRoomId)
    }

    @MessageMapping("{classRoomId}/studyDataMap")
    fun getStudyDataMap(
        @DestinationVariable classRoomId: String,
    ): Mono<ClassStudyDataMap?> {
        log.info("getStudyDataMap: {}", classRoomId)
        return classRoomService.getStudyDataMap(classRoomId)
    }
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

    /**
     * 학습 관련 메세지를 공유한다.
     * (server -> kafka -> server -> client)
     */
    @MessageMapping("share/studyAction")
    fun shareStudyAction(envelop: Envelop) {
        log.info("Share action: {}", envelop)
        envelopSendService.sendMessageQueue(envelop)
    }
}