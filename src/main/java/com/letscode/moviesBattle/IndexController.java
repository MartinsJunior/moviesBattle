package com.letscode.moviesBattle;

import com.letscode.moviesBattle.model.Movie;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    private static final String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4OWIyYWQwMjFhYzU4NmQ1OTVlYjlmZmZiY2UyODFiYSIsInN1YiI6IjYyOGNjZTAwZDNkMzg3MDBhMTQ4MTE1ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.vGT4_NbdwYymxNaax5c-dVMf9UPO_XgWS61Cp7NQ6os";
    private static final String sufixImagePath = "https://image.tmdb.org/t/p/original/" ;
    private static final String urlApi = "https://api.themoviedb.org/3/movie/";


    @RequestMapping("/")
    public String index(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null) {
//            SAMLCredential credential = (SAMLCredential) authentication.getCredentials();
//            model.addAttribute("name", credential.getNameID().getValue());
//        }


        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i <4; i++) {
            movies.add(getvaluesFromEndPoint());

        }
        model.addAttribute("movies", movies);
        return "index";
    }

    private Movie getvaluesFromEndPoint() {
        JSONObject json = new JSONObject();
        do {
            Optional<JSONObject> jsonWithAllInformations = getJson();
            if (jsonWithAllInformations.isPresent()) {
                json = jsonWithAllInformations.get();
            }
        }while(json.isNull("poster_path"));
        return mapModelFromJsonValidInformation(json);
    }

    private Movie mapModelFromJsonValidInformation(JSONObject json) {
        Movie movie = new Movie();
        try{
            String imagePath = assemblyImagePath(json.getString("poster_path"));
            movie.setId(json.getLong("id"));
            movie.setTitle(json.getString("title"));
            movie.setVoteAverage(json.getBigDecimal("vote_average"));
            movie.setVoteCount(json.getInt("vote_count"));
            movie.setImagePath(imagePath);

        }catch (Exception e){
            e.printStackTrace();
        }
        return movie;
    }

    private Optional<JSONObject> getJson( ) {

        String theUrl = urlApi + getRandomId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = createHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
            return Optional.of(new JSONObject(response.getBody()));
        }
        catch (Exception eek) {
            return getJson();
        }
    }

    private double getRandomId() {
        return 10000 * Math.random();
    }

    private String assemblyImagePath(String  imagePath) {
        return sufixImagePath + imagePath;
    }

    private HttpHeaders createHttpHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+accessToken);
        return headers;
    }


}