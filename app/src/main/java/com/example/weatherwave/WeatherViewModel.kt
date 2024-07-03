package com.example.weatherwave

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherwave.api.NetworkResponse
import com.example.weatherwave.api.RetrofitInstance
import com.example.weatherwave.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel(): ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherData = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherData: MutableLiveData<NetworkResponse<WeatherModel>> = _weatherData

    fun getData(city: String) {
        viewModelScope.launch {
            _weatherData.value = NetworkResponse.Loading
            try {
                val response = weatherApi.getCurrentWeather(Constants.API_KEY, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherData.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherData.value = NetworkResponse.Error("Failed to load the data")
                }
            } catch (e: Exception) {
                _weatherData.value = NetworkResponse.Error("Failed to load the data")
            }
        }
    }
}