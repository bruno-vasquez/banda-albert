package com.alumnos.banda.springboot.dto;

public class UserCreateDto {
    public String username;
    public String password;
    public String role; // "ADMIN" o "ENCARGADO"
    public Integer familiaId; // requerido si ENCARGADO
}