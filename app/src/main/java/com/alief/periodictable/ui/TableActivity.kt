package com.alief.periodictable.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alief.periodictable.R
import com.alief.periodictable.adaptor.MyAdaptor
import com.alief.periodictable.adaptor.OnElementClickListener
import com.alief.periodictable.model.Element
import com.alief.periodictable.repository.Repository
import com.alief.periodictable.util.Resource

class TableActivity: AppCompatActivity(), OnElementClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var myAdaptor: MyAdaptor
    lateinit var viewModel: ViewModel
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val newsViewModelProviderFactory = ViewModelProviderFactory( application, repository)
        viewModel = ViewModelProvider(this, newsViewModelProviderFactory).get(ViewModel::class.java)

        progressBar = findViewById(R.id.paginationProgressBar)
        recyclerView = findViewById(R.id.recyclerview)
        getAllElements()

        myAdaptor = MyAdaptor(this)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = myAdaptor
        }
    }

    fun getAllElements()
    {

        viewModel.allElements.observe(this, Observer { resources ->
            when (resources) {
                is Resource.Success -> {
                    hideProgressBar()
                    resources.data?.let { response ->
                        myAdaptor.differ.submitList(response.toList())
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(this, resources.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }
    fun showProgressBar()
    {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar()
    {
        progressBar.visibility = View.INVISIBLE
    }

    override fun onClicked(element: Element) {
        TODO("Not yet implemented")
    }

}