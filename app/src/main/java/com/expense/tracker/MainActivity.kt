package com.expense.tracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.expense.tracker.common.navigation.VolakoNavGraph
import com.expense.tracker.common.theme.VolakoTheme
import com.expense.tracker.core.domain.repository.CategoryRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var categoryRepository: CategoryRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        runBlocking { categoryRepository.seedDefaultCategories() }
        setContent {
            VolakoTheme {
                VolakoNavGraph()
            }
        }
    }
}
