package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_participants")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserParticipant {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_invite_id", referencedColumnName = "id")
    private UserInvite userInvite;

    @ManyToOne
    @JoinColumn(name = "program_uuid", referencedColumnName = "program_uuid")
    @JoinColumn(name = "program_version", referencedColumnName = "version")
    private Program program;

}
