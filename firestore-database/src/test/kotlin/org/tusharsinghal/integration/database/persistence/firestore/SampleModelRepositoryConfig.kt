package org.tusharsinghal.integration.database.persistence.firestore

import com.google.cloud.firestore.Firestore
import org.tusharsinghal.base.domain.SampleModel
import org.tusharsinghal.database.domain.DatabaseRepository
import org.tusharsinghal.database.persistence.firestore.FirestoreRepository
import org.tusharsinghal.database.persistence.firestore.FirestoreUtil
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class SampleModelRepositoryConfig {
    companion object {

        private fun getServiceAccountStream(): InputStream {
            val serviceAccountJson: String = System.getenv("FIREBASE_ADMIN_SERVICE_ACCOUNT_JSON")
                ?: throw IllegalStateException("FIREBASE_ADMIN_SERVICE_ACCOUNT_JSON is not set")

            return ByteArrayInputStream(serviceAccountJson.toByteArray())
        }

        private fun sampleFirestore(): Firestore {
//            val filePath = "/Users/personal-tushar/Projects/kotlinboilerplate/src/main/resources/config/firebaseAdminServiceAccount.json"
//            val file = File(filePath)
//            val serviceAccountStream: InputStream = FileInputStream(file)
            return FirestoreUtil.initializeFirestore(getServiceAccountStream(), "UserFirestoreApp")
        }

        fun sampleRepository(): DatabaseRepository<SampleModel> {
            return FirestoreRepository(sampleFirestore(), "samples", SampleModel::class)
        }
    }


}
