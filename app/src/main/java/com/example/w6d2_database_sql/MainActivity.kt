package com.example.w6d2_database_sql

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.w6d2_database_sql.model.Note
import com.example.w6d2_database_sql.navigation.AppNavigation
import com.example.w6d2_database_sql.ui.theme.W6D2_Database_sqlTheme
import com.example.w6d2_database_sql.vm.NoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            W6D2_Database_sqlTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(navController, Modifier.padding(innerPadding))
                }
            }
        }


    }
}

@Composable
fun DisplayData(navController: NavController, viewModel: NoteViewModel  ) {
    val notes by viewModel.allNotes.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        if (notes.isEmpty()) {
            Text(text = "No data available there") // No data found
        } else {
            LazyColumn {
                items(notes) { note: Note -> // Ensure correct type
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp) // Correct elevation
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) { // Text takes most space
                                Text(
                                    text = note.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = note.content,
                                    fontSize = 16.sp
                                )
                            }

                            // ✅ Edit Icon
                            IconButton(onClick = {
                                navController.navigate("AddNote/${note.id}") // Open note for editing
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Note",
                                    tint = Color.Green
                                )
                            }


                            var showDialog by remember { mutableStateOf(false) } // State to show/hide dialog

                            IconButton(onClick = { showDialog = true }) { // Show dialog on click
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Note",
                                    tint = Color.Red
                                )
                            }

                            // Display confirmation dialog before deleting
                            if (showDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false }, // Close dialog when dismissed
                                    title = { Text("Delete Note") },
                                    text = { Text("Are you sure you want to delete this note?") },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                viewModel.delete(note) // Delete note
                                                showDialog = false // Close dialog
                                            }
                                        ) {
                                            Text("Delete")
                                        }
                                    },
                                    dismissButton = {
                                        Button(
                                            onClick = { showDialog = false } //Cancel deletion
                                        ) {
                                            Text("Cancel")
                                        }
                                    }
                                )
                            }

                        }
                    }
                }
            }

        }

        Button(onClick = { navController.navigate("AddNote") }) {
            Text("Add Note")
        }
    }
}


@Composable
fun AddNote(navController: NavController, viewModel: NoteViewModel, noteId: Int? = null) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    //Load existing note data when editing
    LaunchedEffect(noteId) {
        if (noteId != null) {
            val note = viewModel.allNotes.value?.find { it.id == noteId }
            note?.let {
                title = it.title
                content = it.content
                isEditing = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Note Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Note Content") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = Int.MAX_VALUE
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (title.isNotEmpty() && content.isNotEmpty()) {
                        if (isEditing) {
                            //  Update existing note
                            val updatedNote = Note(id = noteId!!, title = title, content = content)
                            viewModel.update(updatedNote)
                        } else {
                            //  Add new note
                            val newNote = Note(title = title, content = content)
                            viewModel.insert(newNote)
                        }
                        navController.popBackStack() // Go back
                    }
                }
            ) {
                Text(if (isEditing) "Save Edit" else "Save Data")
            }

            Button(
                onClick = { navController.popBackStack() }
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Go Back")
                }
            }
        }
    }
}
