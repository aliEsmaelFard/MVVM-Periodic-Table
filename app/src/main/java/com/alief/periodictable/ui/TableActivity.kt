package com.alief.periodictable.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
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

class TableActivity : AppCompatActivity(), OnElementClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var myAdaptor: MyAdaptor
    lateinit var viewModel: ViewModel
    lateinit var progressBar: ProgressBar
    lateinit var searchEditText: EditText
    lateinit var toolbarTitle: TextView
    lateinit var toolbarSearchImage: ImageView
    lateinit var toolbarCloseSearch: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewFinder()

        val repository = Repository()
        val newsViewModelProviderFactory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, newsViewModelProviderFactory).get(ViewModel::class.java)

        progressBar = findViewById(R.id.paginationProgressBar)
        recyclerView = findViewById(R.id.recyclerview)
        searchEditText = findViewById(R.id.toolbar_text_view)

        getAllElements()

        searchEditText.addTextChangedListener { text ->
            text?.let {
                var search = text.toString().filter { !it.isWhitespace() }
                if (search.isNotEmpty()) {
                    viewModel.getSearchElements(search)
                }
            }
        }

        getSearchElements()

        myAdaptor = MyAdaptor(this)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = myAdaptor
        }

        //set up items toolbar visibility
        toolbarSearchImage.setOnClickListener{
            toolbarTitle.visibility = View.INVISIBLE
            toolbarSearchImage.visibility = View.INVISIBLE
            searchEditText.visibility = View.VISIBLE
            toolbarCloseSearch.visibility = View.VISIBLE
        }

        toolbarCloseSearch.setOnClickListener {
            getAllElements()
            toolbarTitle.visibility = View.VISIBLE
            toolbarSearchImage.visibility = View.VISIBLE
            searchEditText.visibility = View.INVISIBLE
            toolbarCloseSearch.visibility = View.INVISIBLE
            searchEditText.setText("")
        }
    }

    private fun getAllElements() {

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

    private fun getSearchElements() {

        viewModel.searchElements.observe(this, Observer { resources ->
            when (resources) {
                is Resource.Success -> {
                    hideProgressBar()
                    resources.data?.let { response ->
                        myAdaptor.differ.submitList(response.toList())
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()

                    var message = resources.message
                    if (message == "BAD REQUEST") {
                        message = "element is invalid"
                    }

                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }


    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun onClicked(element: Element) {
        TODO("Not yet implemented")
    }

    //find all Views
    fun viewFinder()
    {
        progressBar = findViewById(R.id.paginationProgressBar)
        recyclerView = findViewById(R.id.recyclerview)
        searchEditText = findViewById(R.id.toolbar_text_view)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarSearchImage = findViewById(R.id.toolbar_search_image)
        toolbarCloseSearch = findViewById(R.id.toolbar_close_image)
    }
}