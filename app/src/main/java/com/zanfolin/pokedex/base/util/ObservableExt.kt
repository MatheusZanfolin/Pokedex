package com.zanfolin.pokedex.base.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

fun <T> Single<Response<T>>.takeResponseContents(): Single<T> = this
    .map { r -> r.body() }

fun <T> Single<T>.sendToMainThread(): Single<T> = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<Response<T>>.takeBodyAndSendToMainThread(): Single<T> = this
    .takeResponseContents()
    .sendToMainThread()