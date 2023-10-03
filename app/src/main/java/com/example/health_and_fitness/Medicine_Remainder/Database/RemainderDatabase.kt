package com.example.health_and_fitness.Medicine_Remainder.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.health_and_fitness.Medicine_Remainder.Model.Reminder

class RemainderDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "RemainderDatabase"
        private const val TABLE_REMAINDERS = "MRemainderTable"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DATE = "date"
        private const val KEY_TIME = "time"
        private const val KEY_REPEAT = "repeat"
        private const val KEY_REPEAT_NO = "repeat_no"
        private const val KEY_REPEAT_TYPE = "repeat_type"
        private const val KEY_ACTIVE = "active"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val CERATE_REMAINDERS_TABLE = "CREATE TABLE $TABLE_REMAINDERS(" +
                "$KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_TITLE TEXT," +
                "$KEY_DATE TEXT," +
                "$KEY_TIME INTEGER," +
                "$KEY_REPEAT BOOLEAN," +
                "$KEY_REPEAT_NO INTEGER," +
                "$KEY_REPEAT_TYPE TEXT," +
                "$KEY_ACTIVE BOOLEAN)"
        sqLiteDatabase.execSQL(CERATE_REMAINDERS_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion >= newVersion) {
            return
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_REMAINDERS")
            onCreate(sqLiteDatabase)
        }
    }

    fun addRemainder(reminder: Reminder): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_TITLE, reminder.title)
        values.put(KEY_DATE, reminder.date)
        values.put(KEY_TIME, reminder.time)
        values.put(KEY_REPEAT, reminder.repeat)
        values.put(KEY_REPEAT_NO, reminder.repeatNo)
        values.put(KEY_REPEAT_TYPE, reminder.repeatType)
        values.put(KEY_ACTIVE, reminder.active)

        val ID = db.insert(TABLE_REMAINDERS, null, values)
        db.close()
        return ID.toInt()
    }

    fun getRemainder(id: Int): Reminder {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_REMAINDERS,
            arrayOf(
                KEY_ID, KEY_TITLE, KEY_DATE, KEY_TIME,
                KEY_REPEAT, KEY_REPEAT_NO, KEY_REPEAT_TYPE, KEY_ACTIVE
            ),
            "$KEY_ID=?", arrayOf(id.toString()), null, null, null, null
        )

        cursor.moveToFirst()

        val reminder = Reminder(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4),
            cursor.getString(5),
            cursor.getString(6),
            cursor.getString(7)
        )
        cursor.close()
        return reminder
    }

    fun getAllRemainders(): List<Reminder> {
        val remainderList = ArrayList<Reminder>()
        val selectQuery = "SELECT * FROM $TABLE_REMAINDERS"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val reminder = Reminder(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
                )
                remainderList.add(reminder)
            } while (cursor.moveToNext())

        }
        cursor.close()
        return remainderList
    }

    fun getRemaindersCount(): Int {
        val countQuery = "SELECT * FROM $TABLE_REMAINDERS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        return count
    }

    fun updateRemainder(reminder: Reminder): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, reminder.title)
        values.put(KEY_DATE, reminder.date)
        values.put(KEY_TIME, reminder.time)
        values.put(KEY_REPEAT, reminder.repeat)
        values.put(KEY_REPEAT_NO, reminder.repeatNo)
        values.put(KEY_REPEAT_TYPE, reminder.repeatType)
        values.put(KEY_ACTIVE,reminder.active)
        val updateRows = db.update(TABLE_REMAINDERS,values,"$KEY_ID=?", arrayOf(reminder.id.toString()))
        db.close()
        return updateRows
    }

    fun deleteRemainder(reminder: Reminder){
        val db = this.writableDatabase
        db.delete(TABLE_REMAINDERS,"$KEY_ID=?", arrayOf(reminder.id.toString()))
        db.close()
    }
}