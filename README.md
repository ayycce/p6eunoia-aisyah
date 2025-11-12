# 🌸 Eunoia Journal

**Eunoia (yoo-noy-ah):** _"Beautiful thinking; a well mind."_

Your personal, aesthetic, and private space to reflect and grow. This is a modern, local-first Android journaling application built with 100% Jetpack Compose.

![Eunoia Home Screen Banner](URL_UNTUK_BANNER_HOMESCREEN_MU)

---

### 📝 About This Project

This project was created as a submission for the **Mobile Programming Practicum 6** at Universitas Antasari, focusing on local data persistence.

This application is a complete and creative modification of the base module, focusing on a **soft, clean, aesthetic, and modern user experience (UX)**. It ditches standard layouts for a more "Eunoia" vibe, featuring custom themes, soft color palettes, floating cards, custom fonts, and an intuitive, user-friendly flow.

### ✨ Features

* **Full CRUD Functionality:** Create, Read, Update, and Delete journal entries.
* **100% Offline & Private:** All data is stored securely on your device using the **Room ORM** database.
* **Aesthetic 4-Step Onboarding:** A beautiful, user-friendly onboarding flow that saves user preferences (like name) using **Jetpack DataStore**.
* **Modern UI/UX:** Built entirely with **Jetpack Compose** and **Material 3**, featuring a custom "Eunoia" theme (soft lavender, warm ivory, and peach).
* **Aesthetic Layouts:** Ditches standard lists for "floating" **Cards** with soft shadows for a cleaner, more modern feel.
* **Custom Typography:** Implements the **Quicksand** font family for a soft and rounded text appearance.
* **Functional Tab Navigation:**
    * **Home:** An aesthetic list of all journal entries with a search function.
    * **Calendar:** A custom grid calendar that displays dots (🟣) on days with entries and shows a list of entries for the selected date.
    * **Insights:** A statistics dashboard showing total entries, mood counts (with a Donut Chart 🍩), and journaling streaks (🔥).
    * **Settings:** A clean, modern (mockup) settings screen.
* **Modern Navigation:** Uses `NavHostController` for all navigation, including a custom `BottomNavBar` with a centered `FloatingActionButton` (FAB) for creating new entries.
* **Advanced Form Input:** Beautiful Material 3 `DatePicker` and `TimePicker` dialogs for selecting the exact date and time of your memories.

### 🛠️ Tech Stack & Dependencies

* **Language:** Kotlin
* **UI:** Jetpack Compose (Material 3)
* **State Management:** `remember`, `mutableStateOf`, and Lifecycle `ViewModel`
* **Navigation:** Jetpack Navigation Component (`NavHostController`)
* **Local Database (CRUD):** Room ORM
* **User Preferences:** Jetpack DataStore (Proto/Preferences)
* **Asynchronicity:** Kotlin Coroutines & `Flow` (for reactive data in Calendar/Insights)
* **UI Helpers:** Coil (for loading images - *jika kamu pakai*), Custom Fonts (Quicksand)

### 📸 Screenshots

*(Ganti `URL_...` ini dengan link ke screenshot-mu setelah kamu meng-upload-nya ke GitHub, Google Drive, atau Imgur)*

| Onboarding (Welcome & Name) | Onboarding (Hello & Start) |
| :---: | :---: |
| <img src="URL_SCREENSHOT_ONBOARDING_1_&_2" width="300"> | <img src="URL_SCREENSHOT_ONBOARDING_3_&_4" width="300"> |
| **Home Screen (Aesthetic List)** | **Calendar (Grid & List)** |
| <img src="URL_SCREENSHOT_HOME" width="300"> | <img src="URL_SCREENSHOT_CALENDAR" width="300"> |
| **New Entry (with Pickers)** | **Detail Screen (Aesthetic Read)** |
| <img src="URL_SCREENSHOT_NEW_ENTRY" width="300"> | <img src="URL_SCREENSHOT_DETAIL" width="300"> |
| **Insights (Stats & Chart)** | **Settings (Clean Menu)** |
| <img src="URL_SCREENSHOT_INSIGHTS" width="300"> | <img src="URL_SCREENSHOT_SETTINGS" width="300"> |

### 🚀 Getting Started

1.  Clone this repository:
    ```bash
    git clone [https://github.com/Namamu/p6eunoia-aisyah.git](https://github.com/Namamu/p6eunoia-aisyah.git)
    ```
2.  Open the project in **Android Studio** (Iguana 🦎 or newer recommended).
3.  Ensure you have the Android SDK 34 installed.
4.  All required fonts (`Quicksand`) are already included in the `app/src/main/res/font` directory.
5.  Build the project (`Build > Rebuild Project`).
6.  Run the app on an emulator or a physical device (API 24+).

### Acknowledgements

* This project is based on the **Praktikum 6 Modul "Menggunakan Database Lokal"** for Mobile Programming.
* [cite_start]Guidance from lecturer: **Muhayat, M.IT.** [cite: 9]
* Special thanks to **Gemini** for the step-by-step guidance and aesthetic modifications. 😉