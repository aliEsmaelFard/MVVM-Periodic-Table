package com.alief.periodictable.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alief.periodictable.repository.Repository

class ViewModel(
    app: Application,
    val myRepository: Repository
): AndroidViewModel(app) {


}