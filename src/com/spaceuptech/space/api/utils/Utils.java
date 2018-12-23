package com.spaceuptech.space.api.utils;

import com.google.gson.GsonBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;

import java.util.HashMap;
import java.util.concurrent.Executors;

public class Utils {

    public interface ResponseListener {
        public void onResponse(int statusCode, Object data);
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

    public static HashMap<String, Object> createMap(String k, Object v) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(k, v);
        return map;
    }

    public static void fetch(AsyncHttpClient client, String method, String token, String url, String body, Utils.ResponseListener listener) {
        ListenableFuture<Response> req = getFuture(client, method, token, url, body);

        req.addListener(() -> {
            try {
                Response response = req.get();
                String b = response.getResponseBody();
                Object obj = null;
                if (b.length() > 0) obj = new GsonBuilder().create().fromJson(b, Object.class);
                listener.onResponse(response.getStatusCode(), obj);
            } catch (Exception e) {
                listener.onError(e);
            }
        }, Executors.newSingleThreadExecutor());
    }
}
