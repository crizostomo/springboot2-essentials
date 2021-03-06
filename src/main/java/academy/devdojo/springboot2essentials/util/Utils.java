package academy.devdojo.springboot2essentials.util;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.exception.ResourceNotFoundException;
import academy.devdojo.springboot2essentials.respository.AnimeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Utils {
    public String formatLocalDateTimeToDatabaseStyle(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public Anime findAnimeOrThrowNotFound(int id, AnimeRepository animeRepository){
        return animeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anime not found"));
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }
}
