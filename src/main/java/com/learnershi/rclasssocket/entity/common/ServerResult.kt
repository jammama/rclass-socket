package com.learnershi.rclasssocket.entity.common

import org.reactivestreams.Publisher
import org.slf4j.MarkerFactory
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors
import javax.lang.model.type.TypeVariable

/**
 * API JSON 결과 포맷 Entity
 *
 * @author dualcat
 */
class ServerResult : LinkedHashMap<String?, Any>() {
    var RESULT_MARKER = MarkerFactory.getMarker("[RESULT]")
    private var count: Long? = null
    private var data: Any? = null
    private var message: String? = null
    private var publisher: Publisher<*>? = null
    private var publisherClass: Class<TypeVariable>? = null

    /**
     * 최종 Response 객체 생성
     *
     * @return Mono<ServerResponse>
    </ServerResponse> */
    fun build(): Mono<ServerResponse> {
        val bodyBuilder = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)

        // Message 값이 존재할 경우 기본값 덮어쓰기
        if (message != null) {
            this[KEY_STATUS_MESSAGE] = message!!
        }

        // Data 설정. null 일 경우 빈값('{}')
        if (data != null) {
            this[KEY_DATA] = data!!
        } else {
            this[KEY_DATA] = HashMap<Any?, Any?>()
        }
        this[KEY_TIMESTAMP] =
            currentTime
        // Sort
        val result: MutableMap<String?, Any> = this.entries.stream()
            .filter { e: Map.Entry<String?, Any> -> e.value != null }
            .collect(
                Collectors.toMap(
                    Function<Map.Entry<String?, Any>, String?> { e: Map.Entry<String?, Any> -> e.key },
                    Function<Map.Entry<String?, Any>, Any> { e: Map.Entry<String?, Any> -> e.value },
                    BinaryOperator { u: Any, v: Any -> u },
                    Supplier { LinkedHashMap<String?, Any>() }
                )
            )

        return bodyBuilder.body(BodyInserters.fromValue(result))

    }

    fun count(count: Long?): ServerResult {
        this.count = count
        return this
    }

    fun data(data: Any?): ServerResult {
        this.data = data
        return this
    }

    fun producer(publisher: Publisher<*>?, clazz: Class<TypeVariable>?): ServerResult {
        this.publisher = publisher
        publisherClass = clazz
        return this
    }

    fun message(message: String?): ServerResult {
        this.message = message
        return this
    }

    override fun put(key: String?, value: Any): Any {
        super.put(key, value)
        return value
    }

    companion object {
        private const val RESULT_SUCCESS = 1000
        private const val RESULT_FAIL = 3000
        private const val KEY_STATUS_CODE = "status_code"
        private const val KEY_STATUS_MESSAGE = "status_message"
        private const val KEY_DATA = "data"
        private const val KEY_TIMESTAMP = "timestamp"
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        fun success(): ServerResult {
            return buildResult(RESULT_SUCCESS)
        }

        fun fail(): ServerResult {
            return buildResult(RESULT_FAIL)
        }

        /**
         * 기본 Result 객체 생성
         *
         * @param result 결과코드
         * @return Result
         */
        private fun buildResult(result: Int): ServerResult {
            val returnServerResult = ServerResult()
            returnServerResult[KEY_STATUS_CODE] = result
            if (RESULT_SUCCESS == result) {
                returnServerResult[KEY_STATUS_MESSAGE] = "Success"
            } else if (RESULT_FAIL == result) {
                returnServerResult[KEY_STATUS_MESSAGE] = "Fail"
            }
            return returnServerResult
        }

        private val currentTime: String
            private get() {
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                return simpleDateFormat.format(Calendar.getInstance().time)
            }
    }
}