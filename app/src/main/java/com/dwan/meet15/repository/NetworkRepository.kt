package com.dwan.meet15.repository

import com.dwan.meet15.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkRepository( // Ketiga dibuat
    private val firestore: FirebaseFirestore // Memanggil firebase
) : RepositoryMhs { // Akses ke RepositoryMhs

    override suspend fun insertMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa").add(mahasiswa).await()
        } catch (e: Exception) {
            throw Exception("Gagal menambahkan data mahasiswa: ${e.message}")
        }
    }

    override fun getAllMhs(): Flow<List<Mahasiswa>> = callbackFlow { // Pasangan real time dengan callbackflow
        // Membuka collection dari firestore
        val mhsCollection = firestore.collection("Mahasiswa")
            .orderBy("nim", Query.Direction.ASCENDING)
            .addSnapshotListener{ // Agar realtime
                    value, error ->
                if(value != null) {
                    val mhsList = value.documents.mapNotNull {
                        // Convert dari document firestore ke data class
                        it.toObject(Mahasiswa::class.java)!!
                    }
                    // Fungsi untuk mengirim collection ke data class
                    trySend(mhsList) // try send memberikan fungsi untuk mengirim data ke flow
                }
            }
        awaitClose {
            // Menutup collection dari firestore
            mhsCollection.remove()
        }
    }

    override fun getMhs(nim: String): Flow<Mahasiswa> = callbackFlow {
        // Membuat listener untuk memantau dokumen mahasiswa di Firestore
        val mhsDocument = firestore.collection("Mahasiswa")
            .document(nim)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    // Mengkonversi data Firestore ke objek Mahasiswa
                    val mhs = value.toObject(Mahasiswa::class.java)!!
                    trySend(mhs)
                }
            }

        awaitClose {
            mhsDocument.remove()
        }
    }

    override suspend fun deleteMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim)
                .delete()
                .await()

        } catch (e:Exception) {
            throw Exception("Gagal menghapus data mahasiswa:${e.message}")
        }
    }

    override suspend fun updateMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim)
                .set(mahasiswa)
                .await()

        } catch (e:Exception) {
            throw Exception("Gagal mengupdate data mahasiswa:${e.message}")
        }
    }
}