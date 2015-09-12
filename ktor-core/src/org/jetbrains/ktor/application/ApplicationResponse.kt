package org.jetbrains.ktor.application

import org.jetbrains.ktor.http.*
import java.io.*
import java.nio.charset.*

public interface ApplicationResponse {
    public val headers: ResponseHeaders
    public val cookies: ResponseCookies

    public fun status(): HttpStatusCode?
    public fun status(value: HttpStatusCode)
    public fun interceptStatus(handler: (value: HttpStatusCode, next: (value: HttpStatusCode) -> Unit) -> Unit)

    public fun stream(body: OutputStream.() -> Unit): Unit
    public fun interceptStream(handler: (body: OutputStream.() -> Unit, next: (body: OutputStream.() -> Unit) -> Unit) -> Unit)

    public fun send(message: Any): ApplicationRequestStatus
    public fun interceptSend(handler: (message: Any, next: (message: Any) -> ApplicationRequestStatus) -> ApplicationRequestStatus)
}

public fun ApplicationResponse.streamBytes(bytes: ByteArray) {
    stream { write(bytes) }
}

public fun ApplicationResponse.streamText(text: String, encoding: String = "UTF-8") {
    streamBytes(text.toByteArray(Charset.forName(encoding)))
}

public fun ApplicationResponse.sendBytes(bytes: ByteArray): ApplicationRequestStatus {
    status(HttpStatusCode.OK)
    streamBytes(bytes)
    return ApplicationRequestStatus.Handled
}

public fun ApplicationResponse.sendText(contentType: ContentType, text: String): ApplicationRequestStatus {
    contentType(contentType)
    val encoding = contentType.parameter("charset") ?: "UTF-8"
    return sendBytes(text.toByteArray(Charset.forName(encoding)))
}

public fun ApplicationResponse.sendText(text: String): ApplicationRequestStatus {
    return sendText(ContentType.Text.Plain.withParameter("charset", "UTF-8"), text)
}

public fun ApplicationResponse.write(body: Writer.() -> Unit) {
    stream {
        writer().use { writer -> writer.body() }
    }
}