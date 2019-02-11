# Note
Simple Note Taking App using SQLite Database and Content Provider

# Functionality 
* Saving,Updating,Deleting Notes using SQLite database .
* Provided content provider to make the app available to be accessed by other apps .
* Used customized CursorAdapter with a ListView to populate the list of notes by binding values to each single row .
* Used CursorLoader to do this in background thread beacuse the process of reading from /writing to database is very expensive .

# Demo 
![empty](https://user-images.githubusercontent.com/21147699/52588968-68105500-2e46-11e9-87e8-ff8d54704681.jpg)
![addnote](https://user-images.githubusercontent.com/21147699/52588994-7494ad80-2e46-11e9-8002-362357bccf80.jpg)
![notes](https://user-images.githubusercontent.com/21147699/52589015-7eb6ac00-2e46-11e9-99e7-7fb9d8f01586.jpg)
![editnote](https://user-images.githubusercontent.com/21147699/52589034-8e35f500-2e46-11e9-8570-1e1acd7af8a0.jpg)
![check_unsave](https://user-images.githubusercontent.com/21147699/52589046-97bf5d00-2e46-11e9-88bd-9bc14e2e53e5.jpg)
![delete](https://user-images.githubusercontent.com/21147699/52589065-a1e15b80-2e46-11e9-8109-0ac9e5988f02.jpg)

# Video 
[Demo](https://youtu.be/BVTF2h-7Kr4)

# Features
* SQLite 
* SQLiteOpenHelper
* ContentProvider
* ListView
* EmptyView
* CardView
* CursorAdapter 
* CursorLoader 
* LoaderManager
* BaseColumns
* Menus
* Dialogs
