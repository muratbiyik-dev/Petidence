package com.pureblacksoft.petidence.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pureblacksoft.petidence.data.Food
import com.pureblacksoft.petidence.data.Pet
import com.pureblacksoft.petidence.data.Play
import com.pureblacksoft.petidence.data.Sleep

class PetidenceDB(private val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_NAME = "petidence_db"
        private const val DATABASE_VERSION = 1

        //region Pet Table
        private const val PET_TABLE = "pet_table"
        private const val PET_ID = "pet_id"
        private const val PET_NAME = "pet_name"
        private const val PET_AGE = "pet_age"
        private const val PET_HEIGHT = "pet_height"
        private const val PET_WEIGHT = "pet_weight"
        private const val PET_BREED = "pet_breed"
        private const val PET_TYPE_ID = "pet_type_id"

        private const val PET_TABLE_SQL = "CREATE TABLE $PET_TABLE (" +
                "$PET_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$PET_NAME VARCHAR(256), " +
                "$PET_AGE INTEGER, " +
                "$PET_HEIGHT INTEGER, " +
                "$PET_WEIGHT INTEGER, " +
                "$PET_BREED VARCHAR(256), " +
                "$PET_TYPE_ID INTEGER)"
        //endregion

        //region Food Table
        private const val FOOD_TABLE = "food_table"
        private const val FOOD_ID = "id"
        private const val FOOD_NAME = "name"
        private const val FOOD_CALORIES = "calories"
        private const val FOOD_FAT = "fat"
        private const val FOOD_CARBOHYDRATE = "carbohydrate"
        private const val FOOD_PROTEIN = "protein"
        private const val FOOD_IMAGE = "image"
        private const val FOOD_FOR_CAT = "for_cat"
        private const val FOOD_FOR_DOG = "for_dog"
        private const val FOOD_FOR_BIRD = "for_bird"
        private const val FOOD_FOR_FISH = "for_fish"

        private const val FOOD_TABLE_SQL = "CREATE TABLE $FOOD_TABLE (" +
                "$FOOD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$FOOD_NAME VARCHAR(100), " +
                "$FOOD_CALORIES FLOAT, " +
                "$FOOD_FAT FLOAT, " +
                "$FOOD_CARBOHYDRATE FLOAT, " +
                "$FOOD_PROTEIN FLOAT, " +
                "$FOOD_IMAGE BLOB, " +
                "$FOOD_FOR_CAT INTEGER, " +
                "$FOOD_FOR_DOG INTEGER, " +
                "$FOOD_FOR_BIRD INTEGER, " +
                "$FOOD_FOR_FISH INTEGER)"
        //endregion

        //region Sleep Table
        private const val SLEEP_TABLE = "sleep_table"
        private const val SLEEP_ID = "id"
        private const val SLEEP_CONTENT = "content"
        private const val SLEEP_PET_TYPE_ID = "pet_type_id"

        private const val SLEEP_TABLE_SQL = "CREATE TABLE $SLEEP_TABLE (" +
                "$SLEEP_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$SLEEP_CONTENT TEXT, " +
                "$SLEEP_PET_TYPE_ID INTEGER)"
        //endregion

        //region Play Table
        private const val PLAY_TABLE = "play_table"
        private const val PLAY_ID = "id"
        private const val PLAY_CONTENT = "content"
        private const val PLAY_PET_TYPE_ID = "pet_type_id"

        private const val PLAY_TABLE_SQL = "CREATE TABLE $PLAY_TABLE (" +
                "$PLAY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$PLAY_CONTENT TEXT, " +
                "$PLAY_PET_TYPE_ID INTEGER)"
        //endregion
    }

    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(PET_TABLE_SQL)
        db.execSQL(FOOD_TABLE_SQL)
        db.execSQL(SLEEP_TABLE_SQL)
        db.execSQL(PLAY_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $PET_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $FOOD_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $SLEEP_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $PLAY_TABLE")

        onCreate(db)
    }

    //region Pet Funcs
    fun insertPet(newPet: Pet)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PET_NAME, newPet.name)
        values.put(PET_AGE, newPet.age)
        values.put(PET_HEIGHT, newPet.height)
        values.put(PET_WEIGHT, newPet.weight)
        values.put(PET_BREED, newPet.breed)
        values.put(PET_TYPE_ID, newPet.typeId)
        db.insert(PET_TABLE, null, values)
    }

    fun updatePet(pet: Pet, updatedPet: Pet)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PET_NAME, updatedPet.name)
        values.put(PET_AGE, updatedPet.age)
        values.put(PET_HEIGHT, updatedPet.height)
        values.put(PET_WEIGHT, updatedPet.weight)
        values.put(PET_BREED, updatedPet.breed)
        values.put(PET_TYPE_ID, updatedPet.typeId)
        val whereClause = "$PET_ID = ?"
        val whereArgs = Array(1) {pet.id.toString()}
        db.update(PET_TABLE, values, whereClause, whereArgs)
    }

    fun deletePet(pet: Pet)
    {
        val db = this.writableDatabase
        val whereClause = "$PET_ID = ?"
        val whereArgs = Array(1) {pet.id.toString()}
        db.delete(PET_TABLE, whereClause, whereArgs)
    }

    fun deletePetTable()
    {
        val db = this.writableDatabase
        db.delete(PET_TABLE, null, null)
        db.close()
    }

    fun readPetList(): MutableList<Pet>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $PET_TABLE"
        val cursor = db.rawQuery(query, null)
        val petList = mutableListOf<Pet>()
        petCursorFirst(cursor, petList)
        db.close()

        return petList
    }

    fun readPetListWithType(petType: Int): MutableList<Pet>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $PET_TABLE WHERE $PET_TYPE_ID = $petType"
        val cursor = db.rawQuery(query, null)
        val petListWithType = mutableListOf<Pet>()
        petCursorFirst(cursor, petListWithType)
        db.close()

        return petListWithType
    }

    private fun petCursorFirst(cursor: Cursor, petList: MutableList<Pet>)
    {
        if (cursor.moveToFirst())
        {
            do
            {
                val pet = Pet()
                pet.id = cursor.getInt(0)
                pet.name = cursor.getString(1)
                pet.age = cursor.getInt(2)
                pet.height = cursor.getInt(3)
                pet.weight = cursor.getInt(4)
                pet.breed = cursor.getString(5)
                pet.typeId = cursor.getInt(6)
                petList.add(pet)
            }
            while (cursor.moveToNext())
        }
        cursor.close()
    }
    //endregion

    //region Food Funcs
    fun insertFood(newFood: Food)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(FOOD_NAME, newFood.name)
        values.put(FOOD_CALORIES, newFood.calories)
        values.put(FOOD_FAT, newFood.fat)
        values.put(FOOD_CARBOHYDRATE, newFood.carbohydrate)
        values.put(FOOD_PROTEIN, newFood.protein)
        values.put(FOOD_IMAGE, newFood.image)
        values.put(FOOD_FOR_CAT, newFood.forCat)
        values.put(FOOD_FOR_DOG, newFood.forDog)
        values.put(FOOD_FOR_BIRD, newFood.forBird)
        values.put(FOOD_FOR_FISH, newFood.forFish)
        db.insert(FOOD_TABLE, null, values)
    }

    fun deleteFoodTable()
    {
        val db = this.writableDatabase
        db.delete(FOOD_TABLE, null, null)
        db.close()
    }

    fun readFoodList(): MutableList<Food>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $FOOD_TABLE"
        val cursor = db.rawQuery(query, null)
        val foodList = mutableListOf<Food>()
        foodCursorFirst(cursor, foodList)
        db.close()

        return foodList
    }

    fun readCatFoodList(foodType: Int): MutableList<Food>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $FOOD_TABLE WHERE $FOOD_FOR_CAT = $foodType ORDER BY $FOOD_NAME"
        val cursor = db.rawQuery(query, null)
        val catFoodList = mutableListOf<Food>()
        foodCursorFirst(cursor, catFoodList)
        db.close()

        return catFoodList
    }

    fun readDogFoodList(foodType: Int): MutableList<Food>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $FOOD_TABLE WHERE $FOOD_FOR_DOG = $foodType ORDER BY $FOOD_NAME"
        val cursor = db.rawQuery(query, null)
        val dogFoodList = mutableListOf<Food>()
        foodCursorFirst(cursor, dogFoodList)
        db.close()

        return dogFoodList
    }

    fun readBirdFoodList(foodType: Int): MutableList<Food>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $FOOD_TABLE WHERE $FOOD_FOR_BIRD = $foodType ORDER BY $FOOD_NAME"
        val cursor = db.rawQuery(query, null)
        val birdFoodList = mutableListOf<Food>()
        foodCursorFirst(cursor, birdFoodList)
        db.close()

        return birdFoodList
    }

    fun readFishFoodList(foodType: Int): MutableList<Food>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $FOOD_TABLE WHERE $FOOD_FOR_FISH = $foodType ORDER BY $FOOD_NAME"
        val cursor = db.rawQuery(query, null)
        val fishFoodList = mutableListOf<Food>()
        foodCursorFirst(cursor, fishFoodList)
        db.close()

        return fishFoodList
    }

    private fun foodCursorFirst(cursor: Cursor, foodList: MutableList<Food>)
    {
        if (cursor.moveToFirst())
        {
            do
            {
                val food = Food()
                food.id = cursor.getInt(0)
                food.name = cursor.getString(1)
                food.calories = cursor.getFloat(2)
                food.fat = cursor.getFloat(3)
                food.carbohydrate = cursor.getFloat(4)
                food.protein = cursor.getFloat(5)
                food.image = cursor.getBlob(6)
                food.forCat = cursor.getInt(7)
                food.forDog = cursor.getInt(8)
                food.forBird = cursor.getInt(9)
                food.forFish = cursor.getInt(10)
                foodList.add(food)
            }
            while (cursor.moveToNext())
        }
        cursor.close()
    }
    //endregion

    //region Sleep Funcs
    fun insertSleep(newSleep: Sleep)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SLEEP_CONTENT, newSleep.content)
        values.put(SLEEP_PET_TYPE_ID, newSleep.petTypeId)
        db.insert(SLEEP_TABLE, null, values)
    }

    fun deleteSleepTable()
    {
        val db = this.writableDatabase
        db.delete(SLEEP_TABLE, null, null)
        db.close()
    }

    fun readSleepList(): MutableList<Sleep>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $SLEEP_TABLE"
        val cursor = db.rawQuery(query, null)
        val sleepList = mutableListOf<Sleep>()
        sleepCursorFirst(cursor, sleepList)
        db.close()

        return sleepList
    }

    fun readPetSleepList(petTypeId: Int): MutableList<Sleep>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $SLEEP_TABLE WHERE $SLEEP_PET_TYPE_ID = $petTypeId ORDER BY $SLEEP_ID"
        val cursor = db.rawQuery(query, null)
        val petSleepList = mutableListOf<Sleep>()
        sleepCursorFirst(cursor, petSleepList)
        db.close()

        return petSleepList
    }

    private fun sleepCursorFirst(cursor: Cursor, sleepList: MutableList<Sleep>)
    {
        if (cursor.moveToFirst())
        {
            do
            {
                val sleep = Sleep()
                sleep.id = cursor.getInt(0)
                sleep.content = cursor.getString(1)
                sleep.petTypeId = cursor.getInt(2)
                sleepList.add(sleep)
            }
            while (cursor.moveToNext())
        }
        cursor.close()
    }
    //endregion

    //region Play Funcs
    fun insertPlay(newPlay: Play)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PLAY_CONTENT, newPlay.content)
        values.put(PLAY_PET_TYPE_ID, newPlay.petTypeId)
        db.insert(PLAY_TABLE, null, values)
    }

    fun deletePlayTable()
    {
        val db = this.writableDatabase
        db.delete(PLAY_TABLE, null, null)
        db.close()
    }

    fun readPlayList(): MutableList<Play>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $PLAY_TABLE"
        val cursor = db.rawQuery(query, null)
        val playList = mutableListOf<Play>()
        playCursorFirst(cursor, playList)
        db.close()

        return playList
    }

    fun readPetPlayList(petTypeId: Int): MutableList<Play>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM $PLAY_TABLE WHERE $PLAY_PET_TYPE_ID = $petTypeId ORDER BY $PLAY_ID"
        val cursor = db.rawQuery(query, null)
        val petPlayList = mutableListOf<Play>()
        playCursorFirst(cursor, petPlayList)
        db.close()

        return petPlayList
    }

    private fun playCursorFirst(cursor: Cursor, playList: MutableList<Play>)
    {
        if (cursor.moveToFirst())
        {
            do
            {
                val play = Play()
                play.id = cursor.getInt(0)
                play.content = cursor.getString(1)
                play.petTypeId = cursor.getInt(2)
                playList.add(play)
            }
            while (cursor.moveToNext())
        }
        cursor.close()
    }
    //endregion
}

//PureBlack Software / Murat BIYIK