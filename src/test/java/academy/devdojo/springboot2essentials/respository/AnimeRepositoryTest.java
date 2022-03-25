package academy.devdojo.springboot2essentials.respository;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.respository.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Anime Repository Test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when successful")
    public void save_PersistAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(anime);
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("Save updates anime when successful")
    public void save_UpdateAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(anime);

        savedAnime.setName("That time I got reincarnated as a slime");

        Anime updatedAnime = this.animeRepository.save(savedAnime);

        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(updatedAnime.getName());
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    public void delete_RemoveAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(anime);

        this.animeRepository.delete(anime);

        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Find by name returns anime when successful")
    public void findByName_ReturnAnimes_WhenSuccessful(){
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(anime);

        String name = savedAnime.getName();

        List<Anime> animeList = this.animeRepository.findByName(name);

        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList).contains(savedAnime);
    }

    @Test
    @DisplayName("Find by name returns empty list when no anime is found")
    public void findByName_ReturnEmptyList_WhenAnimeNotFound(){

        String name = "fake-name";

        List<Anime> animeList = this.animeRepository.findByName(name);

        Assertions.assertThat(animeList).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    public void save_throwConstraintViolationException_WhenAnimeIsEmpty(){

        Anime anime = new Anime();

//        Assertions.assertThatThrownBy(()-> animeRepository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(()-> animeRepository.save(anime))
                .withMessageContaining("The name of this anime cannot be empty");
    }
}