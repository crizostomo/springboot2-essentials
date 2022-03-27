package academy.devdojo.springboot2essentials.integration;

import academy.devdojo.springboot2essentials.controller.AnimeController;
import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.respository.AnimeRepository;
import academy.devdojo.springboot2essentials.respository.util.AnimeCreator;
import academy.devdojo.springboot2essentials.service.AnimeService;
import academy.devdojo.springboot2essentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Use this annotation when you have your db running
class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private AnimeRepository animeRepositoryMock;

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

    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){
        String expectName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    public void findById_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){
        Integer expectId = AnimeCreator.createValidAnime().getId();

        Anime anime = testRestTemplate.getForObject("/animes/1", Anime.class);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectId);
    }

    @Test
    @DisplayName("findByName returns a list of animes when successful")
    public void findByName_ReturnListOfAnimes_WhenSuccessful(){
        String expectName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = testRestTemplate.exchange("/animes/find?name='tensei'",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList).isNotNull();

        Assertions.assertThat(animeList).isNotEmpty();

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectName);
    }

    @Test
    @DisplayName("save creates an anime when successful")
    public void save_CreatesAnime_WhenSuccessful(){
        Integer expectId = AnimeCreator.createValidAnime().getId();

        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        Anime anime = testRestTemplate.exchange("/animes", HttpMethod.POST,
                createJsonHttpEntity(animeToBeSaved), Anime.class).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectId);
    }

    private HttpEntity<Anime> createJsonHttpEntity(Anime anime){
        return new HttpEntity<>(anime, createJsonHeader());
    }
    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemovesAnime_WhenSuccessful(){

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange("/animes/1",
                HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update saves updated anime when successful")
    public void update_SavesUpdatedAnime_WhenSuccessful(){
        Anime validAnime = AnimeCreator.createValidAnime();

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange("/animes",
                HttpMethod.PUT, createJsonHttpEntity(validAnime), Void.class);

        Assertions.assertThat(responseEntity).isNotNull();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

}