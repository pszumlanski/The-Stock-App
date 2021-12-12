package redditandroidapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompaniesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewCompany(companyDatabaseEntity: CompanyDatabaseEntity)

    @Query("SELECT * FROM companies")
    fun getAllSavedCompanies(): LiveData<List<CompanyDatabaseEntity>>?

    @Query("DELETE FROM companies WHERE ticker = :ticker")
    fun removeCompany(ticker: String)

    @Query("DELETE FROM companies")
    fun clearDatabase()
}