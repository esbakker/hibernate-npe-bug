package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Table(name = "tasks")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Task implements Serializable {


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program_task_uuid", nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_uuid", referencedColumnName = "program_uuid")
    @JoinColumn(name = "program_version", referencedColumnName = "version")
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "module_uuid", name = "module_uuid")
    @JoinColumn(referencedColumnName = "version", name = "module_version")
    private Module module;

    public void setProgram(Program program) {
        this.program = program;
        program.getTasks().add(this);
    }

}
