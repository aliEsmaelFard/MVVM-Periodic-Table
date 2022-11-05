package com.alief.periodictable.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alief.periodictable.model.ElementItem
import com.alief.periodictable.repository.Repository
import com.alief.periodictable.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ViewModel(
    app: Application,
    val myRepository: Repository
) : AndroidViewModel(app) {

    val allElements: MutableLiveData<Resource<ElementItem>> = MutableLiveData()

    val searchElements: MutableLiveData<Resource<ElementItem>> = MutableLiveData()

    val stateElements: MutableLiveData<Resource<ElementItem>> = MutableLiveData()

    init {
        getElements()
    }

    private fun getElements() = viewModelScope.launch {
       safeAllElements()
    }

    private fun handelAllElement(response: Response<ElementItem>): Resource<ElementItem> {

        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(response.body())
            }
        }
        return Resource.Error(response.message())
    }

    suspend fun safeAllElements(){
        allElements.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = myRepository.getElements()
                allElements.postValue(handelAllElement(response))
            }
            else{
                allElements.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when(t) {
                is IOException -> allElements.postValue(Resource.Error("Network Failure"))
                else -> allElements.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    fun getSearchElements(symbol: String) = viewModelScope.launch {
        safeSearchElement(symbol)
    }

    private suspend fun safeSearchElement(symbol: String) {
        searchElements.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()){
                val response = myRepository.getSearchElements(symbol)
                searchElements.postValue(handelAllElement(response))
            }
            else{
                searchElements.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchElements.postValue(Resource.Error("Network Failure"))
                else -> searchElements.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    fun getElementsByState(state: String) = viewModelScope.launch {
        safeStateElement(state)
    }

    private suspend fun safeStateElement(state: String) {

        stateElements.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()){
                val response = myRepository.getElementByState(state)
                stateElements.postValue(handelAllElement(response))
            }
            else{
                stateElements.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when(t) {
                is IOException -> stateElements.postValue(Resource.Error("Network Failure"))
                else -> stateElements.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<com.alief.periodictable.application.Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}