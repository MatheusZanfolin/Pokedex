package com.zanfolin.pokedex.base.model;

import java.lang.reflect.ParameterizedType;

public abstract class Repository<E> {

    protected final E endpoint;

    public Repository(API api) {
        endpoint = makeEndpoint(api);
    }

    private E makeEndpoint(API api) {
        final ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class<E> endpoint = (Class<E>) type.getActualTypeArguments()[0];

        return api
                .getService()
                .create(endpoint);
    }

}