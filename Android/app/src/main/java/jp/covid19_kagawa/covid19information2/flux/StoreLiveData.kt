package jp.covid19_kagawa.covid19information2.flux

import androidx.lifecycle.LiveData


// https://satoshun.github.io/2018/12/livedata_helper/

class StoreLiveData<T> : LiveData<T>() {
    internal fun internalPostValue(value: T) {
        postValue(value)
    }

    internal fun internalSetValue(v: T) {
        value = v
    }
}

