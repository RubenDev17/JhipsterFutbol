package com.cev.futbol.web.rest;

import com.cev.futbol.FutbolApp;
import com.cev.futbol.domain.Presidente;
import com.cev.futbol.repository.PresidenteRepository;
import com.cev.futbol.repository.search.PresidenteSearchRepository;
import com.cev.futbol.service.PresidenteService;

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
 * Integration tests for the {@link PresidenteResource} REST controller.
 */
@SpringBootTest(classes = FutbolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PresidenteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANIOS_EN_PRESIDENCIA = 1;
    private static final Integer UPDATED_ANIOS_EN_PRESIDENCIA = 2;

    @Autowired
    private PresidenteRepository presidenteRepository;

    @Autowired
    private PresidenteService presidenteService;

    /**
     * This repository is mocked in the com.cev.futbol.repository.search test package.
     *
     * @see com.cev.futbol.repository.search.PresidenteSearchRepositoryMockConfiguration
     */
    @Autowired
    private PresidenteSearchRepository mockPresidenteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresidenteMockMvc;

    private Presidente presidente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presidente createEntity(EntityManager em) {
        Presidente presidente = new Presidente()
            .nombre(DEFAULT_NOMBRE)
            .aniosEnPresidencia(DEFAULT_ANIOS_EN_PRESIDENCIA);
        return presidente;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presidente createUpdatedEntity(EntityManager em) {
        Presidente presidente = new Presidente()
            .nombre(UPDATED_NOMBRE)
            .aniosEnPresidencia(UPDATED_ANIOS_EN_PRESIDENCIA);
        return presidente;
    }

    @BeforeEach
    public void initTest() {
        presidente = createEntity(em);
    }

    @Test
    @Transactional
    public void createPresidente() throws Exception {
        int databaseSizeBeforeCreate = presidenteRepository.findAll().size();
        // Create the Presidente
        restPresidenteMockMvc.perform(post("/api/presidentes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(presidente)))
            .andExpect(status().isCreated());

        // Validate the Presidente in the database
        List<Presidente> presidenteList = presidenteRepository.findAll();
        assertThat(presidenteList).hasSize(databaseSizeBeforeCreate + 1);
        Presidente testPresidente = presidenteList.get(presidenteList.size() - 1);
        assertThat(testPresidente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPresidente.getAniosEnPresidencia()).isEqualTo(DEFAULT_ANIOS_EN_PRESIDENCIA);

        // Validate the Presidente in Elasticsearch
        verify(mockPresidenteSearchRepository, times(1)).save(testPresidente);
    }

    @Test
    @Transactional
    public void createPresidenteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = presidenteRepository.findAll().size();

        // Create the Presidente with an existing ID
        presidente.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresidenteMockMvc.perform(post("/api/presidentes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(presidente)))
            .andExpect(status().isBadRequest());

        // Validate the Presidente in the database
        List<Presidente> presidenteList = presidenteRepository.findAll();
        assertThat(presidenteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Presidente in Elasticsearch
        verify(mockPresidenteSearchRepository, times(0)).save(presidente);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = presidenteRepository.findAll().size();
        // set the field null
        presidente.setNombre(null);

        // Create the Presidente, which fails.


        restPresidenteMockMvc.perform(post("/api/presidentes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(presidente)))
            .andExpect(status().isBadRequest());

        List<Presidente> presidenteList = presidenteRepository.findAll();
        assertThat(presidenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPresidentes() throws Exception {
        // Initialize the database
        presidenteRepository.saveAndFlush(presidente);

        // Get all the presidenteList
        restPresidenteMockMvc.perform(get("/api/presidentes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presidente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].aniosEnPresidencia").value(hasItem(DEFAULT_ANIOS_EN_PRESIDENCIA)));
    }
    
    @Test
    @Transactional
    public void getPresidente() throws Exception {
        // Initialize the database
        presidenteRepository.saveAndFlush(presidente);

        // Get the presidente
        restPresidenteMockMvc.perform(get("/api/presidentes/{id}", presidente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presidente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.aniosEnPresidencia").value(DEFAULT_ANIOS_EN_PRESIDENCIA));
    }
    @Test
    @Transactional
    public void getNonExistingPresidente() throws Exception {
        // Get the presidente
        restPresidenteMockMvc.perform(get("/api/presidentes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresidente() throws Exception {
        // Initialize the database
        presidenteService.save(presidente);

        int databaseSizeBeforeUpdate = presidenteRepository.findAll().size();

        // Update the presidente
        Presidente updatedPresidente = presidenteRepository.findById(presidente.getId()).get();
        // Disconnect from session so that the updates on updatedPresidente are not directly saved in db
        em.detach(updatedPresidente);
        updatedPresidente
            .nombre(UPDATED_NOMBRE)
            .aniosEnPresidencia(UPDATED_ANIOS_EN_PRESIDENCIA);

        restPresidenteMockMvc.perform(put("/api/presidentes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPresidente)))
            .andExpect(status().isOk());

        // Validate the Presidente in the database
        List<Presidente> presidenteList = presidenteRepository.findAll();
        assertThat(presidenteList).hasSize(databaseSizeBeforeUpdate);
        Presidente testPresidente = presidenteList.get(presidenteList.size() - 1);
        assertThat(testPresidente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPresidente.getAniosEnPresidencia()).isEqualTo(UPDATED_ANIOS_EN_PRESIDENCIA);

        // Validate the Presidente in Elasticsearch
        verify(mockPresidenteSearchRepository, times(2)).save(testPresidente);
    }

    @Test
    @Transactional
    public void updateNonExistingPresidente() throws Exception {
        int databaseSizeBeforeUpdate = presidenteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresidenteMockMvc.perform(put("/api/presidentes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(presidente)))
            .andExpect(status().isBadRequest());

        // Validate the Presidente in the database
        List<Presidente> presidenteList = presidenteRepository.findAll();
        assertThat(presidenteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Presidente in Elasticsearch
        verify(mockPresidenteSearchRepository, times(0)).save(presidente);
    }

    @Test
    @Transactional
    public void deletePresidente() throws Exception {
        // Initialize the database
        presidenteService.save(presidente);

        int databaseSizeBeforeDelete = presidenteRepository.findAll().size();

        // Delete the presidente
        restPresidenteMockMvc.perform(delete("/api/presidentes/{id}", presidente.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Presidente> presidenteList = presidenteRepository.findAll();
        assertThat(presidenteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Presidente in Elasticsearch
        verify(mockPresidenteSearchRepository, times(1)).deleteById(presidente.getId());
    }

    @Test
    @Transactional
    public void searchPresidente() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        presidenteService.save(presidente);
        when(mockPresidenteSearchRepository.search(queryStringQuery("id:" + presidente.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(presidente), PageRequest.of(0, 1), 1));

        // Search the presidente
        restPresidenteMockMvc.perform(get("/api/_search/presidentes?query=id:" + presidente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presidente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].aniosEnPresidencia").value(hasItem(DEFAULT_ANIOS_EN_PRESIDENCIA)));
    }
}
