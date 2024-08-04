package ir.jibit.directdebit.gateway.balejbbot.data;

import ir.jibit.directdebit.gateway.balejbbot.data.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findAdminByChatId(String chatId);

    Optional<Admin> findAdminByUsernameAndPassword(String username, String password);

    boolean existsAdminByChatId(String chatId);
}