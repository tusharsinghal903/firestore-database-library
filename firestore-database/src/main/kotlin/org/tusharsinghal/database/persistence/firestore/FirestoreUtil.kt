package org.tusharsinghal.database.persistence.firestore

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import java.io.InputStream

object FirestoreUtil {
    fun initializeFirestore(serviceAccountResource: InputStream, appName: String): Firestore {
        val firebaseApp = FirebaseUtil.initializeFirebase(serviceAccountResource, appName)
        return FirestoreClient.getFirestore(firebaseApp)
    }
}
