package dev.cesarzavaleta.pruebas_unitarias_s11.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.cesarzavaleta.pruebas_unitarias_s11.dtos.MascotaDTO;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cliente;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Mascota;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.ClienteService;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.MascotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class MascotaControllerTest {

    private MascotaService mascotaService;
    private ClienteService clienteService;
    private MascotaController mascotaController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mascotaService = mock(MascotaService.class);
        clienteService = mock(ClienteService.class);
        mascotaController = new MascotaController(mascotaService,clienteService);
        mockMvc = MockMvcBuilders.standaloneSetup(mascotaController).build();
    }

    @Test
    void getAllMascotas() throws Exception {
        Mascota mascota1 = new Mascota();
        mascota1.setIdMascota(1L);
        mascota1.setNombre("Lucas");

        Mascota mascota2 = new Mascota();
        mascota2.setIdMascota(2L);
        mascota2.setNombre("Luna");

        List<Mascota> mascotas = Arrays.asList(mascota1, mascota2);

        when(mascotaService.findAll()).thenReturn(mascotas);

        mockMvc.perform(get("/mascotas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idMascota", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Lucas")))
                .andExpect(jsonPath("$[1].idMascota", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Luna")));
    }

    @Test
    void getMascotaById() throws Exception {
        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);
        mascota.setNombre("Lucas");

        when(mascotaService.findById(1L)).thenReturn(mascota);

        mockMvc.perform(get("/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idMascota", is(1)))
                .andExpect(jsonPath("$.nombre", is("Lucas")));
    }

    @Test
    void createMascota() throws Exception {
        MascotaDTO mascotaDTO = new MascotaDTO();
        mascotaDTO.setNombre("Sammy");
        mascotaDTO.setEspecie("Perro");
        mascotaDTO.setRaza("Labrador");
        mascotaDTO.setSexo("Macho");
        mascotaDTO.setFechaNacimiento(LocalDate.parse("2020-01-01"));
        mascotaDTO.setCliente(1L);

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Mascota mascota = new Mascota();
        mascota.setNombre("Sammy");
        mascota.setEspecie("Perro");
        mascota.setRaza("Labrador");
        mascota.setSexo("Macho");
        mascota.setFechaNacimiento(LocalDate.parse("2020-01-01"));
        mascota.setCliente(cliente);

        when(clienteService.findById(1L)).thenReturn(cliente);
        when(mascotaService.save(any(Mascota.class))).thenReturn(mascota);

        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(o.writeValueAsString(mascotaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.nombre", is(mascotaDTO.getNombre())))
                .andExpect(jsonPath("$.especie", is(mascotaDTO.getEspecie())))
                .andExpect(jsonPath("$.raza", is(mascotaDTO.getRaza())))
                .andExpect(jsonPath("$.sexo", is(mascotaDTO.getSexo())))
                .andExpect(jsonPath("$.fechaNacimiento", is("2020-01-01")))
                .andExpect(jsonPath("$.cliente.idCliente", is(1)));
    }

    @Test
    void updateMascota() throws Exception {
        MascotaDTO mascotaDTO = new MascotaDTO();
        mascotaDTO.setNombre("Lucas Updated");
        mascotaDTO.setEspecie("Perro");
        mascotaDTO.setRaza("Blanco");
        mascotaDTO.setSexo("Macho Updated");
        mascotaDTO.setFechaNacimiento(LocalDate.parse("2020-01-01"));
        mascotaDTO.setCliente(1L);

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Mascota existingMascota = new Mascota();
        existingMascota.setIdMascota(1L);
        existingMascota.setNombre("Lucas");
        existingMascota.setEspecie("Perro");
        existingMascota.setRaza("Golden Retriever");
        existingMascota.setSexo("Macho");
        existingMascota.setFechaNacimiento(LocalDate.parse("2020-01-01"));
        existingMascota.setCliente(cliente);

        Mascota updatedMascota = new Mascota();
        updatedMascota.setIdMascota(1L);
        updatedMascota.setNombre("Lucas Updated");
        updatedMascota.setEspecie("Perro");
        updatedMascota.setRaza("Blanco");
        updatedMascota.setSexo("Macho Updated");
        updatedMascota.setFechaNacimiento(LocalDate.parse("2020-01-01"));
        updatedMascota.setCliente(cliente);

        when(mascotaService.findById(1L)).thenReturn(existingMascota);
        when(clienteService.findById(1L)).thenReturn(cliente);
        when(mascotaService.save(any(Mascota.class))).thenReturn(updatedMascota);

        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());

        mockMvc.perform(put("/mascotas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(o.writeValueAsString(mascotaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.nombre", is(mascotaDTO.getNombre())))
                .andExpect(jsonPath("$.especie", is(mascotaDTO.getEspecie())))
                .andExpect(jsonPath("$.raza", is(mascotaDTO.getRaza())))
                .andExpect(jsonPath("$.sexo", is(mascotaDTO.getSexo())))
                .andExpect(jsonPath("$.fechaNacimiento", is("2020-01-01")))
                .andExpect(jsonPath("$.cliente.idCliente", is(1)));
    }

    @Test
    void deleteMascota() throws Exception {
        Mascota existingMascota = new Mascota();
        existingMascota.setIdMascota(3L);
        existingMascota.setNombre("Pelusa");

        when(mascotaService.findById(1L)).thenReturn(existingMascota);
        doNothing().when(mascotaService).deleteById(1L);

        mockMvc.perform(delete("/mascotas/3"))
                .andExpect(status().isOk());
    }
}