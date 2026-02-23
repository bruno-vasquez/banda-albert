package com.alumnos.banda.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    // Sirve el index.html para rutas del front (React Router)
    @RequestMapping(value = { "/", "/{path:[^\\.]*}", "/**/{path:[^\\.]*}" })
    public String forward() {
        return "forward:/index.html";
    }
}