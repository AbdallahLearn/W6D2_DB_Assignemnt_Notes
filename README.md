# Notes App

## 📌 Description
The **Notes App** is an Android application that allows users to create, edit, view, and delete notes using Room Database.

## 📂 Features
- **Add Notes**: Users can create and save notes.
- **View Notes**: Display a list of saved notes.
- **Edit Notes**: Modify the content of an existing note.
- **Delete Notes**: Remove notes from the database after confirmation.
- **Navigation**: Smooth navigation between screens using Jetpack Navigation.

## 🏗️ Tech Stack
- **Kotlin**
- **Android Jetpack Components** (ViewModel, LiveData, Room, Navigation)
- **Room Database** (SQLite)
- **MVVM Architecture**
- **Material UI Components**

---

## ⚙️ Database Schema
The app uses **Room Database** for local data storage. Below is the schema design:

### 📌 Entity: `Note`
```kotlin
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String
)
```

### 📌 DAO Interface
```kotlin
@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}
```

### 📌 Database Instance
```kotlin
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

---

## 🎨 UI Screens

### **1️⃣ Add/Edit Note Screen**
- **Text Fields**: Allows users to enter/edit a note's title and content.
- **Save Button**: Saves the note to the database.

### **2️⃣ Notes List Screen**
- **Displays a list** of saved notes.
- **Edit Button**: Opens a note for editing.
- **Delete Button**: Removes a note after confirmation.

---

## 🚀 How to Run the App

### **Step 1: Clone the Repository**
```sh
git clone https://github.com/AbdallahLearn/W6D2_DB_Assignemnt_Notes.git
cd NotesApp
```

### **Step 2: Open in Android Studio**
- Open **Android Studio**.
- Click on **Open an Existing Project**.
- Select the `NotesApp` folder.

### **Step 3: Run the App**
- Connect an **Android Emulator** or a **Physical Device**.
- Click **Run ▶️** in Android Studio.

---

## 📸 Screenshots / Video
(Add screenshots or a demo video link here to show app functionality.)

---

## 📜 Submission Requirements
✅ **GitHub Repository:** [Link to GitHub Repository]
✅ **Clean and Modular Code:** Follows MVVM and best practices.
✅ **README.md:** This document explaining app details.
✅ **Screenshots/Video:** Demonstration of CRUD operations.

---

## 📌 Credits
Developed by **[Your Name]** for Room Database Implementation Task.

---

