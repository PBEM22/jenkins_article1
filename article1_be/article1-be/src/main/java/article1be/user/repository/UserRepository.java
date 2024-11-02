package article1be.user.repository;

import article1be.common.aggregate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 회원가입 여부 확인
    boolean existsByUserId(String email);
}
