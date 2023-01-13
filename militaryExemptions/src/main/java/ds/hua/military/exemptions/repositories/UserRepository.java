package ds.hua.military.exemptions.repositories;

import ds.hua.military.exemptions.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query(value="select * from user where username=:username and role='ROLE_ADMIN'", nativeQuery = true)
    User findAdminByUsername(@Param("username") String username);}