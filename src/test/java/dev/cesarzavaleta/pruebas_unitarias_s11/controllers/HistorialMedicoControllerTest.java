package dev.cesarzavaleta.pruebas_unitarias_s11.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.cesarzavaleta.pruebas_unitarias_s11.dtos.HistorialMedicoDTO;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cita;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.HistorialMedico;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.CitaService;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.HistorialMedicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class HistorialMedicoControllerTest {

    private HistorialMedicoService historialMedicoService;
    private CitaService citaService;
    private HistorialMedicoController historialMedicoController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        historialMedicoService = mock(HistorialMedicoService.class);
        citaService = mock(CitaService.class);
        historialMedicoController = new HistorialMedicoController(historialMedicoService, citaService);
        mockMvc = MockMvcBuilders.standaloneSetup(historialMedicoController).build();
    }

    @Test
    void getAllHistorialMedico() throws Exception {
        HistorialMedico historialMedico1 = new HistorialMedico();
        historialMedico1.setIdHistorial(1L);
        historialMedico1.setDiagnostico("Parásitos intestinales");

        HistorialMedico historialMedico2 = new HistorialMedico();
        historialMedico2.setIdHistorial(2L);
        historialMedico2.setDiagnostico("Rabia");

        List<HistorialMedico> historialMedicos = Arrays.asList(historialMedico1, historialMedico2);

        when(historialMedicoService.findAll()).thenReturn(historialMedicos);

        mockMvc.perform(get("/historial_medico"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idHistorial", is(1)))
                .andExpect(jsonPath("$[0].diagnostico", is("Parásitos intestinales")))
                .andExpect(jsonPath("$[1].idHistorial", is(2)))
                .andExpect(jsonPath("$[1].diagnostico", is("Rabia")));
    }


    @Test
    void getHistorialMedicoById() throws Exception {
        HistorialMedico historialMedico = new HistorialMedico();
        historialMedico.setIdHistorial(1L);
        historialMedico.setDiagnostico("Diagnóstico");

        when(historialMedicoService.findById(1L)).thenReturn(historialMedico);

        mockMvc.perform(get("/historial_medico/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idHistorial", is(1)))
                .andExpect(jsonPath("$.diagnostico", is("Diagnóstico")));
    }


    @Test
    void createHistorialMedico() throws Exception {
        HistorialMedicoDTO historialMedicoDTO = new HistorialMedicoDTO();
        historialMedicoDTO.setDiagnostico("Pulgas");
        historialMedicoDTO.setTratamiento("Lavar");
        historialMedicoDTO.setObservaciones("Lavar muy bien");
        historialMedicoDTO.setCita(1L);

        Cita cita = new Cita();
        cita.setIdCita(1L);

        HistorialMedico historialMedico = new HistorialMedico();
        historialMedico.setDiagnostico("Pulgas");
        historialMedico.setTratamiento("Lavar");
        historialMedico.setObservaciones("Lavar muy bien");
        historialMedico.setCita(cita);

        when(citaService.findById(1L)).thenReturn(cita);
        when(historialMedicoService.save(any(HistorialMedico.class))).thenReturn(historialMedico);

        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/historial_medico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(o.writeValueAsString(historialMedicoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.diagnostico", is(historialMedicoDTO.getDiagnostico())))
                .andExpect(jsonPath("$.tratamiento", is(historialMedicoDTO.getTratamiento())))
                .andExpect(jsonPath("$.observaciones", is(historialMedicoDTO.getObservaciones())))
                .andExpect(jsonPath("$.cita.idCita", is(1)));
    }

    @Test
    void updateHistorialMedico() throws Exception {
        HistorialMedicoDTO historialMedicoDTO = new HistorialMedicoDTO();
        historialMedicoDTO.setDiagnostico("Rabia update");
        historialMedicoDTO.setTratamiento("Desparasitación interna. update");
        historialMedicoDTO.setObservaciones("Se desparasitó a la mascota y se le recomendó seguir tratamiento preventivo.");
        historialMedicoDTO.setCita(1L);

        Cita cita = new Cita();
        cita.setIdCita(1L);

        HistorialMedico existingHistorialMedico = new HistorialMedico();
        existingHistorialMedico.setIdHistorial(1L);
        existingHistorialMedico.setDiagnostico("Parásitos intestinales");
        existingHistorialMedico.setTratamiento("Desparasitación interna");
        existingHistorialMedico.setObservaciones("Se desparasitó a la mascota y se le recomendó seguir tratamiento preventivo.");
        existingHistorialMedico.setCita(new Cita());

        HistorialMedico updatedHistorialMedico = new HistorialMedico();
        updatedHistorialMedico.setIdHistorial(1L);
        updatedHistorialMedico.setDiagnostico("Rabia update");
        updatedHistorialMedico.setTratamiento("Desparasitación interna. update");
        updatedHistorialMedico.setObservaciones("Se desparasitó a la mascota y se le recomendó seguir tratamiento preventivo.");
        updatedHistorialMedico.setCita(cita);

        when(historialMedicoService.findById(1L)).thenReturn(existingHistorialMedico);
        when(citaService.findById(1L)).thenReturn(cita);
        when(historialMedicoService.save(any(HistorialMedico.class))).thenReturn(updatedHistorialMedico);

        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());

        mockMvc.perform(put("/historial_medico/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(o.writeValueAsString(historialMedicoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.diagnostico", is(historialMedicoDTO.getDiagnostico())))
                .andExpect(jsonPath("$.tratamiento", is(historialMedicoDTO.getTratamiento())))
                .andExpect(jsonPath("$.observaciones", is(historialMedicoDTO.getObservaciones())))
                .andExpect(jsonPath("$.cita.idCita", is(1)));
    }

    @Test
    void deleteHistorialMedico() throws Exception {
        HistorialMedico existingHistorialMedico = new HistorialMedico();
        existingHistorialMedico.setIdHistorial(1L);
        existingHistorialMedico.setDiagnostico("Diagnóstico a eliminar");

        when(historialMedicoService.findById(1L)).thenReturn(existingHistorialMedico);
        doNothing().when(historialMedicoService).deleteById(1L);

        mockMvc.perform(delete("/historial_medico/1"))
                .andExpect(status().isOk());
    }
}