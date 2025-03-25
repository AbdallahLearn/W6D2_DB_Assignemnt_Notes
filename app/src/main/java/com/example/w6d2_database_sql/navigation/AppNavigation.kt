package com.example.w6d2_database_sql.navigation



import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.w6d2_database_sql.AddNote
import com.example.w6d2_database_sql.DisplayData
import com.example.w6d2_database_sql.vm.NoteViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: NoteViewModel = viewModel() // Provide ViewModel outside NavHost

    NavHost(
        navController = navController,
        startDestination = "DisplayData", // Ensure consistency
        modifier = modifier
    ) {
        composable("DisplayData") {
            DisplayData(navController, viewModel) // Pass ViewModel
        }
        composable("AddNote") {
            AddNote(navController, viewModel) // Pass ViewModel
        }
        composable("AddNote/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            AddNote(navController, viewModel, noteId) // Pass noteId for editing
        }
    }
}


