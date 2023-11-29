package presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.DogModel

class DogsViewModel : ViewModel() {

    private val _UIStateFlow = MutableStateFlow<DogImageState>(DogImageState())
    val UIStateFlow = _UIStateFlow.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    init {
        updateImages()
    }

    override fun onCleared() {
        httpClient.close()
    }

    fun updateImages(){
        viewModelScope.launch {
            val images = getImages()
            _UIStateFlow.update {
                it.copy(images = images.message)
            }
        }
    }

    private suspend fun getImages(): DogModel {
        val images = httpClient
            .get("https://dog.ceo/api/breeds/image/random/50")
            .body<DogModel>()
        return images
    }


}