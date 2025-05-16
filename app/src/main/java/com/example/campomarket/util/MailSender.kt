package com.example.campomarket.util

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailSender(
    private val username: String,
    private val password: String,
) {

    fun sendMail(to: String, subject: String, message: String) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress(username))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                setSubject(subject)

                setContent(message, "text/html; charset=utf-8")
            }

            // Enviar en un hilo separado
            Thread {
                Transport.send(mimeMessage)
            }.start()

        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}

