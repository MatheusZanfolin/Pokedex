package com.zanfolin.pokedex.base.model;

import java.lang.reflect.ParameterizedType;

import retrofit2.Retrofit;

public abstract class Repository<E> {

    protected final E endpoint;

    public Repository(Retrofit service) {
        endpoint = makeEndpoint(service);
    }

    private E makeEndpoint(Retrofit service) {
        final ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class<E> endpoint = (Class<E>) type.getActualTypeArguments()[0];
        return service.create(endpoint);
    }

}
