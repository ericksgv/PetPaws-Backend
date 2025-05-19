package com.example.whiskervet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landingPage")
public class LandingPageController {

    @GetMapping("/1")
    public String landingPage(){
        return "landingpage/LandingPage";
    }
     @GetMapping("/2")
    public String landingPage2(){
        return "landingpage/barra_navegacion";
    }
    @GetMapping("/3")
    public String landingPage3(){
        return "tratamiento/lista_tratamiento";
    }
     @GetMapping("/4")
    public String landingPage4(){
        return "administrador/pagina_inicio_administrador";
    }
}
