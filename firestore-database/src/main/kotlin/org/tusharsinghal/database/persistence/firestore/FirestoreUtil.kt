package org.tusharsinghal.database.persistence.firestore

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import java.io.InputStream

object FirestoreUtil {
    fun initializeFirestore(serviceAccountResource: InputStream, appName: String): Firestore {
        val firebaseApp = FirebaseUtil.initializeFirestore(serviceAccountResource, appName)
        return FirestoreClient.getFirestore(firebaseApp)
    }
}
