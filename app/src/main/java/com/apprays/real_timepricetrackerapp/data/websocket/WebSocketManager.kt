package com.apprays.real_timepricetrackerapp.data.websocket

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    // This flow emits every message received from the server
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 100)
    val messages: SharedFlow<String> = _messages

    private val _isConnected = MutableSharedFlow<Boolean>(extraBufferCapacity = 10)
    val isConnected: SharedFlow<Boolean> = _isConnected

    fun connect() {
        val request = Request.Builder()
            .url("wss://ws.postman-echo.com/raw")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                _isConnected.tryEmit(true)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                _messages.tryEmit(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _isConnected.tryEmit(false)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _isConnected.tryEmit(false)
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "User stopped feed")
        webSocket = null
        _isConnected.tryEmit(false)
    }
}
