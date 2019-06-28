package conference.service.mapper;

import conference.domain.*;
import conference.service.dto.PresentationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Presentation} and its DTO {@link PresentationDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PresentationMapper extends EntityMapper<PresentationDTO, Presentation> {


    @Mapping(target = "removeOwner", ignore = true)

    default Presentation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Presentation presentation = new Presentation();
        presentation.setId(id);
        return presentation;
    }
}
