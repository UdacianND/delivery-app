package uz.md.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByRoleId(int id);

    @Modifying
    @Query("update User set role = :insteadOfRoleId where role = :id")
    void updateRole(int id, int insteadOfRoleId);

    boolean existsByPhoneNumber(String phoneNumber);

//    @Modifying
//    @Query("update User set smsCode = :newPassword where id = :userId")
//    void updatePasswordByPhoneNumber(String newPassword, UUID user_id);
//
//    @Modifying
//    @Query("update User set deleted = true where phoneNumber = :currentPhoneNumber")
//    void deleteByPhoneNumber(String currentPhoneNumber);
}
