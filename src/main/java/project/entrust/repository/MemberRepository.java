package project.entrust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entrust.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
