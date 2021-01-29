package com.cev.futbol.web.rest;

import com.cev.futbol.FutbolApp;
import com.cev.futbol.domain.Temporada;
import com.cev.futbol.repository.TemporadaRepository;
import com.cev.futbol.repository.search.TemporadaSearchRepository;
import com.cev.futbol.service.TemporadaService;

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
 * Integration tests for the {@link TemporadaResource} REST controller.
 */
@SpringBootTest(classes = FutbolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TemporadaResourceIT {

    private static final String DEFAULT_ANIO = "AAAAAAAAAA";
    private static final String UPDATED_ANIO = "BBBBBBBBBB";

    @Autowired
    private TemporadaRepository temporadaRepository;

    @Autowired
    private TemporadaService temporadaService;

    /**
     * This repository is mocked in the com.cev.futbol.repository.search test package.
     *
     * @see com.cev.futbol.repository.search.TemporadaSearchRepositoryMockConfiguration
     */
    @Autowired
    private TemporadaSearchRepository mockTemporadaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemporadaMockMvc;

    private Temporada temporada;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temporada createEntity(EntityManager em) {
        Temporada temporada = new Temporada()
            .anio(DEFAULT_ANIO);
        return temporada;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temporada createUpdatedEntity(EntityManager em) {
        Temporada temporada = new Temporada()
            .anio(UPDATED_ANIO);
        return temporada;
    }

    @BeforeEach
    public void initTest() {
        temporada = createEntity(em);
    }

    @Test
    @Transactional
    public void createTemporada() throws Exception {
        int databaseSizeBeforeCreate = temporadaRepository.findAll().size();
        // Create the Temporada
        restTemporadaMockMvc.perform(post("/api/temporadas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temporada)))
            .andExpect(status().isCreated());

        // Validate the Temporada in the database
        List<Temporada> temporadaList = temporadaRepository.findAll();
        assertThat(temporadaList).hasSize(databaseSizeBeforeCreate + 1);
        Temporada testTemporada = temporadaList.get(temporadaList.size() - 1);
        assertThat(testTemporada.getAnio()).isEqualTo(DEFAULT_ANIO);

        // Validate the Temporada in Elasticsearch
        verify(mockTemporadaSearchRepository, times(1)).save(testTemporada);
    }

    @Test
    @Transactional
    public void createTemporadaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = temporadaRepository.findAll().size();

        // Create the Temporada with an existing ID
        temporada.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemporadaMockMvc.perform(post("/api/temporadas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temporada)))
            .andExpect(status().isBadRequest());

        // Validate the Temporada in the database
        List<Temporada> temporadaList = temporadaRepository.findAll();
        assertThat(temporadaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Temporada in Elasticsearch
        verify(mockTemporadaSearchRepository, times(0)).save(temporada);
    }


    @Test
    @Transactional
    public void checkAnioIsRequired() throws Exception {
        int databaseSizeBeforeTest = temporadaRepository.findAll().size();
        // set the field null
        temporada.setAnio(null);

        // Create the Temporada, which fails.


        restTemporadaMockMvc.perform(post("/api/temporadas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temporada)))
            .andExpect(status().isBadRequest());

        List<Temporada> temporadaList = temporadaRepository.findAll();
        assertThat(temporadaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTemporadas() throws Exception {
        // Initialize the database
        temporadaRepository.saveAndFlush(temporada);

        // Get all the temporadaList
        restTemporadaMockMvc.perform(get("/api/temporadas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temporada.getId().intValue())))
            .andExpect(jsonPath("$.[*].anio").value(hasItem(DEFAULT_ANIO)));
    }
    
    @Test
    @Transactional
    public void getTemporada() throws Exception {
        // Initialize the database
        temporadaRepository.saveAndFlush(temporada);

        // Get the temporada
        restTemporadaMockMvc.perform(get("/api/temporadas/{id}", temporada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(temporada.getId().intValue()))
            .andExpect(jsonPath("$.anio").value(DEFAULT_ANIO));
    }
    @Test
    @Transactional
    public void getNonExistingTemporada() throws Exception {
        // Get the temporada
        restTemporadaMockMvc.perform(get("/api/temporadas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemporada() throws Exception {
        // Initialize the database
        temporadaService.save(temporada);

        int databaseSizeBeforeUpdate = temporadaRepository.findAll().size();

        // Update the temporada
        Temporada updatedTemporada = temporadaRepository.findById(temporada.getId()).get();
        // Disconnect from session so that the updates on updatedTemporada are not directly saved in db
        em.detach(updatedTemporada);
        updatedTemporada
            .anio(UPDATED_ANIO);

        restTemporadaMockMvc.perform(put("/api/temporadas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTemporada)))
            .andExpect(status().isOk());

        // Validate the Temporada in the database
        List<Temporada> temporadaList = temporadaRepository.findAll();
        assertThat(temporadaList).hasSize(databaseSizeBeforeUpdate);
        Temporada testTemporada = temporadaList.get(temporadaList.size() - 1);
        assertThat(testTemporada.getAnio()).isEqualTo(UPDATED_ANIO);

        // Validate the Temporada in Elasticsearch
        verify(mockTemporadaSearchRepository, times(2)).save(testTemporada);
    }

    @Test
    @Transactional
    public void updateNonExistingTemporada() throws Exception {
        int databaseSizeBeforeUpdate = temporadaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemporadaMockMvc.perform(put("/api/temporadas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temporada)))
            .andExpect(status().isBadRequest());

        // Validate the Temporada in the database
        List<Temporada> temporadaList = temporadaRepository.findAll();
        assertThat(temporadaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Temporada in Elasticsearch
        verify(mockTemporadaSearchRepository, times(0)).save(temporada);
    }

    @Test
    @Transactional
    public void deleteTemporada() throws Exception {
        // Initialize the database
        temporadaService.save(temporada);

        int databaseSizeBeforeDelete = temporadaRepository.findAll().size();

        // Delete the temporada
        restTemporadaMockMvc.perform(delete("/api/temporadas/{id}", temporada.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Temporada> temporadaList = temporadaRepository.findAll();
        assertThat(temporadaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Temporada in Elasticsearch
        verify(mockTemporadaSearchRepository, times(1)).deleteById(temporada.getId());
    }

    @Test
    @Transactional
    public void searchTemporada() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        temporadaService.save(temporada);
        when(mockTemporadaSearchRepository.search(queryStringQuery("id:" + temporada.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(temporada), PageRequest.of(0, 1), 1));

        // Search the temporada
        restTemporadaMockMvc.perform(get("/api/_search/temporadas?query=id:" + temporada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temporada.getId().intValue())))
            .andExpect(jsonPath("$.[*].anio").value(hasItem(DEFAULT_ANIO)));
    }
}
