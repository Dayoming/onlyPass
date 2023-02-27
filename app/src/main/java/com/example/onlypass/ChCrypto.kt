package com.example.onlypass

import java.nio.charset.Charset
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object ChCrypto {
    const val SECRET_KEY = "01234567890123450123456789012345"
    @JvmStatic fun aesEncrypt(v:String, secretKey:String) = AES256.encrypt(v, secretKey)
    @JvmStatic fun aesDecrypt(v:String, secretKey: String) = AES256.decrypt(v, secretKey)
}

private object AES256 {
    private val encorder = Base64.getEncoder()
    private val decorder = Base64.getDecoder()
    private fun cipher(opmode:Int, secretKey: String): Cipher {
        if(secretKey.length != 32) throw java.lang.RuntimeException("Secretkey length is not 32 chars")
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val sk = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        val iv = IvParameterSpec(secretKey.substring(0, 16).toByteArray(Charsets.UTF_8))
        c.init(opmode, sk, iv)
        return c
    }

    fun encrypt(str:String, secretKey: String):String {
        val encrypted = cipher(Cipher.ENCRYPT_MODE, secretKey).doFinal(str.toByteArray(Charsets.UTF_8))
        return String(encorder.encode(encrypted))
    }

    fun decrypt(str:String, secretKey: String):String {
        val byteStr = decorder.decode(str.toByteArray(Charsets.UTF_8))
        return String(cipher(Cipher.DECRYPT_MODE, secretKey).doFinal(byteStr))
    }

    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}