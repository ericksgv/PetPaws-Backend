package com.example.whiskervet.entity;

import org.springframework.stereotype.Component;

@Component
public class VeterinarioCounter {
    private long contador = 0;

    public synchronized long getNextId() {
        contador++;
        return contador;
    }

    public long getCurrentId() {
        return contador;
    }

    public synchronized long getlastId() {
        contador--;
        return contador;
    }
}
