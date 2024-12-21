package org.tusharsinghal.database.persistence.firestore

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.InputStream

object FirebaseUtil {
    fun initializeFirebase(serviceAccountResource: InputStream, appName: String): FirebaseApp {
        val credentials = GoogleCredentials.fromStream(serviceAccountResource)
        val options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build()
        return FirebaseApp.initializeApp(options, appName)
    }

}