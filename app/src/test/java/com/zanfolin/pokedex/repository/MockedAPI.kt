package com.zanfolin.pokedex.repository

import com.zanfolin.pokedex.base.service.API
import com.zanfolin.pokedex.base.service.APIConfiguration
import okhttp3.mockwebserver.MockWebServer

class MockedAPI(config: APIConfiguration) : API(config) {

    override fun getHost() = MockWebServer().url("/")

}