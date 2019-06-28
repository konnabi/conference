package conference.web.rest;

import conference.ConferenceApp;
import conference.domain.Presentation;
import conference.domain.User;
import conference.repository.PresentationRepository;
import conference.service.PresentationService;
import conference.service.dto.PresentationDTO;
import conference.service.mapper.PresentationMapper;
import conference.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static conference.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import conference.domain.enumeration.Rooms;
/**
 * Integration tests for the {@Link PresentationResource} REST controller.
 */
@SpringBootTest(classes = ConferenceApp.class)
public class PresentationResourceIT {

    private static final String DEFAULT_PRESENTATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENTATION_THEME = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTATION_THEME = "BBBBBBBBBB";

    private static final Rooms DEFAULT_PRESENTATION_ROOM = Rooms.A505;
    private static final Rooms UPDATED_PRESENTATION_ROOM = Rooms.F328;

    private static final Instant DEFAULT_PRESENTATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRESENTATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PresentationRepository presentationRepository;

    @Mock
    private PresentationRepository presentationRepositoryMock;

    @Autowired
    private PresentationMapper presentationMapper;

    @Mock
    private PresentationService presentationServiceMock;

    @Autowired
    private PresentationService presentationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPresentationMockMvc;

    private Presentation presentation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PresentationResource presentationResource = new PresentationResource(presentationService);
        this.restPresentationMockMvc = MockMvcBuilders.standaloneSetup(presentationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentation createEntity(EntityManager em) {
        Presentation presentation = new Presentation()
            .presentationName(DEFAULT_PRESENTATION_NAME)
            .presentationTheme(DEFAULT_PRESENTATION_THEME)
            .presentationRoom(DEFAULT_PRESENTATION_ROOM)
            .presentationDate(DEFAULT_PRESENTATION_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        presentation.getOwners().add(user);
        return presentation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentation createUpdatedEntity(EntityManager em) {
        Presentation presentation = new Presentation()
            .presentationName(UPDATED_PRESENTATION_NAME)
            .presentationTheme(UPDATED_PRESENTATION_THEME)
            .presentationRoom(UPDATED_PRESENTATION_ROOM)
            .presentationDate(UPDATED_PRESENTATION_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        presentation.getOwners().add(user);
        return presentation;
    }

    @BeforeEach
    public void initTest() {
        presentation = createEntity(em);
    }

    @Test
    @Transactional
    public void createPresentation() throws Exception {
        int databaseSizeBeforeCreate = presentationRepository.findAll().size();

        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);
        restPresentationMockMvc.perform(post("/api/presentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presentationDTO)))
            .andExpect(status().isCreated());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeCreate + 1);
        Presentation testPresentation = presentationList.get(presentationList.size() - 1);
        assertThat(testPresentation.getPresentationName()).isEqualTo(DEFAULT_PRESENTATION_NAME);
        assertThat(testPresentation.getPresentationTheme()).isEqualTo(DEFAULT_PRESENTATION_THEME);
        assertThat(testPresentation.getPresentationRoom()).isEqualTo(DEFAULT_PRESENTATION_ROOM);
        assertThat(testPresentation.getPresentationDate()).isEqualTo(DEFAULT_PRESENTATION_DATE);
    }

    @Test
    @Transactional
    public void createPresentationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = presentationRepository.findAll().size();

        // Create the Presentation with an existing ID
        presentation.setId(1L);
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresentationMockMvc.perform(post("/api/presentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presentationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPresentationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = presentationRepository.findAll().size();
        // set the field null
        presentation.setPresentationName(null);

        // Create the Presentation, which fails.
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        restPresentationMockMvc.perform(post("/api/presentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presentationDTO)))
            .andExpect(status().isBadRequest());

        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPresentationRoomIsRequired() throws Exception {
        int databaseSizeBeforeTest = presentationRepository.findAll().size();
        // set the field null
        presentation.setPresentationRoom(null);

        // Create the Presentation, which fails.
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        restPresentationMockMvc.perform(post("/api/presentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presentationDTO)))
            .andExpect(status().isBadRequest());

        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPresentationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = presentationRepository.findAll().size();
        // set the field null
        presentation.setPresentationDate(null);

        // Create the Presentation, which fails.
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        restPresentationMockMvc.perform(post("/api/presentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presentationDTO)))
            .andExpect(status().isBadRequest());

        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPresentations() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        // Get all the presentationList
        restPresentationMockMvc.perform(get("/api/presentations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].presentationName").value(hasItem(DEFAULT_PRESENTATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].presentationTheme").value(hasItem(DEFAULT_PRESENTATION_THEME.toString())))
            .andExpect(jsonPath("$.[*].presentationRoom").value(hasItem(DEFAULT_PRESENTATION_ROOM.toString())))
            .andExpect(jsonPath("$.[*].presentationDate").value(hasItem(DEFAULT_PRESENTATION_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPresentationsWithEagerRelationshipsIsEnabled() throws Exception {
        PresentationResource presentationResource = new PresentationResource(presentationServiceMock);
        when(presentationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPresentationMockMvc = MockMvcBuilders.standaloneSetup(presentationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPresentationMockMvc.perform(get("/api/presentations?eagerload=true"))
        .andExpect(status().isOk());

        verify(presentationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPresentationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        PresentationResource presentationResource = new PresentationResource(presentationServiceMock);
            when(presentationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPresentationMockMvc = MockMvcBuilders.standaloneSetup(presentationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPresentationMockMvc.perform(get("/api/presentations?eagerload=true"))
        .andExpect(status().isOk());

            verify(presentationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPresentation() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        // Get the presentation
        restPresentationMockMvc.perform(get("/api/presentations/{id}", presentation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(presentation.getId().intValue()))
            .andExpect(jsonPath("$.presentationName").value(DEFAULT_PRESENTATION_NAME.toString()))
            .andExpect(jsonPath("$.presentationTheme").value(DEFAULT_PRESENTATION_THEME.toString()))
            .andExpect(jsonPath("$.presentationRoom").value(DEFAULT_PRESENTATION_ROOM.toString()))
            .andExpect(jsonPath("$.presentationDate").value(DEFAULT_PRESENTATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPresentation() throws Exception {
        // Get the presentation
        restPresentationMockMvc.perform(get("/api/presentations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresentation() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();

        // Update the presentation
        Presentation updatedPresentation = presentationRepository.findById(presentation.getId()).get();
        // Disconnect from session so that the updates on updatedPresentation are not directly saved in db
        em.detach(updatedPresentation);
        updatedPresentation
            .presentationName(UPDATED_PRESENTATION_NAME)
            .presentationTheme(UPDATED_PRESENTATION_THEME)
            .presentationRoom(UPDATED_PRESENTATION_ROOM)
            .presentationDate(UPDATED_PRESENTATION_DATE);
        PresentationDTO presentationDTO = presentationMapper.toDto(updatedPresentation);

        restPresentationMockMvc.perform(put("/api/presentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presentationDTO)))
            .andExpect(status().isOk());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
        Presentation testPresentation = presentationList.get(presentationList.size() - 1);
        assertThat(testPresentation.getPresentationName()).isEqualTo(UPDATED_PRESENTATION_NAME);
        assertThat(testPresentation.getPresentationTheme()).isEqualTo(UPDATED_PRESENTATION_THEME);
        assertThat(testPresentation.getPresentationRoom()).isEqualTo(UPDATED_PRESENTATION_ROOM);
        assertThat(testPresentation.getPresentationDate()).isEqualTo(UPDATED_PRESENTATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPresentation() throws Exception {
        int databaseSizeBeforeUpdate = presentationRepository.findAll().size();

        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresentationMockMvc.perform(put("/api/presentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presentationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Presentation in the database
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePresentation() throws Exception {
        // Initialize the database
        presentationRepository.saveAndFlush(presentation);

        int databaseSizeBeforeDelete = presentationRepository.findAll().size();

        // Delete the presentation
        restPresentationMockMvc.perform(delete("/api/presentations/{id}", presentation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Presentation> presentationList = presentationRepository.findAll();
        assertThat(presentationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presentation.class);
        Presentation presentation1 = new Presentation();
        presentation1.setId(1L);
        Presentation presentation2 = new Presentation();
        presentation2.setId(presentation1.getId());
        assertThat(presentation1).isEqualTo(presentation2);
        presentation2.setId(2L);
        assertThat(presentation1).isNotEqualTo(presentation2);
        presentation1.setId(null);
        assertThat(presentation1).isNotEqualTo(presentation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PresentationDTO.class);
        PresentationDTO presentationDTO1 = new PresentationDTO();
        presentationDTO1.setId(1L);
        PresentationDTO presentationDTO2 = new PresentationDTO();
        assertThat(presentationDTO1).isNotEqualTo(presentationDTO2);
        presentationDTO2.setId(presentationDTO1.getId());
        assertThat(presentationDTO1).isEqualTo(presentationDTO2);
        presentationDTO2.setId(2L);
        assertThat(presentationDTO1).isNotEqualTo(presentationDTO2);
        presentationDTO1.setId(null);
        assertThat(presentationDTO1).isNotEqualTo(presentationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(presentationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(presentationMapper.fromId(null)).isNull();
    }
}
