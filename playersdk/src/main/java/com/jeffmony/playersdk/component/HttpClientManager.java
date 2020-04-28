package com.jeffmony.playersdk.component;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class HttpClientManager {

  private static HttpClientManager sInstance;
  private OkHttpClient mClient;

  public static HttpClientManager getInstance() {
    if (sInstance == null) {
      synchronized (HttpClientManager.class) {
        if (sInstance == null) {
          sInstance = new HttpClientManager();
        }
      }
    }
    return sInstance;
  }

  private HttpClientManager() {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    clientBuilder.connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES));
    mClient = clientBuilder.build();
  }

  public OkHttpClient getClient() {
    return mClient;
  }
}
