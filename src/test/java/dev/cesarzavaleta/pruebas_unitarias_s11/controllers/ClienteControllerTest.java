package dev.cesarzavaleta.pruebas_unitarias_s11.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.cesarzavaleta.pruebas_unitarias_s11.model.Cliente;
import dev.cesarzavaleta.pruebas_unitarias_s11.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


class ClienteControllerTest {

    private ClienteService clienteService;
    private MockMvc mockMvc;
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        clienteService = mock(ClienteService.class);
        clienteController = new ClienteController(clienteService);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    @Test
    void getAllClientes() throws Exception {
        Cliente cliente1 = new Cliente();
        cliente1.setIdCliente(1L);
        cliente1.setNombre("Juan");

        Cliente cliente2 = new Cliente();
        cliente2.setIdCliente(2L);
        cliente2.setNombre("Ana");

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        when(clienteService.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idCliente", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[1].idCliente", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Ana")));
    }

    @Test
    public void getClienteById() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNombre("Juan");

        when(clienteService.findById(1L)).thenReturn(cliente);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idCliente", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan")));
    }

    @Test
    void createCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNombre("Carlos");
        cliente.setApellido("Pérez");
        cliente.setDireccion("Calle Francisco 123");
        cliente.setTelefono("555-1754");
        cliente.setCorreoElectronico("carlos@example.com");

        when(clienteService.save(any(Cliente.class))).thenReturn(cliente);

        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(o.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.nombre", is(cliente.getNombre())))
                .andExpect(jsonPath("$.apellido", is(cliente.getApellido())))
                .andExpect(jsonPath("$.direccion", is(cliente.getDireccion())))
                .andExpect(jsonPath("$.telefono", is(cliente.getTelefono())))
                .andExpect(jsonPath("$.correoElectronico", is(cliente.getCorreoElectronico())));
    }

    @Test
    void updateCliente() throws Exception {
        Cliente existingCliente = new Cliente();
        existingCliente.setIdCliente(1L);
        existingCliente.setNombre("Juan");
        existingCliente.setApellido("García");
        existingCliente.setDireccion("Calle 123");
        existingCliente.setTelefono("555-1234");
        existingCliente.setCorreoElectronico("juan@example.com");

        Cliente updatedCliente = new Cliente();
        updatedCliente.setNombre("Juan Updated");
        updatedCliente.setApellido("García Updated");
        updatedCliente.setDireccion("Calle 123 Updated");
        updatedCliente.setTelefono("555-5678");
        updatedCliente.setCorreoElectronico("juan.updated@example.com");

        when(clienteService.findById(1L)).thenReturn(existingCliente);
        when(clienteService.save(any(Cliente.class))).thenReturn(updatedCliente);

        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(o.writeValueAsString(updatedCliente)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.nombre", is(updatedCliente.getNombre())))
                .andExpect(jsonPath("$.apellido", is(updatedCliente.getApellido())))
                .andExpect(jsonPath("$.direccion", is(updatedCliente.getDireccion())))
                .andExpect(jsonPath("$.telefono", is(updatedCliente.getTelefono())))
                .andExpect(jsonPath("$.correoElectronico", is(updatedCliente.getCorreoElectronico())));
    }

    @Test
    void deleteCliente() throws Exception {
        Cliente existingCliente = new Cliente();
        existingCliente.setIdCliente(1L);
        existingCliente.setNombre("Juan");

        when(clienteService.findById(1L)).thenReturn(existingCliente);
        doNothing().when(clienteService).deleteById(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isOk());
    }
}
