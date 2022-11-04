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

    init {
        getElements()
    }
    private fun getElements() = viewModelScope.launch {
        val response = myRepository.getElements()
        handelAllElement(response)
    }

    private fun handelAllElement(response: Response<ElementItem>) {
        allElements.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                if (response.isSuccessful) {
                    allElements.postValue(Resource.Success(response.body()))
                } else {
                    allElements.postValue(Resource.Error(response.message()))
                }
            } else {
                allElements.postValue(Resource.Error("No Internet"))
            }

        }catch (t: Throwable) {
            when(t) {
                is IOException -> allElements.postValue(Resource.Error("Network Failure"))
                else -> allElements.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private fun getSearchElements(symbol: String) = viewModelScope.launch {
        val response = myRepository.getSearchElements(symbol)
        handelSearchElement(response)
    }

    private fun handelSearchElement(response: Response<ElementItem>) {
        searchElements.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                if (response.isSuccessful) {
                    searchElements.postValue(Resource.Success(response.body()))
                } else {
                    searchElements.postValue(Resource.Error(response.message()))
                }
            } else {
                searchElements.postValue(Resource.Error("No Internet"))
            }

        }catch (t: Throwable) {
            when(t) {
                is IOException -> searchElements.postValue(Resource.Error("Network Failure"))
                else -> searchElements.postValue(Resource.Error("Conversion Error"))
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