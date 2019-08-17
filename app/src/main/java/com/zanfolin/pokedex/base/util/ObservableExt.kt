package com.zanfolin.pokedex.base.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

fun <T> Observable<Response<T>>.takeResponseContents(): Observable<T> = this
    .map { r -> r.body() }

fun <T> Observable<T>.takeNonNulls(): Observable<T> = this
    .filter { next -> next != null }
    .map { next -> next!! }

fun <T> Observable<T>.sendToMainThread(): Observable<T> = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<Response<T>>.takeBodyAndSendToMainThread(): Observable<T> = this
    .takeResponseContents()
    .takeNonNulls()
    .sendToMainThread()