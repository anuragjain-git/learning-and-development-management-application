package com.vilt.narmada.dto;

public class JwtResponse {

    private String token;
    private String message;
    public JwtResponse() {
        super();
        // TODO Auto-generated constructor stub
    }
    public JwtResponse(String message,String token) {
        super();
        this.token = token;
        this.message = message;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
