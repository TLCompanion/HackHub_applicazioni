import jakarta.persistence.*;

@Entity
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    protected Utente() {} // richiesto da JPA

    public Utente(String email) { this.email = email; }

    public Long getId() { return id; }
    public String getEmail() { return email; }
}
