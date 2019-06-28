package conference.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import conference.domain.enumeration.Rooms;

/**
 * A Presentation.
 */
@Entity
@Table(name = "presentation")
public class Presentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "presentation_name", nullable = false)
    private String presentationName;

    @Column(name = "presentation_theme")
    private String presentationTheme;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "presentation_room", nullable = false)
    private Rooms presentationRoom;

    @NotNull
    @Column(name = "presentation_date", nullable = false)
    private Instant presentationDate;

    @ManyToMany
    @NotNull
    @JoinTable(name = "presentation_owner",
               joinColumns = @JoinColumn(name = "presentation_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"))
    private Set<User> owners = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresentationName() {
        return presentationName;
    }

    public Presentation presentationName(String presentationName) {
        this.presentationName = presentationName;
        return this;
    }

    public void setPresentationName(String presentationName) {
        this.presentationName = presentationName;
    }

    public String getPresentationTheme() {
        return presentationTheme;
    }

    public Presentation presentationTheme(String presentationTheme) {
        this.presentationTheme = presentationTheme;
        return this;
    }

    public void setPresentationTheme(String presentationTheme) {
        this.presentationTheme = presentationTheme;
    }

    public Rooms getPresentationRoom() {
        return presentationRoom;
    }

    public Presentation presentationRoom(Rooms presentationRoom) {
        this.presentationRoom = presentationRoom;
        return this;
    }

    public void setPresentationRoom(Rooms presentationRoom) {
        this.presentationRoom = presentationRoom;
    }

    public Instant getPresentationDate() {
        return presentationDate;
    }

    public Presentation presentationDate(Instant presentationDate) {
        this.presentationDate = presentationDate;
        return this;
    }

    public void setPresentationDate(Instant presentationDate) {
        this.presentationDate = presentationDate;
    }

    public Set<User> getOwners() {
        return owners;
    }

    public Presentation owners(Set<User> users) {
        this.owners = users;
        return this;
    }

    public Presentation addOwner(User user) {
        this.owners.add(user);
        return this;
    }

    public Presentation removeOwner(User user) {
        this.owners.remove(user);
        return this;
    }

    public void setOwners(Set<User> users) {
        this.owners = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Presentation)) {
            return false;
        }
        return id != null && id.equals(((Presentation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Presentation{" +
            "id=" + getId() +
            ", presentationName='" + getPresentationName() + "'" +
            ", presentationTheme='" + getPresentationTheme() + "'" +
            ", presentationRoom='" + getPresentationRoom() + "'" +
            ", presentationDate='" + getPresentationDate() + "'" +
            "}";
    }
}
