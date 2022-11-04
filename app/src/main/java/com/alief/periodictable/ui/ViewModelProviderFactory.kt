package com.alief.periodictable.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.alief.periodictable.repository.Repository
import com.alief.periodictable.ui.ViewModel

class ViewModelProviderFactory(
    val app: Application,
    val repository: Repository
): ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T
    {
        return ViewModel(app, repository) as T
    }
}