package com.cev.futbol.web.rest;

import com.cev.futbol.FutbolApp;
import com.cev.futbol.domain.Dato;
import com.cev.futbol.repository.DatoRepository;
import com.cev.futbol.repository.search.DatoSearchRepository;
import com.cev.futbol.service.DatoService;

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
 * Integration tests for the {@link DatoResource} REST controller.
 */
@SpringBootTest(classes = FutbolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DatoResourceIT {

    private static final Integer DEFAULT_JORNADA = 1;
    private static final Integer UPDATED_JORNADA = 2;

    private static final String DEFAULT_RIVAL = "AAAAAAAAAA";
    private static final String UPDATED_RIVAL = "BBBBBBBBBB";

    private static final String DEFAULT_RESULTADO = "AAAAAAAAAA";
    private static final String UPDATED_RESULTADO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_DE_GOLES = 1;
    private static final Integer UPDATED_NUMERO_DE_GOLES = 2;

    private static final Integer DEFAULT_CORNER = 1;
    private static final Integer UPDATED_CORNER = 2;

    private static final Integer DEFAULT_FALTAS = 1;
    private static final Integer UPDATED_FALTAS = 2;

    @Autowired
    private DatoRepository datoRepository;

    @Autowired
    private DatoService datoService;

    /**
     * This repository is mocked in the com.cev.futbol.repository.search test package.
     *
     * @see com.cev.futbol.repository.search.DatoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DatoSearchRepository mockDatoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDatoMockMvc;

    private Dato dato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dato createEntity(EntityManager em) {
        Dato dato = new Dato()
            .jornada(DEFAULT_JORNADA)
            .rival(DEFAULT_RIVAL)
            .resultado(DEFAULT_RESULTADO)
            .numeroDeGoles(DEFAULT_NUMERO_DE_GOLES)
            .corner(DEFAULT_CORNER)
            .faltas(DEFAULT_FALTAS);
        return dato;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dato createUpdatedEntity(EntityManager em) {
        Dato dato = new Dato()
            .jornada(UPDATED_JORNADA)
            .rival(UPDATED_RIVAL)
            .resultado(UPDATED_RESULTADO)
            .numeroDeGoles(UPDATED_NUMERO_DE_GOLES)
            .corner(UPDATED_CORNER)
            .faltas(UPDATED_FALTAS);
        return dato;
    }

    @BeforeEach
    public void initTest() {
        dato = createEntity(em);
    }

    @Test
    @Transactional
    public void createDato() throws Exception {
        int databaseSizeBeforeCreate = datoRepository.findAll().size();
        // Create the Dato
        restDatoMockMvc.perform(post("/api/datoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dato)))
            .andExpect(status().isCreated());

        // Validate the Dato in the database
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeCreate + 1);
        Dato testDato = datoList.get(datoList.size() - 1);
        assertThat(testDato.getJornada()).isEqualTo(DEFAULT_JORNADA);
        assertThat(testDato.getRival()).isEqualTo(DEFAULT_RIVAL);
        assertThat(testDato.getResultado()).isEqualTo(DEFAULT_RESULTADO);
        assertThat(testDato.getNumeroDeGoles()).isEqualTo(DEFAULT_NUMERO_DE_GOLES);
        assertThat(testDato.getCorner()).isEqualTo(DEFAULT_CORNER);
        assertThat(testDato.getFaltas()).isEqualTo(DEFAULT_FALTAS);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(1)).save(testDato);
    }

    @Test
    @Transactional
    public void createDatoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = datoRepository.findAll().size();

        // Create the Dato with an existing ID
        dato.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatoMockMvc.perform(post("/api/datoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dato)))
            .andExpect(status().isBadRequest());

        // Validate the Dato in the database
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(0)).save(dato);
    }


    @Test
    @Transactional
    public void checkRivalIsRequired() throws Exception {
        int databaseSizeBeforeTest = datoRepository.findAll().size();
        // set the field null
        dato.setRival(null);

        // Create the Dato, which fails.


        restDatoMockMvc.perform(post("/api/datoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dato)))
            .andExpect(status().isBadRequest());

        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDatoes() throws Exception {
        // Initialize the database
        datoRepository.saveAndFlush(dato);

        // Get all the datoList
        restDatoMockMvc.perform(get("/api/datoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dato.getId().intValue())))
            .andExpect(jsonPath("$.[*].jornada").value(hasItem(DEFAULT_JORNADA)))
            .andExpect(jsonPath("$.[*].rival").value(hasItem(DEFAULT_RIVAL)))
            .andExpect(jsonPath("$.[*].resultado").value(hasItem(DEFAULT_RESULTADO)))
            .andExpect(jsonPath("$.[*].numeroDeGoles").value(hasItem(DEFAULT_NUMERO_DE_GOLES)))
            .andExpect(jsonPath("$.[*].corner").value(hasItem(DEFAULT_CORNER)))
            .andExpect(jsonPath("$.[*].faltas").value(hasItem(DEFAULT_FALTAS)));
    }
    
    @Test
    @Transactional
    public void getDato() throws Exception {
        // Initialize the database
        datoRepository.saveAndFlush(dato);

        // Get the dato
        restDatoMockMvc.perform(get("/api/datoes/{id}", dato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dato.getId().intValue()))
            .andExpect(jsonPath("$.jornada").value(DEFAULT_JORNADA))
            .andExpect(jsonPath("$.rival").value(DEFAULT_RIVAL))
            .andExpect(jsonPath("$.resultado").value(DEFAULT_RESULTADO))
            .andExpect(jsonPath("$.numeroDeGoles").value(DEFAULT_NUMERO_DE_GOLES))
            .andExpect(jsonPath("$.corner").value(DEFAULT_CORNER))
            .andExpect(jsonPath("$.faltas").value(DEFAULT_FALTAS));
    }
    @Test
    @Transactional
    public void getNonExistingDato() throws Exception {
        // Get the dato
        restDatoMockMvc.perform(get("/api/datoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDato() throws Exception {
        // Initialize the database
        datoService.save(dato);

        int databaseSizeBeforeUpdate = datoRepository.findAll().size();

        // Update the dato
        Dato updatedDato = datoRepository.findById(dato.getId()).get();
        // Disconnect from session so that the updates on updatedDato are not directly saved in db
        em.detach(updatedDato);
        updatedDato
            .jornada(UPDATED_JORNADA)
            .rival(UPDATED_RIVAL)
            .resultado(UPDATED_RESULTADO)
            .numeroDeGoles(UPDATED_NUMERO_DE_GOLES)
            .corner(UPDATED_CORNER)
            .faltas(UPDATED_FALTAS);

        restDatoMockMvc.perform(put("/api/datoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDato)))
            .andExpect(status().isOk());

        // Validate the Dato in the database
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeUpdate);
        Dato testDato = datoList.get(datoList.size() - 1);
        assertThat(testDato.getJornada()).isEqualTo(UPDATED_JORNADA);
        assertThat(testDato.getRival()).isEqualTo(UPDATED_RIVAL);
        assertThat(testDato.getResultado()).isEqualTo(UPDATED_RESULTADO);
        assertThat(testDato.getNumeroDeGoles()).isEqualTo(UPDATED_NUMERO_DE_GOLES);
        assertThat(testDato.getCorner()).isEqualTo(UPDATED_CORNER);
        assertThat(testDato.getFaltas()).isEqualTo(UPDATED_FALTAS);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(2)).save(testDato);
    }

    @Test
    @Transactional
    public void updateNonExistingDato() throws Exception {
        int databaseSizeBeforeUpdate = datoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatoMockMvc.perform(put("/api/datoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dato)))
            .andExpect(status().isBadRequest());

        // Validate the Dato in the database
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(0)).save(dato);
    }

    @Test
    @Transactional
    public void deleteDato() throws Exception {
        // Initialize the database
        datoService.save(dato);

        int databaseSizeBeforeDelete = datoRepository.findAll().size();

        // Delete the dato
        restDatoMockMvc.perform(delete("/api/datoes/{id}", dato.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(1)).deleteById(dato.getId());
    }

    @Test
    @Transactional
    public void searchDato() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        datoService.save(dato);
        when(mockDatoSearchRepository.search(queryStringQuery("id:" + dato.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dato), PageRequest.of(0, 1), 1));

        // Search the dato
        restDatoMockMvc.perform(get("/api/_search/datoes?query=id:" + dato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dato.getId().intValue())))
            .andExpect(jsonPath("$.[*].jornada").value(hasItem(DEFAULT_JORNADA)))
            .andExpect(jsonPath("$.[*].rival").value(hasItem(DEFAULT_RIVAL)))
            .andExpect(jsonPath("$.[*].resultado").value(hasItem(DEFAULT_RESULTADO)))
            .andExpect(jsonPath("$.[*].numeroDeGoles").value(hasItem(DEFAULT_NUMERO_DE_GOLES)))
            .andExpect(jsonPath("$.[*].corner").value(hasItem(DEFAULT_CORNER)))
            .andExpect(jsonPath("$.[*].faltas").value(hasItem(DEFAULT_FALTAS)));
    }
}
