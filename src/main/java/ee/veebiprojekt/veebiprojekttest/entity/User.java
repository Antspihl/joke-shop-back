package ee.veebiprojekt.veebiprojekttest.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "users")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "salt")
    private String salt;
    @Column(name = "is_admin")
    private boolean isAdmin;
    @Column(name="full_name")
    private String fullName;
}
