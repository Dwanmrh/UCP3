package com.dwan.meet15.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.meet15.model.Mahasiswa
import com.dwan.meet15.repository.RepositoryMhs
import kotlinx.coroutines.launch

class InsertViewModel(
    private val mhs: RepositoryMhs
) : ViewModel() {
    var uiEvent: InsertUiState by mutableStateOf(InsertUiState())
        private set

    // State untuk menyimpan status form (Idle/Loading/Success/Error)
    var uiState: FormState by mutableStateOf(FormState.Idle)
        private set

    // Memperbarui state berdasarkan input pengguna
    fun updateState(mahasiswaEvent: MahasiswaEvent) {
        uiEvent = uiEvent.copy(
            insertUiEvent = mahasiswaEvent,
        )
    }

    // Validasi data input pengguna
    fun validateFields(): Boolean {
        val event = uiEvent.insertUiEvent // Mengambil data event dari UI

        // Membuat object FormErrorState yang berisi pesan error untuk setiap field
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "NIM tidak boleh kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            jenisKelamin = if (event.jenisKelamin.isNotEmpty()) null else "Jenis Kelamin tidak boleh kosong",
            alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong",
            kelas = if (event.kelas.isNotEmpty()) null else "Kelas tidak boleh kosong",
            angkatan = if (event.angkatan.isNotEmpty()) null else "Angkatan tidak boleh kosong",
            judul = if (event.judul.isNotEmpty()) null else "Judul tidak boleh kosong",
            dosbim1 = if (event.dosbim1.isNotEmpty()) null else "Dosbim tidak boleh kosong",
            dosbim2 = if (event.dosbim2.isNotEmpty()) null else "Dosbim tidak boleh kosong"
        )
        uiEvent = uiEvent.copy(isEntryValid = errorState) // Update status validasi pada UI event
        return errorState.isValid() // Mengembalikan hasil validasi
    }
    // Fungsi untuk menyimpan data mahasiswa ke database
    fun insertMhs() {
        if (validateFields()) {
            viewModelScope.launch {
                uiState =
                    FormState.Loading // Set state loading untuk menunjukkan proses sedang berlangsung
                try {
                    // Mengkonversi data UI ke model database dan menyimpannya
                    mhs.insertMhs(uiEvent.insertUiEvent.toMhsModel())
                    uiState = FormState.Success("Data berhasil disimpan")
                } catch (e: Exception) {
                    uiState = FormState.Error("Data gagal disimpan")
                }
            }
        } else {
            uiState = FormState.Error("Data tidak valid")
        }
    }
    // Fungsi untuk mereset form ke kondisi awal
    fun resetForm() {
        uiEvent = InsertUiState() // Reset UI event ke nilai default
        uiState = FormState.Idle // Reset state form ke kondisi idle
    }

    fun resetSnackBarMessage() {
        uiState = FormState.Idle
    }
}

// Sealed class untuk menentukan status/keadaan form
sealed class FormState {
    object Idle : FormState() // Status awal (form belum disubmit)
    object Loading : FormState() // Status loading (sedang memproses)
    data class Success(val message: String) : FormState()
    data class Error(val message: String) : FormState()
}

// Data class untuk menyimpan state keseluruhan form input mahasiswa
data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),  // Menyimpan data input form saat ini
    val isEntryValid: FormErrorState = FormErrorState(),  // Menyimpan status validasi form (error atau valid)
)

// Data class untuk menyimpan pesan error pada setiap field form
data class FormErrorState(
    val nim: String? = null, // Pesan error untuk field NIM
    val nama: String? = null,
    val jenisKelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null,
    val judul: String = null,
    val dosbim1: String = null,
    val dosbim2: String = null
) {
    // Fungsi untuk mengecek apakah form valid (tidak ada error)
    fun isValid(): Boolean {
        return nim == null && nama == null && jenisKelamin == null &&
                alamat == null && kelas == null && angkatan == null
    }
}

// Data class Variabel yang menyimpan data input form
data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val jenisKelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = "",
    val judul: String = "",
    val dosbim1: String = "",
    val dosbim2: String = "",
)

// Menyimpan input form ke dalam entity
fun MahasiswaEvent.toMhsModel(): Mahasiswa = Mahasiswa(
    nim: String,
    nama: String,
    alamat: String,
    jenisKelamin: String,
    kelas: String,
    angkatan: String,
    judul: String,
    dosbim1: String,
    dosbim2: String
)