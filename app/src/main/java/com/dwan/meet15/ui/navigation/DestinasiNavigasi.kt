package com.dwan.meet15.ui.navigation

interface DestinasiNavigasi {
    val route: String // String untuk identifikasi rute navigasi
    val titleRes: String // String untuk judul halaman/screen
}

object DestinasiHome: DestinasiNavigasi {
    override val route: String = "home" // Rute untuk halaman home
    override val titleRes: String = "Home" // Judul untuk halaman home
}

object DestinasiInsert: DestinasiNavigasi {
    override val route: String = "insert"
    override val titleRes: String = "Insert"
}

object DestinasiDetail: DestinasiNavigasi {
    override val route: String = "detail/{nim}"
    override val titleRes: String = "Detail"
}