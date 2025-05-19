package com.example.whiskervet.servicio.correoelectronico;

import com.example.whiskervet.entity.Citas;
import com.example.whiskervet.entity.Tratamiento;

public interface EmailService {

    public void enviarCorreo(Tratamiento tratamiento);
    public void enviarCorreo(Citas cita);
}