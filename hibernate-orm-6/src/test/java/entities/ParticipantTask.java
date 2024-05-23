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

import java.util.UUID;


@Table(name = "participant_tasks")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ParticipantTask {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "participant_task_uuid", nullable = false)
	private UUID uuid;

	@Column(name = "program_task_uuid", nullable = false)
	private UUID programTaskUuid;


	@ManyToOne(optional=true)
	@JoinColumn(name="user_participant_id")
	private UserParticipant userParticipant;

}
