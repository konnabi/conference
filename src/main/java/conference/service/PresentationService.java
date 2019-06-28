package conference.service;

import conference.domain.Presentation;
import conference.repository.PresentationRepository;
import conference.service.dto.PresentationDTO;
import conference.service.mapper.PresentationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Presentation}.
 */
@Service
@Transactional
public class PresentationService {

    private final Logger log = LoggerFactory.getLogger(PresentationService.class);

    private final PresentationRepository presentationRepository;

    private final PresentationMapper presentationMapper;

    public PresentationService(PresentationRepository presentationRepository, PresentationMapper presentationMapper) {
        this.presentationRepository = presentationRepository;
        this.presentationMapper = presentationMapper;
    }

    /**
     * Save a presentation.
     *
     * @param presentationDTO the entity to save.
     * @return the persisted entity.
     */
    public PresentationDTO save(PresentationDTO presentationDTO) {
        log.debug("Request to save Presentation : {}", presentationDTO);
        Presentation presentation = presentationMapper.toEntity(presentationDTO);
        presentation = presentationRepository.save(presentation);
        return presentationMapper.toDto(presentation);
    }

    /**
     * Get all the presentations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PresentationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Presentations");
        return presentationRepository.findAll(pageable)
            .map(presentationMapper::toDto);
    }

    /**
     * Get all the presentations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PresentationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return presentationRepository.findAllWithEagerRelationships(pageable).map(presentationMapper::toDto);
    }
    

    /**
     * Get one presentation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PresentationDTO> findOne(Long id) {
        log.debug("Request to get Presentation : {}", id);
        return presentationRepository.findOneWithEagerRelationships(id)
            .map(presentationMapper::toDto);
    }

    /**
     * Delete the presentation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Presentation : {}", id);
        presentationRepository.deleteById(id);
    }
}
