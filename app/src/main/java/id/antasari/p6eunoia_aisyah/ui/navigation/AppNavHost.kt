package id.antasari.p6eunoia_aisyah.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import id.antasari.p6eunoia_aisyah.data.DiaryViewModel
import id.antasari.p6eunoia_aisyah.datastore.UserViewModel

// Ganti import OnboardScreen dengan 4 layar baru
import id.antasari.p6eunoia_aisyah.ui.screens.AskNameScreen
import id.antasari.p6eunoia_aisyah.ui.screens.EditEntryScreen
import id.antasari.p6eunoia_aisyah.ui.screens.HelloScreen
import id.antasari.p6eunoia_aisyah.ui.screens.HomeScreen
import id.antasari.p6eunoia_aisyah.ui.screens.InsightsScreen
import id.antasari.p6eunoia_aisyah.ui.screens.NewEntryScreen
import id.antasari.p6eunoia_aisyah.ui.screens.NoteDetailScreen
import id.antasari.p6eunoia_aisyah.ui.screens.SettingsScreen
import id.antasari.p6eunoia_aisyah.ui.screens.StartJournalingScreen
import id.antasari.p6eunoia_aisyah.ui.screens.WelcomeScreen
import id.antasari.p6eunoia_aisyah.ui.screens.calendar.CalendarScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String, // Ini akan jadi "onboarding/welcome" atau "home"
    userViewModel: UserViewModel,
    diaryViewModel: DiaryViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // --- GANTI BLOK ONBOARDING LAMA ---
        // Hapus: composable(Routes.ONBOARD) { ... }

        // --- DENGAN 4 RUTE BARU INI ---

        composable(Routes.ONBOARD_WELCOME) {
            WelcomeScreen(
                onGetStarted = { navController.navigate(Routes.ONBOARD_ASKNAME) },
                onLogin = { /* (Abaikan dulu) */ }
            )
        }

        composable(Routes.ONBOARD_ASKNAME) {
            AskNameScreen(
                onConfirm = { name ->
                    userViewModel.saveName(name) // Simpan nama
                    navController.navigate(Routes.ONBOARD_HELLO) // Lanjut
                },
                onSkip = {
                    userViewModel.saveName("") // Simpan nama kosong
                    navController.navigate(Routes.ONBOARD_HELLO) // Lanjut
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ONBOARD_HELLO) {
            val userName by userViewModel.userName.collectAsState()
            HelloScreen(
                userName = userName,
                onNext = { navController.navigate(Routes.ONBOARD_CTA) }
            )
        }

        composable(Routes.ONBOARD_CTA) {
            StartJournalingScreen(
                onStart = {
                    // TANDAI ONBOARDING SELESAI!
                    userViewModel.finishOnboarding()
                    // Pergi ke Home & Hapus semua riwayat onboarding
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARD_WELCOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // --- SISA RUTE (HOME, DETAIL, DLL) TETAP SAMA ---

        composable(Routes.HOME) {
            val userName by userViewModel.userName.collectAsState()
            HomeScreen(
                userName = userName,
                onOpenEntry = { entryId ->
                    navController.navigate(Routes.detailScreen(entryId))
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val idArg = backStackEntry.arguments?.getInt("entryId") ?: -1
            NoteDetailScreen(
                entryId = idArg,
                onBack = { navController.popBackStack() },
                onDeleted = { navController.popBackStack() },
                onEdit = { id -> navController.navigate(Routes.editScreen(id)) }
            )
        }

        composable(
            route = Routes.EDIT,
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val idArg = backStackEntry.arguments?.getInt("entryId") ?: -1
            EditEntryScreen(
                entryId = idArg,
                onBack = { navController.popBackStack() },
                onSaved = { savedId ->
                    navController.popBackStack()
                    navController.navigate(Routes.detailScreen(savedId)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.NEW) {
            NewEntryScreen(
                onBack = { navController.popBackStack() },
                onSaved = { newId ->
                    navController.popBackStack()
                    navController.navigate(Routes.detailScreen(newId)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.CALENDAR) {
            CalendarScreen(
                diaryViewModel = diaryViewModel,
                onOpenEntry = { entryId ->
                    navController.navigate(Routes.detailScreen(entryId))
                }
            )
        }

        composable(Routes.INSIGHTS) {
            InsightsScreen(
                diaryViewModel = diaryViewModel
            )
        }

        composable(Routes.SETTINGS) {
            val userName by userViewModel.userName.collectAsState()
            SettingsScreen(
                userName = userName
            )
        }
    }
}