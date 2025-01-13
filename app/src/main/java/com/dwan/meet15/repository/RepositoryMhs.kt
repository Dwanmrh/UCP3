package com.dwan.meet15.repository

import com.dwan.meet15.model.Mahasiswa
import kotlinx.coroutines.flow.Flow

interface RepositoryMhs { // Kedua dibuat
    suspend fun insertMhs(mahasiswa: Mahasiswa) // insertMhs

    // getAllMhs
    fun getAllMhs(): Flow<List<Mahasiswa>> // Mengembalikan data dalam list secara real-time

    // getMhs
    fun getMhs(nim: String): Flow<Mahasiswa> // Mengambil data mahasiswa berdasarkan NIM

    // deleteMhs
    suspend fun deleteMhs(mahasiswa: Mahasiswa) // Menghapus data mahasiswa

    // updateMhs
    suspend fun updateMhs(mahasiswa: Mahasiswa) // Memperbarui data mahasisiswa
}