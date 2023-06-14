package entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "ohh_module")
@Entity
public class Module {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public UUID getModuleUuid() {
        return moduleUuid;
    }

    public void setModuleUuid(UUID moduleUuid) {
        this.moduleUuid = moduleUuid;
    }

    public Set<System> getSystems() {
        return systems;
    }

    public void setSystems(Set<System> systems) {
        this.systems = systems;
    }

    @Column(name = "module_uuid", nullable = false)
    private UUID moduleUuid;

    @ManyToMany
    @JoinTable(
            name = "ohh_module_system",
            joinColumns = {
                    @JoinColumn(name = "module_uuid", referencedColumnName = "module_uuid"),
                    @JoinColumn(name = "module_version", referencedColumnName = "version")
            },
            inverseJoinColumns = @JoinColumn(name = "system_uuid", referencedColumnName = "uuid"))
    private Set<System> systems = new HashSet<>();


    public void addSystem(System system){
        systems.add(system);
        if(!system.getModules().contains(this)){
            system.addModule(this);
        }

    }



}
