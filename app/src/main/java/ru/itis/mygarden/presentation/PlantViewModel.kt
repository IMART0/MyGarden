package ru.itis.mygarden.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.itis.mygarden.data.Plant
import ru.itis.mygarden.data.PlantDao
import ru.itis.mygarden.data.PlantDatabase
import ru.itis.mygarden.data.User
import ru.itis.mygarden.data.UserDao
import java.lang.ref.WeakReference

class PlantViewModel(context: Context) : ViewModel() {
    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val plantDao: PlantDao = contextRef.get()?.let {
        PlantDatabase.getDataBase(it).plantDao()
    } ?: throw IllegalStateException("Context is null")

    private val userDao : UserDao = contextRef.get()?.let {
        PlantDatabase.getDataBase(it).userDao()
    } ?: throw IllegalStateException("Context is null")

    private val _userStateFlow = MutableStateFlow<User?>(null)
    val userStateFlow : StateFlow<User?>
        get() = _userStateFlow

    fun getUser() {
        viewModelScope.launch {
            _userStateFlow.emit(userDao.getUser(1))
        }
    }

    fun updateUser(name : String, imagePath : String) {
        viewModelScope.launch {
            println("kek3")
            userStateFlow.value?.let {
                println("kek is not null")
                return@launch userDao.updateUser(it.copy(name = name, imagePath = imagePath))
            }
            println("it is null")
            userDao.updateUser(User(
                id = 1,
                name = name,
                imagePath = imagePath
            ))
        }
    }

    suspend fun getAllPlants(): List<Plant> {
        return withContext(Dispatchers.IO) {
            plantDao.getAllPlants()
        }
    }

    fun addPlant(plant: Plant) {
        viewModelScope.launch(Dispatchers.IO) {
            plantDao.insertPlant(plant)
        }
    }

    fun updatePlant(plant: Plant) {
        viewModelScope.launch(Dispatchers.IO) {
            plantDao.updatePlant(plant)
        }
    }

    fun deletePlant(plant: Plant) {
        viewModelScope.launch(Dispatchers.IO) {
            plantDao.deletePlant(plant)
        }
    }
}
