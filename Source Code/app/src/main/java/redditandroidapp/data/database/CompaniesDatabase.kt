package redditandroidapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(CompanyDatabaseEntity::class)], version = 1)
abstract class CompaniesDatabase : RoomDatabase() {
    abstract fun getCompaniesDao(): CompaniesDao
}