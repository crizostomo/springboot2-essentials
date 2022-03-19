package academy.devdojo.springboot2essentials.respository;

import academy.devdojo.springboot2essentials.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Integer> {

    List<Anime> findByName(String name);
}
