package com.pureblacksoft.petidence.data

data class Food(var id: Int = 0,
                var name: String = "",
                var calories: Float = 0f,
                var fat: Float = 0f,
                var carbohydrate: Float = 0f,
                var protein: Float = 0f,
                var image: ByteArray = byteArrayOf(0),
                var forCat: Int = 0,
                var forDog: Int = 0,
                var forBird: Int = 0,
                var forFish: Int = 0)
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Food

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int
    {
        return image.contentHashCode()
    }
}

//PureBlack Software / Murat BIYIK