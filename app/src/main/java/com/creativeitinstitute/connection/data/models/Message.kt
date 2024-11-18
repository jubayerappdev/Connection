package com.creativeitinstitute.connection.data.models


interface Message{
    val senderID: String
    val receiver: String
    var msgID: String
}


data class TextMessages(
    var text:String? = null,
    override val senderID: String = "",
    override val receiver: String = "",
    override var msgID: String = ""
) : Message

data class ImageMessage(
    val imageLink: String = "",
    override val senderID: String = "",
    override val receiver: String = "",
    override var msgID: String = "",

    ):Message
