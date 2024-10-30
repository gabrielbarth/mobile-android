package com.gabrielbarth.myapi

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielbarth.myapi.adapter.ItemAdapter
import com.gabrielbarth.myapi.database.DatabaseBuilder
import com.gabrielbarth.myapi.database.model.UserLocation
import com.gabrielbarth.myapi.databinding.ActivityMainBinding
import com.gabrielbarth.myapi.model.Item
import com.gabrielbarth.myapi.service.Result
import com.gabrielbarth.myapi.service.RetrofitClient
import com.gabrielbarth.myapi.service.safeApiCall
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestLocationPermission()
        setupView()
    }

    override fun onResume() {
        super.onResume()
        fetchItems()
    }

    private fun requestLocationPermission() {
        // Inicializa o FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Configura o ActivityResultLauncher para solicitar a permissão de localização
        locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Permissão concedida, obter a localização
                    getLastLocation()
                } else {
                    Toast.makeText(this, R.string.denied_permission, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        checkLocationPermissionAndRequest()
    }

    private fun checkLocationPermissionAndRequest() {
        when {
            checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                    checkSelfPermission(
                        this,
                        ACCESS_COARSE_LOCATION
                    ) == PERMISSION_GRANTED -> {
                getLastLocation()
            }

            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {
                locationPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }

            shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) -> {
                locationPermissionLauncher.launch(ACCESS_COARSE_LOCATION)
            }

            else -> {
                locationPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getLastLocation() {
        if (checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED ||
            checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result
                val userLocation =
                    UserLocation(latitude = location.latitude, longitude = location.longitude)
                Log.d(
                    "HELLO_WORLD",
                    "Lat: ${userLocation.latitude} Long: ${userLocation.longitude}"
                )
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseBuilder.getInstance()
                        .userLocationDao()
                        .insert(userLocation)
                }
            } else {
                Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchItems() {
        // change execution to thread IO
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall {
                RetrofitClient.apiService.getItems()
            }

            // change back execution to main thread
            withContext(Dispatchers.Main) {
                binding.swipeRefreshLayout.isRefreshing = false
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
        val adapter = ItemAdapter(data) {
            startActivity(
                ItemDetailsActivity.newIntent(
                    this,
                    it.id
                )
            )
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            fetchItems()
        }
        binding.addCta.setOnClickListener {
            startActivity(NewItemActivity.newIntent(this))
        }
    }
}