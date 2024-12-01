package org.tusharsinghal.googleCloudStorage.persistence

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.io.InputStream

object GoogleCloudStorageServiceFactory {
    fun getGoogleStorageObject(serviceAccountResource: InputStream): Storage {
        val credentials = GoogleCredentials.fromStream(serviceAccountResource)
        return StorageOptions.newBuilder().setCredentials(credentials).build().service
    }
}