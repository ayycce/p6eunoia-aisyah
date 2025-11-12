package id.antasari.p6eunoia_aisyah.ui.navigation

object Routes {
    // Rute lama (biarkan saja)
    const val ONBOARD = "onboard" // Kita ganti ini nanti
    const val HOME = "home"
    const val NEW = "new"
    const val DETAIL = "detail/{entryId}"
    const val EDIT = "edit/{entryId}"
    const val CALENDAR = "calendar"
    const val INSIGHTS = "insights"
    const val SETTINGS = "settings"

    // --- TAMBAHKAN 4 RUTE BARU INI ---
    const val ONBOARD_WELCOME = "onboarding/welcome"
    const val ONBOARD_ASKNAME = "onboarding/askname"
    const val ONBOARD_HELLO = "onboarding/hello"
    const val ONBOARD_CTA = "onboarding/cta" // CTA = Call to Action

    // --- Helper function (sudah ada) ---
    fun detailScreen(entryId: Int) = "detail/$entryId"
    fun editScreen(entryId: Int) = "edit/$entryId"
}