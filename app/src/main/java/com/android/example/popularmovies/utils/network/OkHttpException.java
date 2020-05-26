package com.android.example.popularmovies.utils.network;

import androidx.annotation.Nullable;

import okhttp3.Response;

public class OkHttpException extends RuntimeException {
    private Response response;

    @Nullable
    @Override
    public String getMessage() {
        switch (response.code()){
            case 400:
                return "Invalid request syntax";
            case 401:
                return "Not authorized to access this information";
            case 404:
                return "The server could not be found";
            default:
                return super.getMessage();
        }

    }

    public OkHttpException(Response response) {
        this.response = response;
    }
}
