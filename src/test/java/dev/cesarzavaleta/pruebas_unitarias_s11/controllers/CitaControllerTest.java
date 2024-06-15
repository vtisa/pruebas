package dev.cesarzavaleta.pruebas_unitarias_s11.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.cesarzavaleta.pruebas_unitarias_s11.dtos.CitaDTO;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cita;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Mascota;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.CitaService;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.MascotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class CitaControllerTest {

    private CitaService citaService;
    private MascotaService mascotaService;
    private CitaController citaController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        citaService = mock(CitaService.class);
        mascotaService = mock(MascotaService.class);
        citaController = new CitaController(citaService, mascotaService);
        mockMvc = MockMvcBuilders.standaloneSetup(citaController).build();
    }

    @Test
    void getAllCitas() throws Exception {
        Cita cita1 = new Cita();
        cita1.setIdCita(1L);
        cita1.setFechaHora(LocalDateTime.parse("2024-05-25T10:00:00"));
        cita1.setMotivoConsulta("Revisión Anual");

        Cita cita2 = new Cita();
        cita2.setIdCita(2L);
        cita2.setFechaHora(LocalDateTime.parse("2024-06-15T15:30:00"));
        cita2.setMotivoConsulta("Vacunación");

        List<Cita> citas = Arrays.asList(cita1, cita2);

        when(citaService.findAll()).thenReturn(citas);

        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idCita", is(1)))
                .andExpect(jsonPath("$[0].fechaHora[0]", is(2024))) // Año
                .andExpect(jsonPath("$[0].fechaHora[1]", is(5))) // Mes
                .andExpect(jsonPath("$[0].fechaHora[2]", is(25))) // Día
                .andExpect(jsonPath("$[0].fechaHora[3]", is(10))) // Hora
                .andExpect(jsonPath("$[0].motivoConsulta", is("Revisión Anual")))
                .andExpect(jsonPath("$[1].idCita", is(2)))
                .andExpect(jsonPath("$[1].fechaHora[0]", is(2024))) // Año
                .andExpect(jsonPath("$[1].fechaHora[1]", is(6))) // Mes
                .andExpect(jsonPath("$[1].fechaHora[2]", is(15))) // Día
                .andExpect(jsonPath("$[1].fechaHora[3]", is(15))) // Hora
                .andExpect(jsonPath("$[1].motivoConsulta", is("Vacunación")));
    }


    @Test
    void getCitaById() throws Exception {
        Cita cita = new Cita();
        cita.setIdCita(1L);
        cita.setFechaHora(LocalDateTime.parse("2024-05-25T10:00:00"));
        cita.setMotivoConsulta("Vacunación");

        when(citaService.findById(1L)).thenReturn(cita);

        mockMvc.perform(get("/citas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idCita", is(1)))
                .andExpect(jsonPath("$.fechaHora[0]", is(2024))) // Año
                .andExpect(jsonPath("$.fechaHora[1]", is(5))) // Mes
                .andExpect(jsonPath("$.fechaHora[2]", is(25))) // Día
                .andExpect(jsonPath("$.fechaHora[3]", is(10))) // Hora
                .andExpect(jsonPath("$.motivoConsulta", is("Vacunación")));
    }



    @Test
    void createCita() throws Exception {
        CitaDTO citaDTO = new CitaDTO();
        citaDTO.setFechaHora(LocalDateTime.parse("2024-07-10T11:00:00"));
        citaDTO.setMotivoConsulta("Control Dermatológico");
        citaDTO.setMascota(3L);

        Mascota mascota = new Mascota();
        mascota.setIdMascota(3L);

        Cita cita = new Cita();
        cita.setFechaHora(LocalDateTime.parse("2024-07-10T11:00:00"));
        cita.setMotivoConsulta("Control Dermatológico");
        cita.setMascota(mascota);

        when(mascotaService.findById(3L)).thenReturn(mascota);
        when(citaService.save(any(Cita.class))).thenReturn(cita);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.fechaHora[0]", is(2024))) // Año
                .andExpect(jsonPath("$.fechaHora[1]", is(7))) // Mes
                .andExpect(jsonPath("$.fechaHora[2]", is(10))) // Día
                .andExpect(jsonPath("$.fechaHora[3]", is(11))) // Hora
                .andExpect(jsonPath("$.motivoConsulta", is("Control Dermatológico")))
                .andExpect(jsonPath("$.mascota.idMascota", is(3)));
    }


    @Test
    void updateCita() throws Exception {
        CitaDTO citaDTO = new CitaDTO();
        citaDTO.setFechaHora(LocalDateTime.parse("2024-07-10T11:00:00"));
        citaDTO.setMotivoConsulta("Control Dermatológico");
        citaDTO.setMascota(1L);

        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);

        Cita existingCita = new Cita();
        existingCita.setIdCita(1L);
        existingCita.setFechaHora(LocalDateTime.parse("2024-05-25T10:00:00"));
        existingCita.setMotivoConsulta("Revisión Anual");
        existingCita.setMascota(mascota);

        Cita updatedCita = new Cita();
        updatedCita.setIdCita(1L);
        updatedCita.setFechaHora(LocalDateTime.parse("2024-07-10T11:00:00"));
        updatedCita.setMotivoConsulta("Control Dermatológico");
        updatedCita.setMascota(mascota);

        when(citaService.findById(1L)).thenReturn(existingCita);
        when(mascotaService.findById(1L)).thenReturn(mascota);
        when(citaService.save(any(Cita.class))).thenReturn(updatedCita);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(put("/citas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.fechaHora[0]", is(2024))) // Año
                .andExpect(jsonPath("$.fechaHora[1]", is(7))) // Mes
                .andExpect(jsonPath("$.fechaHora[2]", is(10))) // Día
                .andExpect(jsonPath("$.fechaHora[3]", is(11))) // Hora
                .andExpect(jsonPath("$.motivoConsulta", is("Control Dermatológico")))
                .andExpect(jsonPath("$.mascota.idMascota", is(1)));
    }


    @Test
    void deleteCita() throws Exception {
        Cita existingCita = new Cita();
        existingCita.setIdCita(3L);
        existingCita.setFechaHora(LocalDateTime.parse("2024-07-10T11:00:00"));
        existingCita.setMotivoConsulta("Control Dermatológico");

        when(citaService.findById(3L)).thenReturn(existingCita);
        doNothing().when(citaService).deleteById(3L);

        mockMvc.perform(delete("/citas/3"))
                .andExpect(status().isOk());
    }
}