package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "programs")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Program implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "program_uuid", nullable = false)
    private UUID programUuid;
    @Column(name = "version", nullable = false)
    private Long version;

    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "program_module",
            joinColumns = {
                    @JoinColumn(name = "program_uuid", referencedColumnName = "program_uuid"),
                    @JoinColumn(name = "program_version", referencedColumnName = "version")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "module_uuid", referencedColumnName = "module_uuid"),
                    @JoinColumn(name = "module_version", referencedColumnName = "version")
            })
    private List<Module> modules = new ArrayList<>();

    public void addModule(Module module) {
        this.modules.add(module);
        module.getPrograms().add(this);
    }
}
