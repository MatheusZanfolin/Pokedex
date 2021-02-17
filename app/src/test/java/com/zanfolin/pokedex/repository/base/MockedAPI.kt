package com.zanfolin.pokedex.repository.base

import com.zanfolin.pokedex.base.model.API
import com.zanfolin.pokedex.base.model.APIConfiguration
import okhttp3.mockwebserver.MockWebServer

class MockedAPI(config: APIConfiguration, private val mockServer: MockWebServer) : API(config) {

    override fun getHost() = mockServer.url("/")

}