package com.example.nagwatask.data.mian

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.nagwatask.di.module.NetworkModule
import com.example.nagwatask.models.ApiState
import com.example.nagwatask.models.ErrorState
import com.example.nagwatask.models.LoadingState
import com.example.nagwatask.models.SuccessState
import com.example.nagwatask.models.file.FileMapper
import com.example.nagwatask.models.file.FileResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetFileRepository @Inject constructor(private val networkModel: NetworkModule) {

    private val mediatorResponse: MediatorLiveData<ApiState<List<FileMapper>>> = MediatorLiveData()
    private val fakeJson: String ="[\n" +
            "  {\n" +
            "    \"id\": 1,\n" +
            "    \"type\": \"VIDEO\",\n" +
            "    \"url\": \"https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4\",\n" +
            "    \"name\": \"Video 1\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 2,\n" +
            "    \"type\": \"VIDEO\",\n" +
            "    \"url\": \"https://bestvpn.org/html5demos/assets/dizzy.mp4\",\n" +
            "    \"name\": \"Video 2\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 3,\n" +
            "    \"type\": \"PDF\",\n" +
            "    \"url\": \"(https://kotlinlang.org/docs/kotlin-reference.pdf\",\n" +
            "    \"name\": \"PDF 3\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 4,\n" +
            "    \"type\": \"VIDEO\",\n" +
            "    \"url\": \"https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4\",\n" +
            "    \"name\": \"Video 4\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 5,\n" +
            "    \"type\": \"PDF\",\n" +
            "    \"url\": \"https://www.cs.cmu.edu/afs/cs.cmu.edu/user/gchen/www/download/java/LearnJava.pdf\",\n" +
            "    \"name\": \"PDF 5\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 6,\n" +
            "    \"type\": \"VIDEO\",\n" +
            "    \"url\": \"https://storage.googleapis.com/exoplayer-test-media-1/mp4/android-screens-10s.mp4\",\n" +
            "    \"name\": \"Video 6\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 7,\n" +
            "    \"type\": \"VIDEO\",\n" +
            "    \"url\": \"https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4\",\n" +
            "    \"name\": \"Video 7\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 8,\n" +
            "    \"type\": \"VIDEO\",\n" +
            "    \"url\": \"https://storage.googleapis.com/exoplayer-test-media-1/mp4/android-screens-25s.mp4\",\n" +
            "    \"name\": \"Video 8\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 9,\n" +
            "    \"type\": \"PDF\",\n" +
            "    \"url\": \"https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf\",\n" +
            "    \"name\": \"PDF 9\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 10,\n" +
            "    \"type\": \"PDF\",\n" +
            "    \"url\": \"https://en.unesco.org/inclusivepolicylab/sites/default/files/dummy-pdf_2.pdf\",\n" +
            "    \"name\": \"PDF 10\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 11,\n" +
            "    \"type\": \"VIDEO\",\n" +
            "    \"url\": \"https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4\",\n" +
            "    \"name\": \"Video 11\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 12,\n" +
            "    \"type\": \"VIDEO\",\n" +
            "    \"url\": \"https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4\",\n" +
            "    \"name\": \"Video 12\"\n" +
            "  }\n" +
            "]"
    fun getFile(): MediatorLiveData<ApiState<List<FileMapper>>> {
        mediatorResponse.value = LoadingState()
        val source: LiveData<ApiState<List<FileMapper>>> = LiveDataReactiveStreams.fromPublisher(
            networkModel.provideServiceInterface()
                .getAllFiles().map { handleResponse(it) }.onErrorReturn { handleError(it) }
                .subscribeOn(Schedulers.io())
        )
        mediatorResponse.addSource(source, Observer {
            mediatorResponse.value = it
            mediatorResponse.removeSource(source)
        })
        return mediatorResponse
    }

    private fun handleError(it: Throwable): ApiState<List<FileMapper>> {
        return ErrorState("${it.localizedMessage}\nSwipe to refresh")
    }

    private fun handleResponse(response: List<FileResponse>): ApiState<List<FileMapper>> {
        val responseList = mutableListOf<FileMapper>()
        response.forEach {
            responseList.add(FileMapper(it))
        }
        return SuccessState("", responseList)
    }
}


