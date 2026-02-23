package com.alumnos.banda.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.alumnos.banda.springboot.models.*;
import com.alumnos.banda.springboot.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BandaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BandaApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository users, PasswordEncoder encoder, FamiliaRepository familias) {
        return args -> {
            if (!users.existsByUsername("admin")) {
                AppUser a = new AppUser();
                a.setUsername("admin");
                a.setPasswordHash(encoder.encode("admin123"));
                a.setRole(Role.ROLE_ADMIN);
                a.setFamiliaAsignada(null);
                users.save(a);
                System.out.println("✅ Admin creado: admin / admin123");
            }

            // Familias base (si no existen)
            String[] base = {"Trompetas","Bajos","Maderas","Liras","Tambores","Multitenores","Timbales","Bombos","Platillos","Otros"};
            for (String n : base) {
                familias.findByNombre(n).orElseGet(() -> familias.save(new FamiliaInstrumento(n)));
            }
        };
    }
}