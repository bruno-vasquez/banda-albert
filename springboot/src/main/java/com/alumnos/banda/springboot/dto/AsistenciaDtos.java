package com.alumnos.banda.springboot.dto;

import java.time.LocalDate;
import java.util.List;

public class AsistenciaDtos {
    public static class Item {
        public Integer alumnoId;
        public boolean asistio;
    }

    public static class BulkRequest {
        public LocalDate fecha;
        public List<Item> items;
    }
}