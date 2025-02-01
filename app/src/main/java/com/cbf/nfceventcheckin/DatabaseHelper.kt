package com.cbf.nfceventcheckin

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "event_check_ins.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_NAME = "event_check_ins"
        const val COLUMN_ID = "id"
        const val COLUMN_SERIAL_NUMBER = "serial_number"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SERIAL_NUMBER TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_TIMESTAMP TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNfcTag(serialNumber: String, email: String, timestamp: String) {
        val db = writableDatabase
        val query = "INSERT INTO $TABLE_NAME ($COLUMN_SERIAL_NUMBER, $COLUMN_EMAIL, $COLUMN_TIMESTAMP) VALUES (?, ?, ?)"
        val statement = db.compileStatement(query)
        statement.bindString(1, serialNumber)
        statement.bindString(2, email)
        statement.bindString(3, timestamp)
        statement.executeInsert()
        db.close()
    }

    @SuppressLint("Range")
    fun getAllCheckedInEmails(serialNumber: String): List<Pair<String, String>> {
        val emailList = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_EMAIL, COLUMN_TIMESTAMP),
            "$COLUMN_SERIAL_NUMBER = ?",
            arrayOf(serialNumber),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            do {
                val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
                val timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP))
                emailList.add(Pair(email, timestamp))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return emailList
    }
}
