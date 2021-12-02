Svetlana Kurintseva 31/10/2021

Notes App

This is a note taking app. Provides for editing notes and a mark on the completion of the note.

04/11/21 branch lesson-2: Added a main screen with notes, trying to add a screen with note details and the ability to edit a note.

04/11/21 branch lesson-2: Fixed screen with note details, but still there is no data saving from edited note.

17/11/21 branch lesson-3: Trying to use SharedViewModel to pass data from DetailFragment to MainFragment.

18/11/21 branch lesson-4: I deleted two Fragments and rewrote the implementation for two Activities (Main and AddEditNote).

18/11/21 branch lesson-4: Trying to implement saving notes data with Room.

22/11/21 branch lesson-5: Fixed naming of variables. Added link field and link preview. WebView does not load preview, there is an error.

28/11/21 branch lesson-6: Implemented NetworkMonitor for subscription to the link change event (ACCESS_NETWORK_STATE) and notify the user about it.

28/11/21 branch lesson-6: Added NoteAnalyticsService, added logging of all operations with notes (add, edit, delete).

03/12/21 branch lesson-7: Removed link field from NoteEntity. Moved in utils directory: NetworkMonitor.kt and NoteAnalyticsService.kt.

03/12/21 branch lesson-7: Added "movie of the day" field for each note. Implemented getting Array Movie from TheMovieDB with Retrofit. Used Coil to load movie poster.
