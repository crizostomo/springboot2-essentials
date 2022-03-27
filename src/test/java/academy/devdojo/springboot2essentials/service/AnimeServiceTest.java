package academy.devdojo.springboot2essentials.service;

import academy.devdojo.springboot2essentials.controller.AnimeController;
import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.exception.ResourceNotFoundException;
import academy.devdojo.springboot2essentials.respository.AnimeRepository;
import academy.devdojo.springboot2essentials.respository.util.AnimeCreator;
import academy.devdojo.springboot2essentials.util.Utils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) //You don't need to load your db
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @Mock
    private Utils utilsMock;

    @BeforeEach
    public void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createValidAnime()))
                .thenReturn(AnimeCreator.createValidUpdatedAnime());

        BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(ArgumentMatchers.anyInt(),
                        ArgumentMatchers.any(AnimeRepository.class)))
                .thenReturn(AnimeCreator.createValidUpdatedAnime());

    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){
        String expectName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    public void findById_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){
        Integer expectId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeService.findById(1);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectId);
    }

    @Test
    @DisplayName("findByName returns a pageable list of animes when successful")
    public void findByName_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){
        String expectName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeService.findByName("DBZ");

        Assertions.assertThat(animeList).isNotNull();

        Assertions.assertThat(animeList).isNotEmpty();

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectName);
    }

    @Test
    @DisplayName("save creates an anime when successful")
    public void save_CreatesAnime_WhenSuccessful(){
        Integer expectId = AnimeCreator.createValidAnime().getId();

        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        Anime anime = animeService.save(animeToBeSaved);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectId);
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemovesAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeService.delete(1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException if when the anime does not exist")
    public void delete_ThrowsResourceNotFoundException_WhenAnimeDoesNotExist(){
        BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(ArgumentMatchers.anyInt(),
                        ArgumentMatchers.any(AnimeRepository.class)))
                .thenThrow(new ResourceNotFoundException("Anime not found"));
        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> animeService.delete(1));

    }

    @Test
    @DisplayName("save updating update anime when successful")
    public void save_SavesUpdatedAnime_WhenSuccessful(){
        Anime validUpdatedAnime = AnimeCreator.createValidUpdatedAnime();

        String expectedName = validUpdatedAnime.getName();

        Anime anime = animeService.save(AnimeCreator.createValidAnime());

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getName()).isEqualTo(expectedName
        );
    }


}