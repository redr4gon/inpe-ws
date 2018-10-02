package com.ws.inpe.model;

public class Token {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean validateToken(Object token) {
        if (token == null) return false;
        return this.token.equals(token.toString());
    }
}
