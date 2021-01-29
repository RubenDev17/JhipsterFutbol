package com.cev.futbol.web.rest;

import com.cev.futbol.FutbolApp;
import com.cev.futbol.domain.Partido;
import com.cev.futbol.repository.PartidoRepository;
import com.cev.futbol.repository.search.PartidoSearchRepository;
import com.cev.futbol.service.PartidoService;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PartidoResource} REST controller.
 */
@SpringBootTest(classes = FutbolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PartidoResourceIT {

    private static final Integer DEFAULT_JORNADA = 1;
    private static final Integer UPDATED_JORNADA = 2;

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RIVAL = "AAAAAAAAAA";
    private static final String UPDATED_RIVAL = "BBBBBBBBBB";

    @Autowired
    private PartidoRepository partidoRepository;

    @Mock
    private PartidoRepository partidoRepositoryMock;

    @Mock
    private PartidoService partidoServiceMock;

    @Autowired
    private PartidoService partidoService;

    /**
     * This repository is mocked in the com.cev.futbol.repository.search test package.
     *
     * @see com.cev.futbol.repository.search.PartidoSearchRepositoryMockConfiguration
     */
    @Autowired
    private PartidoSearchRepository mockPartidoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartidoMockMvc;

    private Partido partido;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partido createEntity(EntityManager em) {
        Partido partido = new Partido()
            .jornada(DEFAULT_JORNADA)
            .fecha(DEFAULT_FECHA)
            .rival(DEFAULT_RIVAL);
        return partido;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partido createUpdatedEntity(EntityManager em) {
        Partido partido = new Partido()
            .jornada(UPDATED_JORNADA)
            .fecha(UPDATED_FECHA)
            .rival(UPDATED_RIVAL);
        return partido;
    }

    @BeforeEach
    public void initTest() {
        partido = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartido() throws Exception {
        int databaseSizeBeforeCreate = partidoRepository.findAll().size();
        // Create the Partido
        restPartidoMockMvc.perform(post("/api/partidos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partido)))
            .andExpect(status().isCreated());

        // Validate the Partido in the database
        List<Partido> partidoList = partidoRepository.findAll();
        assertThat(partidoList).hasSize(databaseSizeBeforeCreate + 1);
        Partido testPartido = partidoList.get(partidoList.size() - 1);
        assertThat(testPartido.getJornada()).isEqualTo(DEFAULT_JORNADA);
        assertThat(testPartido.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testPartido.getRival()).isEqualTo(DEFAULT_RIVAL);

        // Validate the Partido in Elasticsearch
        verify(mockPartidoSearchRepository, times(1)).save(testPartido);
    }

    @Test
    @Transactional
    public void createPartidoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partidoRepository.findAll().size();

        // Create the Partido with an existing ID
        partido.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartidoMockMvc.perform(post("/api/partidos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partido)))
            .andExpect(status().isBadRequest());

        // Validate the Partido in the database
        List<Partido> partidoList = partidoRepository.findAll();
        assertThat(partidoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Partido in Elasticsearch
        verify(mockPartidoSearchRepository, times(0)).save(partido);
    }


    @Test
    @Transactional
    public void getAllPartidos() throws Exception {
        // Initialize the database
        partidoRepository.saveAndFlush(partido);

        // Get all the partidoList
        restPartidoMockMvc.perform(get("/api/partidos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partido.getId().intValue())))
            .andExpect(jsonPath("$.[*].jornada").value(hasItem(DEFAULT_JORNADA)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].rival").value(hasItem(DEFAULT_RIVAL)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPartidosWithEagerRelationshipsIsEnabled() throws Exception {
        when(partidoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartidoMockMvc.perform(get("/api/partidos?eagerload=true"))
            .andExpect(status().isOk());

        verify(partidoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPartidosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partidoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartidoMockMvc.perform(get("/api/partidos?eagerload=true"))
            .andExpect(status().isOk());

        verify(partidoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPartido() throws Exception {
        // Initialize the database
        partidoRepository.saveAndFlush(partido);

        // Get the partido
        restPartidoMockMvc.perform(get("/api/partidos/{id}", partido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partido.getId().intValue()))
            .andExpect(jsonPath("$.jornada").value(DEFAULT_JORNADA))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.rival").value(DEFAULT_RIVAL));
    }
    @Test
    @Transactional
    public void getNonExistingPartido() throws Exception {
        // Get the partido
        restPartidoMockMvc.perform(get("/api/partidos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartido() throws Exception {
        // Initialize the database
        partidoService.save(partido);

        int databaseSizeBeforeUpdate = partidoRepository.findAll().size();

        // Update the partido
        Partido updatedPartido = partidoRepository.findById(partido.getId()).get();
        // Disconnect from session so that the updates on updatedPartido are not directly saved in db
        em.detach(updatedPartido);
        updatedPartido
            .jornada(UPDATED_JORNADA)
            .fecha(UPDATED_FECHA)
            .rival(UPDATED_RIVAL);

        restPartidoMockMvc.perform(put("/api/partidos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPartido)))
            .andExpect(status().isOk());

        // Validate the Partido in the database
        List<Partido> partidoList = partidoRepository.findAll();
        assertThat(partidoList).hasSize(databaseSizeBeforeUpdate);
        Partido testPartido = partidoList.get(partidoList.size() - 1);
        assertThat(testPartido.getJornada()).isEqualTo(UPDATED_JORNADA);
        assertThat(testPartido.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testPartido.getRival()).isEqualTo(UPDATED_RIVAL);

        // Validate the Partido in Elasticsearch
        verify(mockPartidoSearchRepository, times(2)).save(testPartido);
    }

    @Test
    @Transactional
    public void updateNonExistingPartido() throws Exception {
        int databaseSizeBeforeUpdate = partidoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartidoMockMvc.perform(put("/api/partidos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partido)))
            .andExpect(status().isBadRequest());

        // Validate the Partido in the database
        List<Partido> partidoList = partidoRepository.findAll();
        assertThat(partidoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Partido in Elasticsearch
        verify(mockPartidoSearchRepository, times(0)).save(partido);
    }

    @Test
    @Transactional
    public void deletePartido() throws Exception {
        // Initialize the database
        partidoService.save(partido);

        int databaseSizeBeforeDelete = partidoRepository.findAll().size();

        // Delete the partido
        restPartidoMockMvc.perform(delete("/api/partidos/{id}", partido.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Partido> partidoList = partidoRepository.findAll();
        assertThat(partidoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Partido in Elasticsearch
        verify(mockPartidoSearchRepository, times(1)).deleteById(partido.getId());
    }

    @Test
    @Transactional
    public void searchPartido() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        partidoService.save(partido);
        when(mockPartidoSearchRepository.search(queryStringQuery("id:" + partido.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(partido), PageRequest.of(0, 1), 1));

        // Search the partido
        restPartidoMockMvc.perform(get("/api/_search/partidos?query=id:" + partido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partido.getId().intValue())))
            .andExpect(jsonPath("$.[*].jornada").value(hasItem(DEFAULT_JORNADA)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].rival").value(hasItem(DEFAULT_RIVAL)));
    }
}
