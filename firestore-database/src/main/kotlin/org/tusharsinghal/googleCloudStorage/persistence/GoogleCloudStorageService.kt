package org.tusharsinghal.googleCloudStorage.persistence

import com.google.cloud.storage.*
import org.tusharsinghal.googleCloudStorage.domain.StorageService
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.util.concurrent.TimeUnit

class GoogleCloudStorageService(
    private val bucketName: String,
    private val storage: Storage
): StorageService {

    override fun generateSignedUrl(fileName: String, expirationMinutes: Long, isPublic: Boolean): URL {
        val blobInfo = BlobInfo.newBuilder(bucketName, fileName).build()
        val signedUrl = storage.signUrl(
            blobInfo,
            expirationMinutes,
            TimeUnit.MINUTES,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withV4Signature()
        )

        if (isPublic) {
            val blob = storage.get(blobInfo.blobId)
            blob.toBuilder().setAcl(listOf(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))).build().update()
        }

        return signedUrl
    }

    override fun uploadFileUsingInputStream(fileName: String, inputStream: InputStream, isPublic: Boolean): String {
        val blobId = BlobId.of(bucketName, fileName)
        val blobInfo = BlobInfo.newBuilder(blobId).build()
        val blob = storage.createFrom(blobInfo, inputStream)

        if (isPublic) {
            blob.toBuilder().setAcl(listOf(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))).build().update()
        }

        return fileName
    }

    override fun uploadFileFromLocal(fileName: String, localFilePath: String, isPublic: Boolean): String {
        val blobId = BlobId.of(bucketName, fileName)
        val blobInfo = BlobInfo.newBuilder(blobId).build()
        FileInputStream(localFilePath).use { inputStream ->
            val blob = storage.createFrom(blobInfo, inputStream)

            if (isPublic) {
                blob.toBuilder().setAcl(listOf(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))).build().update()
            }
        }
        return fileName
    }

    override fun downloadFile(fileName: String): ByteArray {
        val blob = storage.get(BlobId.of(bucketName, fileName))
        return blob.getContent()
    }

    override fun downloadFileToLocal(fileName: String, localFilePath: String) {
        val blob = storage.get(BlobId.of(bucketName, fileName))
        val file = File(localFilePath)
        FileOutputStream(file).use { fos ->
            fos.write(blob.getContent())
        }
    }

    override fun getPublicUrl(fileName: String): String {
        return "https://storage.googleapis.com/$bucketName/$fileName"
    }
}
