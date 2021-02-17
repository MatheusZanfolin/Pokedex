package com.zanfolin.pokedex.repository.base;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.zanfolin.pokedex.base.util.FileUtils.readJsonForEndpoint;

public class BaseRepositoryTest {

    protected MockWebServer mockServer = new MockWebServer();

    @Before
    public void setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                return Schedulers.trampoline();
            }
        });
    }

    @After
    public void teardown() throws IOException {
        mockServer.shutdown();
    }

    protected void mock(String endpoint, int code) {
        mock(endpoint, code, Mock.CODE_AND_BODY);
    }

    protected void mock(String endpoint, int code, Mock type) {
        MockResponse respsonse = new MockResponse().setResponseCode(code);

        if (type == Mock.CODE_AND_BODY) {
            respsonse.setBody(readJsonForEndpoint(endpoint));
        }

        mockServer.enqueue(respsonse);
    }

}
