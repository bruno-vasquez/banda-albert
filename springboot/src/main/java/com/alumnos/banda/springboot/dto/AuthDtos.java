package com.alumnos.banda.springboot.dto;

public class AuthDtos {
    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class LoginResponse {
        public String token;
        public String username;
        public String role;
        public Integer familiaId;
        public String familiaNombre;

        public LoginResponse(String token, String username, String role, Integer familiaId, String familiaNombre) {
            this.token = token;
            this.username = username;
            this.role = role;
            this.familiaId = familiaId;
            this.familiaNombre = familiaNombre;
        }
    }
}