package model

class DatabaseImpl : Database {

    private val _fakeData = List(10) {
        Item(it.toLong() + 1, "item number ${it + 1}")
    }

    override fun getAll(): List<Item> = _fakeData

    override fun getById(id: Long): Item = _fakeData[id.toInt() - 1]
}