import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
val DATABASENAME = "MY DATABASE"
val TABLENAME = "Users"
val COL_NAME = "name"
val COL_LOC1 = "longitude"
val COL_LOC2 = "latitude"
val COL_ID = "id"
class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
1) {
   override fun onCreate(db: SQLiteDatabase?) {
      val createTable = "CREATE TABLE " + TABLENAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_NAME + " VARCHAR(256)," + COL_LOC1 + " DOUBLE,"+ COL_LOC2 + "DOUBLE)"
      db?.execSQL(createTable)
   }
   override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
      //onCreate(db);
   }
   fun insertData(user: User) {
      val database = this.writableDatabase
      val contentValues = ContentValues()
      contentValues.put(COL_NAME, user.name)
      contentValues.put(COL_LOC1, user.longitude)
      contentValues.put(COL_LOC2,user.latitude)
      val result = database.insert(TABLENAME, null, contentValues)
      if (result == (0).toLong()) {
         Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
      }
      else {
         Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
      }
   }
   fun readData(): MutableList<User> {
      val list: MutableList<User> = ArrayList()
      val db = this.readableDatabase
      val query = "Select * from $TABLENAME"
      val result = db.rawQuery(query, null)
      if (result.moveToFirst()) {
         do {
            val user = User()
            user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
            user.name = result.getString(result.getColumnIndex(COL_NAME))
            user.longitude = result.getString(result.getColumnIndex(COL_LOC1)).toDouble()
            user.latitude = result.getString(result.getColumnIndex(COL_LOC2)).toDouble()
            list.add(user)
         }
         while (result.moveToNext())
      }
      return list
   }
}

class User(var name:String? = null, var longitude: Double ?= null, var latitude: Double ?= null, var id: Int? = null)
