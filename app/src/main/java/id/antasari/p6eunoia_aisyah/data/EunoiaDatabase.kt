package id.antasari.p6eunoia_aisyah.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// PASTIKAN DI SINI ADA 'DiaryEntry::class'
@Database(
    entities = [DiaryEntry::class], //
    version = 1,
    exportSchema = false
)
abstract class EunoiaDatabase : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: EunoiaDatabase? = null

        fun getInstance(context: Context): EunoiaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    EunoiaDatabase::class.java,
                    "eunoia.db" //
                )
                    .build()
                    .also { db -> INSTANCE = db }
            }
        }
    }
}