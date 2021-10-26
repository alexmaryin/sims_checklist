package model

interface Database {
    fun getAll(): List<Item>
    fun getById(id: Long): Item
}