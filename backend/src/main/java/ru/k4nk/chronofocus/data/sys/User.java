package ru.k4nk.chronofocus.data.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    String login;


    @JsonIgnore
    @Column(nullable = false)
    String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)

    private Set<Role> roles;

    public User(String login, String password, Set<Role> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
    }
}
