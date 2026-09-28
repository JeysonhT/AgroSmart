package com.example.agrosmart.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrosmart.R
import com.example.agrosmart.core.utils.interfaces.IHomeViewModel
import com.example.agrosmart.domain.designModels.CropCarouselData
import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.usecase.CropsUseCase
import com.example.agrosmart.presentation.viewmodels.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cropsUseCase: CropsUseCase
) : ViewModel(), IHomeViewModel {

    private val tag = "HOME_VIEWMODEL"

    // Modern StateFlow UI State (UDF)
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Backward-compatible LiveData
    private val _cropsData = MutableLiveData<List<CropCarouselData>>()
    override val crops: LiveData<List<CropCarouselData>> get() = _cropsData


    override fun loadCrops() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val list = cropsUseCase.getCrops()
                if (list.isNotEmpty()) {
                    val data = list.map { createCropInfo(it) }
                    _cropsData.postValue(data)
                    _uiState.value = HomeUiState.Success(data)
                    Log.println(Log.ASSERT, tag, "Datos cargados exitosamente")
                } else {
                    val placeholderList = listOf(
                        CropCarouselData(
                            R.drawable.no_internet_placeholder,
                            "No hay conexión a internet",
                            "",
                            "",
                            ""
                        )
                    )
                    _cropsData.postValue(placeholderList)
                    _uiState.value = HomeUiState.Success(placeholderList)
                }
            } catch (e: Exception) {
                Log.e(tag, "Error al cargar los cultivos: ${e.message}", e)
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Error al cargar los cultivos")
            }
        }
    }

    fun createCropInfo(c: Crop): CropCarouselData {
        val name = c.cropName
        return CropCarouselData(
            getCropImage(name),
            name,
            c.description,
            c.harvestTime,
            c.type
        )
    }

    private fun getCropImage(cropName: String): Int {
        return when (cropName.lowercase()) {
            "maiz" -> R.drawable.imagen_1
            "frijol" -> R.drawable.sorgo
            else -> R.drawable.frijol
        }
    }
}
