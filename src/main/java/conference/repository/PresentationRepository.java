package conference.repository;

import conference.domain.Presentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Presentation entity.
 */
@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Long> {

    @Query(value = "select distinct presentation from Presentation presentation left join fetch presentation.owners",
        countQuery = "select count(distinct presentation) from Presentation presentation")
    Page<Presentation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct presentation from Presentation presentation left join fetch presentation.owners")
    List<Presentation> findAllWithEagerRelationships();

    @Query("select presentation from Presentation presentation left join fetch presentation.owners where presentation.id =:id")
    Optional<Presentation> findOneWithEagerRelationships(@Param("id") Long id);

}
