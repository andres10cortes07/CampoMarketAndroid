package com.example.campomarket.util

import com.example.campomarket.data.model.Usuario

object PasswordRecoveryUtil {

    fun generarClaveAleatoria(longitud: Int = 10): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..longitud)
            .map { caracteres.random() }
            .joinToString("")
    }

    fun enviarCorreoRecuperacion(usuario: Usuario, claveTemporal: String, asunto : String): Boolean {
        val asuntoCorreo : String

        if (asunto == "Asignacion de contraseña") asuntoCorreo = "Asignación de contraseña - CampoMarket"
        else if (asunto == "Recuperación de Contraseña") asuntoCorreo = "Recuperación de contraseña - CampoMarket"
        else asuntoCorreo = "Nueva contraseña - CampoMarket"

        val mensaje = """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"><title>${asunto}</title></head>
            <body style="margin: 0; font-family: Arial, sans-serif; background-color: #FFFFFF;">
                <div style="background-color: #4CAF50; padding: 20px; text-align: center; color: white;">
                    <h1 style="margin: 0; font-size: 26px;">${asunto}</h1>
                </div>
                <div style="padding: 30px;">
                    <h2 style="color: #FF7E05;">Hola,</h2>
                    <p style="font-size: 16px; color: #212121;">Aquí tienes tu nueva clave temporal:</p>
                    <div style="background-color: #F5F5F5; border-left: 6px solid #5C9EAD; padding: 15px; margin: 20px 0;">
                        <p style="font-size: 18px; color: #000;"><strong>👉 $claveTemporal 👈</strong></p>
                    </div>
                    <p style="font-size: 16px; color: #212121;">Por favor, inicia sesión con esta clave y cámbiala lo antes posible desde tu perfil.</p>
                    <p style="font-size: 14px; color: #5C9EAD;">Si no solicitaste este cambio, ignora este mensaje o contáctanos.</p>
                </div>
                <div style="background-color: #4CAF50; text-align: center; padding: 15px; color: white; font-size: 14px;">
                    © 2025 CampoMarket – Todos los derechos reservados.
                </div>
            </body>
            </html>
        """.trimIndent()

        return try {
            val sender = MailSender("campomarketapp@gmail.com", "adzs lyof pvig bcpd")
            sender.sendMail(usuario.correo, asuntoCorreo, mensaje)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
