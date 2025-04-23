package com.example.demoweb.model;

public record Result<V>(V data, int code, Throwable exception, String... messages) {
    public Result(V data, int code, Throwable exception) {
        this(data, code, exception, new String[0]);
    }

    public Result(V data, int code) {
        this(data, code, null);
    }

    public Result(V data) {
        this(data, 0);
    }

    public Result() {
        this(null);
    }  
}