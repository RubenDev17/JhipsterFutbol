package com.cev.futbol.web.rest;

import com.cev.futbol.FutbolApp;
import com.cev.futbol.domain.Equipo;
import com.cev.futbol.repository.EquipoRepository;
import com.cev.futbol.repository.search.EquipoSearchRepository;
import com.cev.futbol.service.EquipoService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EquipoResource} REST controller.
 */
@SpringBootTest(classes = FutbolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EquipoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TITULOS = 1;
    private static final Integer UPDATED_TITULOS = 2;

    private static final Instant DEFAULT_FECHA_DE_FUNDACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_DE_FUNDACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private EquipoService equipoService;

    /**
     * This repository is mocked in the com.cev.futbol.repository.search test package.
     *
     * @see com.cev.futbol.repository.search.EquipoSearchRepositoryMockConfiguration
     */
    @Autowired
    private EquipoSearchRepository mockEquipoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipoMockMvc;

    private Equipo equipo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipo createEntity(EntityManager em) {
        Equipo equipo = new Equipo()
            .nombre(DEFAULT_NOMBRE)
            .titulos(DEFAULT_TITULOS)
            .fechaDeFundacion(DEFAULT_FECHA_DE_FUNDACION);
        return equipo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipo createUpdatedEntity(EntityManager em) {
        Equipo equipo = new Equipo()
            .nombre(UPDATED_NOMBRE)
            .titulos(UPDATED_TITULOS)
            .fechaDeFundacion(UPDATED_FECHA_DE_FUNDACION);
        return equipo;
    }

    @BeforeEach
    public void initTest() {
        equipo = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipo() throws Exception {
        int databaseSizeBeforeCreate = equipoRepository.findAll().size();
        // Create the Equipo
        restEquipoMockMvc.perform(post("/api/equipos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipo)))
            .andExpect(status().isCreated());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeCreate + 1);
        Equipo testEquipo = equipoList.get(equipoList.size() - 1);
        assertThat(testEquipo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEquipo.getTitulos()).isEqualTo(DEFAULT_TITULOS);
        assertThat(testEquipo.getFechaDeFundacion()).isEqualTo(DEFAULT_FECHA_DE_FUNDACION);

        // Validate the Equipo in Elasticsearch
        verify(mockEquipoSearchRepository, times(1)).save(testEquipo);
    }

    @Test
    @Transactional
    public void createEquipoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipoRepository.findAll().size();

        // Create the Equipo with an existing ID
        equipo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipoMockMvc.perform(post("/api/equipos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipo)))
            .andExpect(status().isBadRequest());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Equipo in Elasticsearch
        verify(mockEquipoSearchRepository, times(0)).save(equipo);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipoRepository.findAll().size();
        // set the field null
        equipo.setNombre(null);

        // Create the Equipo, which fails.


        restEquipoMockMvc.perform(post("/api/equipos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipo)))
            .andExpect(status().isBadRequest());

        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipos() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList
        restEquipoMockMvc.perform(get("/api/equipos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].titulos").value(hasItem(DEFAULT_TITULOS)))
            .andExpect(jsonPath("$.[*].fechaDeFundacion").value(hasItem(DEFAULT_FECHA_DE_FUNDACION.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipo() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get the equipo
        restEquipoMockMvc.perform(get("/api/equipos/{id}", equipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.titulos").value(DEFAULT_TITULOS))
            .andExpect(jsonPath("$.fechaDeFundacion").value(DEFAULT_FECHA_DE_FUNDACION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingEquipo() throws Exception {
        // Get the equipo
        restEquipoMockMvc.perform(get("/api/equipos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipo() throws Exception {
        // Initialize the database
        equipoService.save(equipo);

        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();

        // Update the equipo
        Equipo updatedEquipo = equipoRepository.findById(equipo.getId()).get();
        // Disconnect from session so that the updates on updatedEquipo are not directly saved in db
        em.detach(updatedEquipo);
        updatedEquipo
            .nombre(UPDATED_NOMBRE)
            .titulos(UPDATED_TITULOS)
            .fechaDeFundacion(UPDATED_FECHA_DE_FUNDACION);

        restEquipoMockMvc.perform(put("/api/equipos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEquipo)))
            .andExpect(status().isOk());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
        Equipo testEquipo = equipoList.get(equipoList.size() - 1);
        assertThat(testEquipo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEquipo.getTitulos()).isEqualTo(UPDATED_TITULOS);
        assertThat(testEquipo.getFechaDeFundacion()).isEqualTo(UPDATED_FECHA_DE_FUNDACION);

        // Validate the Equipo in Elasticsearch
        verify(mockEquipoSearchRepository, times(2)).save(testEquipo);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipo() throws Exception {
        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipoMockMvc.perform(put("/api/equipos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipo)))
            .andExpect(status().isBadRequest());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Equipo in Elasticsearch
        verify(mockEquipoSearchRepository, times(0)).save(equipo);
    }

    @Test
    @Transactional
    public void deleteEquipo() throws Exception {
        // Initialize the database
        equipoService.save(equipo);

        int databaseSizeBeforeDelete = equipoRepository.findAll().size();

        // Delete the equipo
        restEquipoMockMvc.perform(delete("/api/equipos/{id}", equipo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Equipo in Elasticsearch
        verify(mockEquipoSearchRepository, times(1)).deleteById(equipo.getId());
    }

    @Test
    @Transactional
    public void searchEquipo() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        equipoService.save(equipo);
        when(mockEquipoSearchRepository.search(queryStringQuery("id:" + equipo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(equipo), PageRequest.of(0, 1), 1));

        // Search the equipo
        restEquipoMockMvc.perform(get("/api/_search/equipos?query=id:" + equipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].titulos").value(hasItem(DEFAULT_TITULOS)))
            .andExpect(jsonPath("$.[*].fechaDeFundacion").value(hasItem(DEFAULT_FECHA_DE_FUNDACION.toString())));
    }
}
