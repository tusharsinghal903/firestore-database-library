package org.tusharsinghal.googleCloudStorage.domain

import java.io.InputStream
import java.net.URL

interface StorageService {
    fun generateSignedUrl(fileName: String, expirationMinutes: Long, isPublic: Boolean): URL

    fun uploadFileUsingInputStream(fileName: String, inputStream: InputStream, isPublic: Boolean = false): String
    fun uploadFileFromLocal(fileName: String, localFilePath: String, isPublic: Boolean = false): String

    fun downloadFile(fileName: String): ByteArray
    fun downloadFileToLocal(fileName: String, localFilePath: String)

    fun getPublicUrl(fileName: String): String
}