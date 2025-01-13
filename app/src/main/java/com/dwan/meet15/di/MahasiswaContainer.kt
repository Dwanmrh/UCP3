package com.dwan.meet15.di

import android.content.Context
import com.dwan.meet15.repository.NetworkRepository
import com.dwan.meet15.repository.RepositoryMhs
import com.google.firebase.firestore.FirebaseFirestore

interface InterfaceContainerApp { // Keempat dibuat
    val repositoryMhs: RepositoryMhs // Mengelola data mahasiswa
}

class MahasiswaContainer(private val context: Context) : InterfaceContainerApp {
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance() // Base function yg harus digunakan setara base url
    override val repositoryMhs: RepositoryMhs by lazy {
        NetworkRepository(firestore) // Memanggil firestore
    }
}