package academy.devdojo.springboot2essentials.respository.util;

import academy.devdojo.springboot2essentials.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Tensei Shitara")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .name("Tensei Shitara")
                .id(1)
                .build();
    }

    public static Anime createValidUpdatedAnime(){
        return Anime.builder()
                .name("Tensei Shitara")
                .id(1)
                .build();
    }
}
