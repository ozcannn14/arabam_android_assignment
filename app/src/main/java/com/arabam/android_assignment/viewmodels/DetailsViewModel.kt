package com.arabam.android_assignment.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.arabam.android_assignment.models.Advert
import com.arabam.android_assignment.models.AdvertDetails
import com.arabam.android_assignment.services.LocalDatabase
import com.arabam.android_assignment.services.retrofit.ApiService
import com.arabam.android_assignment.views.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : BaseViewModel(application) {
    private val apiService = ApiService()
    private val disposable = CompositeDisposable()


    val advert = MutableLiveData<AdvertDetails>()
    val advertError = MutableLiveData<Boolean>()
    val advertLoading = MutableLiveData<Boolean>()

    fun getDetails(id: Int?) {
        getDataFromAPI(id)
    }

    private fun getDataFromLocal(id: Int?): AdvertDetails? {
        launch {
            val dao = LocalDatabase(getApplication()).roomDao()
            val advertDetails = dao.getAdvertDetails(id ?: 0)
            advert.value = advertDetails
        }
        return advert.value
    }

    private fun getDataFromAPI(id: Int?) {
        advertLoading.value = true
        disposable.add(
            apiService.getAdvertDetails(id ?: 0).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AdvertDetails>() {
                    override fun onSuccess(t: AdvertDetails) {
                        advert.value = t
                        advertError.value = false
                        advertLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        advertError.value = true
                        advertLoading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }
}