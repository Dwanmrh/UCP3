package com.dwan.meet15.model

data class Mahasiswa( // Pertama yg dibuat dari semuanya
    val nim: String,
    val nama: String,
    val alamat: String,
    val jenisKelamin: String,
    val kelas: String,
    val angkatan: String,
    val judul: String,
    val dosbim1: String,
    val dosbim2: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "") // Harus dibangun dengan konstruktor
}
