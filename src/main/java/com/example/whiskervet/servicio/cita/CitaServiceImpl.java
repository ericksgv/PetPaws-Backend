package com.example.whiskervet.servicio.cita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.whiskervet.dto.CitasDTO;
import com.example.whiskervet.dto.CitasMapper;
import com.example.whiskervet.dto.VeterinarioMapper;
import com.example.whiskervet.entity.Citas;
import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.repository.RepositorioCitas;
import com.example.whiskervet.repository.RepositorioMascotas;
import com.example.whiskervet.repository.RepositorioUsuarios;
import com.example.whiskervet.servicio.correoelectronico.EmailService;
import com.example.whiskervet.servicio.usuario.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class CitaServiceImpl implements CitaService {
    
    private static final Logger logger = LoggerFactory.getLogger(CitaServiceImpl.class);

    @Autowired
    private RepositorioCitas citaRepository;

    @Autowired
    private RepositorioMascotas repositorioMascotas;

    @Autowired
    private UsuarioService usuarioService; 

    @Autowired
    private RepositorioUsuarios repositorioUsuarios;

    @Autowired
    private EmailService emailService;
    @Override
    public List<Citas> getAllCitas() {
        return citaRepository.findAll();
    }

    @Override
    public Citas getCitaById(Long id) {
        return citaRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCita(Citas cita) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaFormateada = dateFormat.format(cita.getFechaHora());

        logger.info("Fecha antes de guardar: {}", fechaFormateada);

        citaRepository.save(cita);
        emailService.enviarCorreo(cita);

        logger.info("Fecha después de guardar: {}", fechaFormateada);
    }

    @Override
    public void deleteCita(Long id) {
        citaRepository.deleteById(id);
    }

    @Override
    public List<Date> getHorasDisponiblesParaDia(Date fecha) {
        List<Date> horasDisponibles = new ArrayList<>();
    
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(fecha);
        calInicio.set(Calendar.HOUR_OF_DAY, 8);
        calInicio.set(Calendar.MINUTE, 0);
        calInicio.set(Calendar.SECOND, 0);
    
        Calendar calFin = Calendar.getInstance();
        calFin.setTime(fecha);
        calFin.set(Calendar.HOUR_OF_DAY, 17);
        calFin.set(Calendar.MINUTE, 0);
        calFin.set(Calendar.SECOND, 0);
    
        Calendar calActual = (Calendar) calInicio.clone();
        while (calActual.before(calFin)) {
            if (esHoraDisponible(calActual.getTime(), fecha)) {
                horasDisponibles.add(calActual.getTime());
            }
            calActual.add(Calendar.HOUR_OF_DAY, 1);
        }
    
        return horasDisponibles;
    }

    @Override
public boolean esHoraDisponible(Date horaActual, Date fecha) {
    Calendar calFin = Calendar.getInstance();
    calFin.setTime(horaActual);
    calFin.add(Calendar.HOUR_OF_DAY, 1);

    ZonedDateTime inicio = ZonedDateTime.ofInstant(horaActual.toInstant(), ZoneId.of("America/Bogota"));
    ZonedDateTime fin = ZonedDateTime.ofInstant(calFin.getTime().toInstant(), ZoneId.of("America/Bogota"));

    boolean hayCitasEnEsteRango = citaRepository.existsByFechaHoraBetweenAndFechaHoraBefore(
            java.sql.Timestamp.valueOf(inicio.toLocalDateTime()),
            java.sql.Timestamp.valueOf(fin.toLocalDateTime()),
            fecha
    );

    // Verifica si la hora está dentro del rango deseado (8 am - 5 pm)
    boolean dentroDelRango = inicio.getHour() >= 8 && inicio.getHour() < 17;

    logger.info("Fecha y hora: " + inicio + ", Hay citas en este rango: " + hayCitasEnEsteRango + ", Dentro del rango deseado: " + dentroDelRango);

    return !hayCitasEnEsteRango && dentroDelRango;
}

@Override
public List<Citas> buscarCitasPorIdMascota(Long idMascota) {
    return citaRepository.findByMascotaId(idMascota);
}

@Override
public List<CitasDTO> buscarCitasPorIdCliente(Long idCliente) {
    Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorCedula(idCliente);
    Usuario usuario = usuarioOptional.get();
    List<Mascota> mascotas = usuario.getMascotas();
    List<Citas> citas = new ArrayList<>();
    for (Mascota m : mascotas) {
        citas.addAll(citaRepository.findByMascotaId(m.getId()));
    }
    List<CitasDTO> citasDTO = new ArrayList<>();

    for (Citas c : citas) {
        CitasDTO citaDTO = CitasMapper.INSTANCE.convert(c);
        citaDTO.setNombreMascota(repositorioMascotas.findById(c.getIdMascota()).get().getNombre());
        citasDTO.add(citaDTO);

    }
    
    return citasDTO;
}

@Override
public List<CitasDTO> getCitasDelDiaActual() {
    LocalDate fechaActual = LocalDate.now();
    List<Citas> citas = citaRepository.findAll();
    List<Citas> citasDelDia = new ArrayList<>();
    System.out.println("HOY: "+ fechaActual);
    for (Citas cita : citas) {
        Date fechaCitaDate = cita.getFechaHora();

        // Convertir Date a LocalDate
        LocalDate fechaCitaLocalDate = fechaCitaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Comparar si la fecha de la cita es igual a la fecha actual
        if (fechaCitaLocalDate.equals(fechaActual)) {
            citasDelDia.add(cita);
        }
    }

    List<CitasDTO> citasDTO = new ArrayList<>();

        for (Citas c : citasDelDia) {
        CitasDTO citaDTO = CitasMapper.INSTANCE.convert(c);
        citaDTO.setNombreMascota(repositorioMascotas.findById(c.getIdMascota()).get().getNombre());
        citasDTO.add(citaDTO);

    }
    
    return citasDTO;
}




    

    private Date truncarHoraAlInicioDelDia(Date fecha) {
        return Date.from(fecha.toInstant().atZone(ZoneId.of("America/Bogota")).toLocalDate().atStartOfDay(ZoneId.of("America/Bogota")).toInstant());
    }

    private Date truncarHoraAlFinalDelDia(Date fecha) {
        LocalDateTime fechaFinal = fecha.toInstant().atZone(ZoneId.of("America/Bogota")).toLocalDate().atTime(23, 59, 59);
        return Date.from(fechaFinal.atZone(ZoneId.of("America/Bogota")).toInstant());
    }
}

