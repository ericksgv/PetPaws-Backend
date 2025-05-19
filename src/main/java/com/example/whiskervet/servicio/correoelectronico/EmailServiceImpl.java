package com.example.whiskervet.servicio.correoelectronico;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.whiskervet.entity.Citas;
import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Medicamento;
import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.servicio.mascota.MascotaService;
import com.example.whiskervet.servicio.usuario.UsuarioService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

// ...

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void enviarCorreo(Tratamiento tratamiento) {
        MimeMessage mensaje = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, "utf-8");

        try {
            Usuario cliente = tratamiento.getMascota().getDuenio();
            Mascota mascota = tratamiento.getMascota();
            Veterinario veterinario = tratamiento.getVeterinario();
            Medicamento medicamento = tratamiento.getMedicamento();

            String destinatario = tratamiento.getMascota().getDuenio().getCorreo();
            String nombreMascota = mascota.getNombre();
            String descripcionTratamiento = tratamiento.getDescripcion();
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/ MM/ yyyy");
            String fechaTratamiento = tratamiento.getFecha().format(formatoFecha);
            String nombreVeterinario = veterinario.getNombre();
            String especialidad = veterinario.getEspecialidad();
            String nombreMedicamento = medicamento.getNombre();
            String costoMedicamento = String.valueOf(medicamento.getPrecioVenta());
            String nombreDuenoMascota = cliente.getNombre();
            String centroVeterinario = "PetPaws";
            String correoContacto = "vetpetpaws@gmail.com";

            String asunto = "Informe de Tratamiento Realizado para " + nombreMascota + " - " + fechaTratamiento;

            // Cargar el contenido de la plantilla desde el archivo
            String contenidoPlantilla = cargarPlantillaDesdeRecurso("correo_template.html");

            // Reemplazar los marcadores de posición con valores reales
            contenidoPlantilla = contenidoPlantilla.replace("%nombreDuenoMascota%", nombreDuenoMascota);
            contenidoPlantilla = contenidoPlantilla.replace("%nombreMascota%", nombreMascota);
            contenidoPlantilla = contenidoPlantilla.replace("%descripcionTratamiento%", descripcionTratamiento);
            contenidoPlantilla = contenidoPlantilla.replace("%fechaTratamiento%", fechaTratamiento);
            contenidoPlantilla = contenidoPlantilla.replace("%nombreVeterinario%", nombreVeterinario);
            contenidoPlantilla = contenidoPlantilla.replace("%especialidad%", especialidad);
            contenidoPlantilla = contenidoPlantilla.replace("%nombreMedicamento%", nombreMedicamento);
            contenidoPlantilla = contenidoPlantilla.replace("%costoMedicamento%", costoMedicamento);
            contenidoPlantilla = contenidoPlantilla.replace("%centroVeterinario%", centroVeterinario);
            contenidoPlantilla = contenidoPlantilla.replace("%correoContacto%", correoContacto);

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoPlantilla, true); // Establecer el segundo parámetro en true para permitir HTML en el correo

            emailSender.send(mensaje);
        } catch (MessagingException e) {
            // Manejar la excepción en caso de un error de envío de correo
            e.printStackTrace();
        }
    }

    private String cargarPlantillaDesdeRecurso(String nombreRecurso) {
        try {
            ClassPathResource resource = new ClassPathResource(nombreRecurso);
            byte[] contenido = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(contenido, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Manejar excepciones si el recurso no se encuentra o no se puede cargar
            e.printStackTrace();
            return ""; // O devolver una cadena vacía en caso de error
        }
    }

@Override
public void enviarCorreo(Citas cita) {
    MimeMessage mensaje = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mensaje, "utf-8");

    try {
        
        Usuario cliente = mascotaService.obtenerMascotaPorId(cita.getId()).getDuenio();
        String destinatario = cliente.getCorreo();
        String nombreMascota = mascotaService.obtenerMascotaPorId(cita.getId()).getNombre(); // Implementa este método según tu lógica de negocio
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaCita = cita.getFechaHora().toInstant().atZone(ZoneId.systemDefault()).format(formatoFecha);
        String nombreDuenoMascota = cliente.getNombre();
        String centroVeterinario = "PetPaws";
        String correoContacto = "vetpetpaws@gmail.com";

        String asunto = "Confirmación de Cita para " + nombreMascota + " - " + fechaCita;

        // Cargar el contenido de la plantilla desde el archivo
        String contenidoPlantilla = cargarPlantillaDesdeRecurso("confirmacion_cita_template.html");

        // Reemplazar los marcadores de posición con valores reales
        contenidoPlantilla = contenidoPlantilla.replace("%nombreDuenoMascota%", nombreDuenoMascota);
        contenidoPlantilla = contenidoPlantilla.replace("%nombreMascota%", nombreMascota);
        contenidoPlantilla = contenidoPlantilla.replace("%fechaCita%", fechaCita);
        contenidoPlantilla = contenidoPlantilla.replace("%centroVeterinario%", centroVeterinario);
        contenidoPlantilla = contenidoPlantilla.replace("%correoContacto%", correoContacto);

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(contenidoPlantilla, true); // Establecer el segundo parámetro en true para permitir HTML en el correo

        emailSender.send(mensaje);
    } catch (MessagingException e) {
        // Manejar la excepción en caso de un error de envío de correo
        e.printStackTrace();
    }
}

}
