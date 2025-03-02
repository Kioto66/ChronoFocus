package ru.k4nk.chronofocus.services;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.k4nk.chronofocus.data.sys.Role;
import ru.k4nk.chronofocus.data.sys.RoleRepository;
import ru.k4nk.chronofocus.data.sys.User;
import ru.k4nk.chronofocus.data.sys.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Optional<User> findByLogin(@NonNull String login) {
        User byLogin = userRepository.findByLogin(login);
        if (byLogin == null) {
            return Optional.empty();
        }
        return Optional.of(byLogin);
    }

    @Transactional
    public User createUser(String login, String password, String roleName) {
        final Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        return userRepository.save(new User(login, password, Set.of(role)));
    }
}