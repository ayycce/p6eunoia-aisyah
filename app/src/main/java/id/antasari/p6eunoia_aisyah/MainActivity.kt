package id.antasari.p6eunoia_aisyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// --- Impor-impor BARU untuk Scaffold, FAB, dan Ikon ---
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.* // Ini akan mengimpor Scaffold, FAB, Icon, dll.
// ---
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
// ---
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState // <-- Impor PENTING
import androidx.navigation.compose.rememberNavController
import id.antasari.p6eunoia_aisyah.data.*
import id.antasari.p6eunoia_aisyah.datastore.*
import id.antasari.p6eunoia_aisyah.ui.BottomNavBar // <-- Impor BottomNavBar kita
import id.antasari.p6eunoia_aisyah.ui.navigation.*
import id.antasari.p6eunoia_aisyah.ui.screens.SplashScreen
import id.antasari.p6eunoia_aisyah.ui.theme.EunoiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- Setup Repository & Factory kamu (Ini sudah BENAR) ---
        val userRepo = UserPrefsRepository(applicationContext)
        val userFactory = UserViewModelFactory(userRepo)
        val db = EunoiaDatabase.getInstance(applicationContext)
        val diaryRepo = DiaryRepository(db.diaryDao())
        val diaryFactory = DiaryViewModelFactory(diaryRepo)

        setContent {
            EunoiaTheme {
                // --- Setup ViewModel & State kamu (Ini sudah BENAR) ---
                val userViewModel: UserViewModel = viewModel(factory = userFactory)
                val diaryViewModel: DiaryViewModel = viewModel(factory = diaryFactory)
                val onboardingDone = userViewModel.onboardingDone.collectAsState(initial = false)
                val navController = rememberNavController()

                var showSplash by remember { mutableStateOf(true) }

                // --- MODIFIKASI DIMULAI DI SINI ---

                if (showSplash) {
                    // Tampilkan SplashScreen di atas Surface
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SplashScreen(onTimeout = { showSplash = false })
                    }
                } else {
                    // 1. Ambil rute saat ini untuk tahu kita di layar mana
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    // 2. Tentukan kapan BottomBar & FAB harus tampil
                    val showBottomBar = shouldShowBottomBar(currentRoute)

                    // 3. Gunakan Scaffold sebagai root setelah splash
                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                // Panggil BottomNavBar.kt kita
                                BottomNavBar(navController = navController)
                            }
                        },
                        floatingActionButton = {
                            if (showBottomBar) {
                                // Tampilkan FAB
                                FloatingActionButton(
                                    onClick = {
                                        // Arahkan ke layar NewEntryScreen
                                        navController.navigate(Routes.NEW)
                                    },
                                    // Posisikan agak ke atas
                                    modifier = Modifier.offset(y = 48.dp),
                                    shape = CircleShape,
                                    // Ambil warna aesthetic dari Tema kita!
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "New entry")
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.Center
                    ) { innerPadding ->
                        // 4. Pindahkan Surface & AppNavHost ke dalam Scaffold
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                // Terapkan padding dari Scaffold
                                .padding(innerPadding),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            // Sisa logikamu (AppNavHost) aman di sini
                            AppNavHost(
                                navController = navController,
                                startDestination = if (onboardingDone.value) Routes.HOME else Routes.ONBOARD_WELCOME,
                                userViewModel = userViewModel,
                                diaryViewModel = diaryViewModel,
                                modifier = Modifier.fillMaxSize() // Pastikan NavHost mengisi Surface
                            )
                        }
                    }
                }
                // --- AKHIR MODIFIKASI ---
            }
        }
    }

    // 5. Tambahkan fungsi helper ini di DALAM class MainActivity
    private fun shouldShowBottomBar(route: String?): Boolean =
        route in setOf(Routes.HOME, Routes.CALENDAR, Routes.INSIGHTS, Routes.SETTINGS)
}