package com.example.health_and_fitness.Water_Remainder.Helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SqliteHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "WaterRemainder"
        private const val TABLE_STATS = "stats"
        private const val KEY_ID = "id"
        private const val KEY_DATE = "date"
        private const val KEY_INTOOK = "intook"
        private const val KEY_TOTAL_INTAKE = "totalintake"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        val CREATE_STATS_TABLE = ("CREATE TABLE $TABLE_STATS ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_DATE TEXT UNIQUE,"
                + "$KEY_INTOOK INT, $KEY_TOTAL_INTAKE INT)")
        sqLiteDatabase?.execSQL(CREATE_STATS_TABLE)
    }

    override fun onUpgrade(sqLiteDatabas: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        sqLiteDatabas!!.execSQL("DROP TABLE IF EXISTS" + TABLE_STATS)
        onCreate(sqLiteDatabas)
    }

    fun addAll(date:String,intook:Int,totalintake:Int):Long{
        if (checkExistance(date) == 0) {
            val values = ContentValues()
            values.put(KEY_DATE, date)
            values.put(KEY_INTOOK, intook)
            values.put(KEY_TOTAL_INTAKE, totalintake)
            val db = this.writableDatabase
            val response = db.insert(TABLE_STATS, null, values)
            db.close()
            return response
        }
        return -1
    }

    @SuppressLint("Range")
    fun getIntook(date: String): Int {
        val selectQuery = "SELECT $KEY_INTOOK FROM $TABLE_STATS WHERE $KEY_DATE = ?"
        val db = this.readableDatabase
        db.rawQuery(selectQuery, arrayOf(date)).use {
            if (it.moveToFirst()) {
                return it.getInt(it.getColumnIndex(KEY_INTOOK))
            }
        }
        return 0
    }

    fun addIntook(date: String, selectedOption: Int): Int {
        val intook = getIntook(date)
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_INTOOK, intook + selectedOption)

        val response = db.update(TABLE_STATS, contentValues, "$KEY_DATE = ?", arrayOf(date))
        db.close()
        return response
    }

    fun checkExistance(date: String): Int {
        val selectQuery = "SELECT $KEY_INTOOK FROM $TABLE_STATS WHERE $KEY_DATE = ?"
        val db = this.readableDatabase
        db.rawQuery(selectQuery, arrayOf(date)).use {
            if (it.moveToFirst()) {
                return it.count
            }
        }
        return 0
    }

    fun getAllStats(): Cursor {
        val selectQuery = "SELECT * FROM $TABLE_STATS"
        val db = this.readableDatabase
        return db.rawQuery(selectQuery, null)

    }

    fun updateTotalIntake(date: String, totalintake: Int): Int {
        val intook = getIntook(date)
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TOTAL_INTAKE, totalintake)

        val response = db.update(TABLE_STATS, contentValues, "$KEY_DATE = ?", arrayOf(date))
        db.close()
        return response
    }
}