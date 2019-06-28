package conference.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import conference.domain.enumeration.Rooms;

/**
 * A DTO for the {@link conference.domain.Presentation} entity.
 */
public class PresentationDTO implements Serializable {

    private Long id;

    @NotNull
    private String presentationName;

    private String presentationTheme;

    @NotNull
    private Rooms presentationRoom;

    @NotNull
    private Instant presentationDate;


    private Set<UserDTO> owners = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresentationName() {
        return presentationName;
    }

    public void setPresentationName(String presentationName) {
        this.presentationName = presentationName;
    }

    public String getPresentationTheme() {
        return presentationTheme;
    }

    public void setPresentationTheme(String presentationTheme) {
        this.presentationTheme = presentationTheme;
    }

    public Rooms getPresentationRoom() {
        return presentationRoom;
    }

    public void setPresentationRoom(Rooms presentationRoom) {
        this.presentationRoom = presentationRoom;
    }

    public Instant getPresentationDate() {
        return presentationDate;
    }

    public void setPresentationDate(Instant presentationDate) {
        this.presentationDate = presentationDate;
    }

    public Set<UserDTO> getOwners() {
        return owners;
    }

    public void setOwners(Set<UserDTO> users) {
        this.owners = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PresentationDTO presentationDTO = (PresentationDTO) o;
        if (presentationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), presentationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PresentationDTO{" +
            "id=" + getId() +
            ", presentationName='" + getPresentationName() + "'" +
            ", presentationTheme='" + getPresentationTheme() + "'" +
            ", presentationRoom='" + getPresentationRoom() + "'" +
            ", presentationDate='" + getPresentationDate() + "'" +
            "}";
    }
}
