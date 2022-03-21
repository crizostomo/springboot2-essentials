package academy.devdojo.springboot2essentials.service;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.respository.AnimeRepository;
import academy.devdojo.springboot2essentials.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class AnimeService {

    private final Utils utils;

    private final AnimeRepository animeRepository;

//    private static List<Anime> animes;
//    static {
//        animes = new ArrayList<>(List.of(new Anime(1,"DBZ"),
//                new Anime(2,"Bersek"),
//                new Anime(3,"Boku no Hero")));
//    }
//
    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }

//    public List<Anime> listAll(){
//        return animeRepository.findAll();
//    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public Anime findById(int id){
        return utils.findAnimeOrThrowNotFound(id, animeRepository);
    }

    @Transactional
    public Anime save(Anime anime){
        return animeRepository.save(anime);
    }

    public void delete(int id){
        animeRepository.delete(utils.findAnimeOrThrowNotFound(id, animeRepository));
    }

    public void update(Anime anime){
        animeRepository.save(anime);
//        animeRepository.delete(utils.findAnimeOrThrowNotFound(anime.getId(), animeRepository));
//        animeRepository.add(anime);
    }
}
