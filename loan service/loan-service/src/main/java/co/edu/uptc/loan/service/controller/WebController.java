package co.edu.uptc.loan.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador Web para servir la interfaz frontend
 * Este controlador maneja las rutas web que sirven archivos estáticos HTML
 */
@Controller
public class WebController {

    /**
     * Mapea la ruta raíz "/" para servir la página principal index.html
     * Spring Boot automáticamente servirá el archivo desde src/main/resources/static/
     */
    @GetMapping("/")
    public String home() {
        return "index.html";  // Retorna el archivo index.html desde static/
    }

    /**
     * Mapea rutas adicionales que podrían necesitar servir la página principal
     * útil para aplicaciones SPA (Single Page Application)
     */
    @GetMapping({"/loans", "/management", "/dashboard"})
    public String loanPages() {
        return "index.html";  // Redirige a la página principal
    }
}