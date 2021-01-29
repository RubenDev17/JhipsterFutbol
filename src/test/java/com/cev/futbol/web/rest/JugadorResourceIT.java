package com.cev.futbol.web.rest;

import com.cev.futbol.FutbolApp;
import com.cev.futbol.domain.Jugador;
import com.cev.futbol.repository.JugadorRepository;
import com.cev.futbol.repository.search.JugadorSearchRepository;
import com.cev.futbol.service.JugadorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JugadorResource} REST controller.
 */
@SpringBootTest(classes = FutbolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class JugadorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDAD = 1;
    private static final Integer UPDATED_EDAD = 2;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private JugadorService jugadorService;

    /**
     * This repository is mocked in the com.cev.futbol.repository.search test package.
     *
     * @see com.cev.futbol.repository.search.JugadorSearchRepositoryMockConfiguration
     */
    @Autowired
    private JugadorSearchRepository mockJugadorSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJugadorMockMvc;

    private Jugador jugador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jugador createEntity(EntityManager em) {
        Jugador jugador = new Jugador()
            .nombre(DEFAULT_NOMBRE)
            .edad(DEFAULT_EDAD);
        return jugador;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jugador createUpdatedEntity(EntityManager em) {
        Jugador jugador = new Jugador()
            .nombre(UPDATED_NOMBRE)
            .edad(UPDATED_EDAD);
        return jugador;
    }

    @BeforeEach
    public void initTest() {
        jugador = createEntity(em);
    }

    @Test
    @Transactional
    public void createJugador() throws Exception {
        int databaseSizeBeforeCreate = jugadorRepository.findAll().size();
        // Create the Jugador
        restJugadorMockMvc.perform(post("/api/jugadors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jugador)))
            .andExpect(status().isCreated());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeCreate + 1);
        Jugador testJugador = jugadorList.get(jugadorList.size() - 1);
        assertThat(testJugador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testJugador.getEdad()).isEqualTo(DEFAULT_EDAD);

        // Validate the Jugador in Elasticsearch
        verify(mockJugadorSearchRepository, times(1)).save(testJugador);
    }

    @Test
    @Transactional
    public void createJugadorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jugadorRepository.findAll().size();

        // Create the Jugador with an existing ID
        jugador.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJugadorMockMvc.perform(post("/api/jugadors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jugador)))
            .andExpect(status().isBadRequest());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Jugador in Elasticsearch
        verify(mockJugadorSearchRepository, times(0)).save(jugador);
    }


    @Test
    @Transactional
    public void getAllJugadors() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList
        restJugadorMockMvc.perform(get("/api/jugadors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jugador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)));
    }
    
    @Test
    @Transactional
    public void getJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get the jugador
        restJugadorMockMvc.perform(get("/api/jugadors/{id}", jugador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jugador.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD));
    }
    @Test
    @Transactional
    public void getNonExistingJugador() throws Exception {
        // Get the jugador
        restJugadorMockMvc.perform(get("/api/jugadors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJugador() throws Exception {
        // Initialize the database
        jugadorService.save(jugador);

        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();

        // Update the jugador
        Jugador updatedJugador = jugadorRepository.findById(jugador.getId()).get();
        // Disconnect from session so that the updates on updatedJugador are not directly saved in db
        em.detach(updatedJugador);
        updatedJugador
            .nombre(UPDATED_NOMBRE)
            .edad(UPDATED_EDAD);

        restJugadorMockMvc.perform(put("/api/jugadors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJugador)))
            .andExpect(status().isOk());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
        Jugador testJugador = jugadorList.get(jugadorList.size() - 1);
        assertThat(testJugador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testJugador.getEdad()).isEqualTo(UPDATED_EDAD);

        // Validate the Jugador in Elasticsearch
        verify(mockJugadorSearchRepository, times(2)).save(testJugador);
    }

    @Test
    @Transactional
    public void updateNonExistingJugador() throws Exception {
        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJugadorMockMvc.perform(put("/api/jugadors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jugador)))
            .andExpect(status().isBadRequest());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Jugador in Elasticsearch
        verify(mockJugadorSearchRepository, times(0)).save(jugador);
    }

    @Test
    @Transactional
    public void deleteJugador() throws Exception {
        // Initialize the database
        jugadorService.save(jugador);

        int databaseSizeBeforeDelete = jugadorRepository.findAll().size();

        // Delete the jugador
        restJugadorMockMvc.perform(delete("/api/jugadors/{id}", jugador.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Jugador in Elasticsearch
        verify(mockJugadorSearchRepository, times(1)).deleteById(jugador.getId());
    }

    @Test
    @Transactional
    public void searchJugador() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        jugadorService.save(jugador);
        when(mockJugadorSearchRepository.search(queryStringQuery("id:" + jugador.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(jugador), PageRequest.of(0, 1), 1));

        // Search the jugador
        restJugadorMockMvc.perform(get("/api/_search/jugadors?query=id:" + jugador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jugador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)));
    }
}
