package com.expense.tracker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.expense.tracker.core.data.local.entity.ExpenseEntity
import com.expense.tracker.core.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM expenses ORDER BY date DESC, created_at DESC")
    fun observeAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun observeById(id: Long): Flow<ExpenseEntity?>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getById(id: Long): ExpenseEntity?

    @Query(
        """
        SELECT * FROM expenses 
        WHERE date >= :startDate AND date <= :endDate 
        ORDER BY date DESC
    """,
    )
    fun observeByDateRange(
        startDate: Long,
        endDate: Long,
    ): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT * FROM expenses 
        WHERE category_id = :categoryId 
        ORDER BY date DESC
    """,
    )
    fun observeByCategory(categoryId: Long): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT * FROM expenses 
        WHERE type = :type 
        ORDER BY date DESC
    """,
    )
    fun observeByType(type: TransactionType): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM expenses 
        WHERE type = :type AND date >= :startDate AND date <= :endDate
    """,
    )
    fun observeTotalByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): Flow<Double>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM expenses 
        WHERE type = 'EXPENSE' AND category_id = :categoryId 
        AND date >= :startDate AND date <= :endDate
    """,
    )
    suspend fun getTotalExpensesByCategoryAndDateRange(
        categoryId: Long,
        startDate: Long,
        endDate: Long,
    ): Double

    @Query(
        """
        SELECT * FROM expenses 
        WHERE title LIKE '%' || :query || '%' 
        OR note LIKE '%' || :query || '%'
        ORDER BY date DESC
    """,
    )
    fun search(query: String): Flow<List<ExpenseEntity>>
}
