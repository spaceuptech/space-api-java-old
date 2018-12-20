package com.spaceuptech.space.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class Utils {

    public class ResponseStub {
        public Object result;
    }

    public interface ResponseListener {
        public void onResponse(int statusCode, ResponseStub data);
        public void onError(Exception e);
    }

    private static ListenableFuture<Response> getFuture(AsyncHttpClient client, String method, String token, String url, String body) {
        BoundRequestBuilder builder;

        switch (method) {
            case "get":
                builder = client.prepareGet(url);
                break;

            case "post":
                builder = client.preparePost(url).setBody(body);
                break;

            case "delete":
                builder = client.prepareDelete(url).setBody(body);
                break;

            case "put":
                builder = client.preparePut(url).setBody(body);
                break;

            default:
                return null;

        }

        return builder
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .execute();
    }

    public static void fetch(AsyncHttpClient client, String method, String token, String url, String body, Utils.ResponseListener listener) {
        ListenableFuture<Response> req = getFuture(client, method, token, url, body);

        req.addListener(() -> {
            try {
                Response response = req.get();
                String b = response.getResponseBody();
                ResponseStub obj = null;
                if (b.length() > 0) obj = new GsonBuilder().create().fromJson(b, ResponseStub.class);
                listener.onResponse(response.getStatusCode(), obj);
            } catch (Exception e) {
                listener.onError(e);
            }
        }, Executors.newSingleThreadExecutor());
    }
}
