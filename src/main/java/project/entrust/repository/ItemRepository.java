package project.entrust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entrust.entity.Item;
import project.entrust.entity.Member;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(Member owner);
}
