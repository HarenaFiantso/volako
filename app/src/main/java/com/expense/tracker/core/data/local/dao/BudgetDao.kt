package com.expense.tracker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.expense.tracker.core.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity): Long

    @Update
    suspend fun update(budget: BudgetEntity)

    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        """
        SELECT * FROM budgets 
        WHERE month = :month AND year = :year 
        ORDER BY category_id ASC
    """,
    )
    fun observeByMonthAndYear(
        month: Int,
        year: Int,
    ): Flow<List<BudgetEntity>>

    @Query(
        """
        SELECT * FROM budgets 
        WHERE category_id = :categoryId AND month = :month AND year = :year
        LIMIT 1
    """,
    )
    suspend fun getByCategoryAndPeriod(
        categoryId: Long?,
        month: Int,
        year: Int,
    ): BudgetEntity?

    @Query(
        """
        SELECT * FROM budgets 
        WHERE category_id IS NULL AND month = :month AND year = :year
        LIMIT 1
    """,
    )
    fun observeGlobalBudget(
        month: Int,
        year: Int,
    ): Flow<BudgetEntity?>
}
