package ru.k4nk.chronofocus.data.sys;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity(name = "_users_webui")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false, unique = true)
    String login;


    @Column(nullable = false)
    String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)

    private Set<Role> roles = new HashSet<>();

    public User(String login, String password, Set<Role> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
    }
}
