package com.dwan.meet15.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dwan.meet15.ui.pages.DetailMhsScreen
import com.dwan.meet15.ui.pages.HomeScreen
import com.dwan.meet15.ui.pages.InsertMhsView

@Composable
fun PengelolaHalaman(
    modifier: Modifier,
    // NavController untuk mengatur navigasi, dengan default value dari remember
    navController: NavHostController = rememberNavController()
) {
    // NavHost sebagai container untuk navigasi
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route, // Rute awal/pertama yang ditampilkan
        modifier = modifier
    ) {
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToItemEntry = {
                    navController.navigate(DestinasiInsert.route)
                },
                onDetailClick = { nim ->
                    navController.navigate("detail/$nim") // Navigasi ke halaman detail dengan NIM
                }
            )
        }
        composable(DestinasiInsert.route) {
            InsertMhsView(
                onBack = { navController.popBackStack() },
                onNavigate = {
                    navController.navigate(DestinasiHome.route)
                }
            )
            composable(
                route = "detail/{nim}",
                arguments = listOf(navArgument("nim") {
                    type = NavType.StringType
                }) // Definisikan parameter "nim"
            ) { backStackEntry ->
                val nim = backStackEntry.arguments?.getString("nim")
                    ?: "" // Ambil nilai NIM dari parameter
                DetailMhsScreen(
                    nim = nim,
                    navigateEdit = { editNim ->
                        navController.navigate("update/$editNim") // Navigasi ke halaman update dengan NIM
                    },
                    navigateBack = {
                        navController.popBackStack() // Kembali ke halaman sebelumnya
                    }
                )
            }
        }
    }
}