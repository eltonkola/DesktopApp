package com.eltonkola.desktop.chat


data class Message(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: String,
    val isThinking: Boolean = false
)

data class Chat(
    val id: String,
    val title: String,
    val lastMessage: String,
    val timestamp: String
)
