package com.example.whiskervet.controller.errorcontroller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(Model model, CustomException exception) {
        String errorCode = exception.getErrorCode();
        Object errorDetails = exception.getErrorDetails();

        if ("CatNotFound".equals(errorCode)) {
            model.addAttribute("id", errorDetails);
            return "mascotas/pagina_error_mascota";
        } else if ("UsuarioNotFound".equals(errorCode)) {
            model.addAttribute("cedula", errorDetails);
            return "usuario/pagina_error_usuario";
        } else if ("VeterinarioNotFound".equals(errorCode)) {
            model.addAttribute("cedula", errorDetails);
            return "veterinario/pagina_error_veterinario";
        } else {
            return "pagina_error_generico";
        }
    }
}

