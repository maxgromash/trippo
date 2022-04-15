package edu.project.trippo.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.project.trippo.di.DaggerDaggerComponent
import edu.project.trippo.model.DestinationVO
import edu.project.trippo.model.Preferences
import edu.project.trippo.model.Quiz
import edu.project.trippo.model.SortType
import edu.project.trippo.repository.MainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var repository: MainRepository
    private val _originData = MutableLiveData<List<DestinationVO>>()
    private val _filteredData = MutableLiveData<List<DestinationVO>>()
    private val _loadingStatus = MutableLiveData<Boolean>()
    var currentQuiz = MutableLiveData<Quiz?>()
    private var questions: List<Quiz>
    lateinit var location: Location
    var sortType = SortType.CONS
    var preferences = Preferences()

    fun loadingStatus(): LiveData<Boolean> = _loadingStatus
    fun filteredData(): LiveData<List<DestinationVO>> = _filteredData

    init {
        DaggerDaggerComponent.create().inject(this)
        questions = repository.getQuestions()
        currentQuiz.value = questions[0]
    }

    fun getFilteredData(query: String) {
        viewModelScope.launch {
            delay(230)
            val lowerQuery = query.lowercase().trim()
            _filteredData.value = _originData.value?.filter {
                it.city.lowercase().startsWith(lowerQuery) or it.country.lowercase().startsWith(lowerQuery)
            }?.sort()
        }
    }

    fun getPrefs() {
        preferences = repository.getSavedPrefs()
        _originData.value = repository.getViaPrefsData(preferences)
        _filteredData.value = _originData.value?.sort()
    }

    fun saveStarred() {
        repository.saveStarred(_originData.value?.filter { it.isStared }!!)
    }

    fun getStarred() {
        val x = repository.getStarred()
        for (i in x) {
            _originData.value?.first { it.id == i }?.isStared = true
        }
    }

    fun getStarredMonths() = _originData.value?.filter { it.isStared }

    private fun List<DestinationVO>.sort(): List<DestinationVO> {
        return when (sortType) {
            SortType.NAME -> this.sortedBy { it.city }
            SortType.TEMPERATURE -> this.sortedByDescending { it.averageTemp }
            SortType.CONS -> this.sortedByDescending {
                it.conformity?.conformityCount
            }
        }
    }

    fun sortBy(key: String) {
        sortType = SortType.values().first { it.key == key }
        _filteredData.value = _filteredData.value?.sort()
    }

    fun changeMonth(month: Int) {
        viewModelScope.launch {
            _loadingStatus.value = true
            delay(1500)
            preferences.month = month
            repository.savePrefs(preferences)
            _originData.value = repository.getViaPrefsData(preferences)
            getStarred()
            _filteredData.value = _originData.value?.sort()
            _loadingStatus.value = false
        }
    }

    fun setPreference(ind: Int) {
        if (ind == -1) {
            currentQuiz.value = null
            return
        }
        val currentQuizNum = currentQuiz.value?.quizNum
        when (currentQuizNum) {
            0 -> preferences.temperature = when (ind) {
                0 -> Pair(20.0, 40.0)
                1 -> Pair(10.0, 19.0)
                2 -> Pair(0.0, 9.0)
                else -> Pair(-40.0, 0.0)
            }
            1 -> preferences.hotelBudget = when (ind) {
                0 -> 20
                1 -> 50
                2 -> 100
                else -> 200
            }
            2 -> preferences.isPopular = ind == 0
            3 -> preferences.isSeaExist = ind == 0
        }

        currentQuizNum?.let {
            if (questions.size > currentQuizNum + 1)
                currentQuiz.value = questions[currentQuizNum + 1]
            else {
                currentQuiz.value = null
                repository.savePrefs(preferences)
                _originData.value = repository.getViaPrefsData(preferences)
                _filteredData.value = _originData.value
            }
        }

    }
}