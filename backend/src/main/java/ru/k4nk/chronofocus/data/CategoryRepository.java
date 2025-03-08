package ru.k4nk.chronofocus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("""
                select c from Category as c
                join c.user as u
                where u.login = :userLogin
            """)
    List<Category> findAllByLogin(String userLogin);

    @Query("""
                select c from Category as c
                join c.user as u
                where u.login = :userLogin
                and c.id in :ids
            """)
    List<Category> findAllByLoginAndId(String userLogin, List<Integer> ids);

    @Query("""
                select c from Category as c
                join c.user as u
                where u.login = :userLogin
                and c.id = :id
            """)
    Optional<Category> findAllByLoginAndId(String userLogin, Integer id);

    @Query("""
                 select c from Category as c
                 join c.user as u
                 where c in :categories
                 and u.login != :login
            """)
    Set<TimeSegment> findAllCategoriesBelongToOtherLogin(String login, List<Category> categories);

}
