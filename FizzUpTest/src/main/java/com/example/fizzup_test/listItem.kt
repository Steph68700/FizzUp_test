package com.example.fizzup_test

class listItem(var name: String, var score: String) {
    private var imageId = 0
    fun setImageId(imageId: Int) {
        this.imageId = imageId
    }

    override fun toString(): String {
        return name + " : " + score
    }

}