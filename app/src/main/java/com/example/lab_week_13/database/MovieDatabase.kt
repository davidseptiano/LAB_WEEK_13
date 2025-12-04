package com.example.lab_week_13.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab_week_13.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        // @Volatile mencegah Race Condition.
        // Jika thread lain sedang memperbarui database melalui instance,
        // nilai dari instance akan segera terlihat oleh thread lain.
        // Ini memastikan bahwa nilai instance selalu terbarui dan sama
        // untuk semua thread eksekusi.
        @Volatile
        private var instance: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            // synchronized() memastikan bahwa hanya satu thread yang dapat
            // mengeksekusi blok kode ini pada satu waktu.
            // Jika beberapa thread mencoba mengeksekusi blok ini secara bersamaan,
            // hanya satu thread yang dapat menjalankannya sementara thread lain
            // menunggu thread pertama selesai.
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MovieDatabase {
            return Room.databaseBuilder(
                context.applicationContext, // Gunakan applicationContext untuk mencegah memory leak
                MovieDatabase::class.java,
                "movie-db"
            ).build()
        }
    }
}
