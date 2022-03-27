package academy.devdojo.springboot2essentials.client;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SpringClient {

    /**
     * Need to be tested again
     */

    public static void main(String[] args) {
//        testGetWithRestTemplate();

        ResponseEntity<PageableResponse<Anime>> exchangeAnimeList = new RestTemplate()
                .exchange("http://localhost:8080/animes?sort=name,desc",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PageableResponse<Anime>>() {});

        log.info("Anime list {}", exchangeAnimeList.getBody());

        Anime overlord = Anime.builder().name("Overlord").build();
//        Anime overlordSaved = new RestTemplate().
//                postForObject("http://localhost:8080/animes?sort=name,desc",
//                        overlord, Anime.class);
//
//        log.info("Overlord saved id: {}", overlordSaved.getId());

        Anime steinsGate = Anime.builder().name("Steins Gate").build();

        Anime steinsGateSaved = new RestTemplate()
                .exchange("http://localhost:8080/animes?sort=name,desc",
                        HttpMethod.POST,
                        new HttpEntity<>(steinsGate, createJsonHeader()),
                        Anime.class).getBody();

        log.info("Steins Gate saved id: {}", steinsGateSaved.getId());

        steinsGateSaved.setName("Steins Gate Zero");
        ResponseEntity<Void> exchangeUpdated = new RestTemplate()
                .exchange("http://localhost:8080/animes?sort=name,desc",
                        HttpMethod.PUT,
                        new HttpEntity<>(steinsGateSaved, createJsonHeader()),
                        Void.class);

        log.info("Steins Gate updated status {}", exchangeUpdated.getStatusCode());

        ResponseEntity<Void> exchangeDeleted = new RestTemplate()
                .exchange("http://localhost:8080/animes/{id}",
                        HttpMethod.DELETE,
                        null,
                        Void.class, steinsGateSaved.getId());

        log.info("Steins Gate deleted status {}", exchangeDeleted.getStatusCode());

    }


    private static void testGetWithRestTemplate() {
        ResponseEntity<Anime> animeResponseEntity = new RestTemplate()
                .getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info("Response Entity {}", animeResponseEntity);

        log.info("Response Data {}", animeResponseEntity.getBody());

        Anime anime = new RestTemplate()
                .getForObject("http://localhost:8080/animes/1", Anime.class);

        log.info("Anime {}", anime);
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
