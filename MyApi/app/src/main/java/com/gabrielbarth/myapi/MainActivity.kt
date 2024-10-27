package com.gabrielbarth.myapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielbarth.myapi.adapter.ItemAdapter
import com.gabrielbarth.myapi.databinding.ActivityMainBinding
import com.gabrielbarth.myapi.model.Item
import com.gabrielbarth.myapi.service.Result
import com.gabrielbarth.myapi.service.RetrofitClient
import com.gabrielbarth.myapi.service.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        fetchItems()
    }

    private fun fetchItems() {
        // change execution to thread IO
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall {
                RetrofitClient.apiService.getItems()
            }

            // change back execution to main thread
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> {
                        handleOnSucess(result.data)
                    }
                }
            }
        }
    }

    private fun handleOnSucess(data: List<Item>) {
        val adapter = ItemAdapter(data)
        binding.recyclerView.adapter = adapter
    }

    private fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}