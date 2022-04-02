package project.entrust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entrust.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
