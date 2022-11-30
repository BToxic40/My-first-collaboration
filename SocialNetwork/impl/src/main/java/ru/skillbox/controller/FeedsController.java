package ru.skillbox.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.config.CloudinaryConfig;
import ru.skillbox.dto.CityDto;
import ru.skillbox.dto.CountryDto;
import ru.skillbox.dto.enums.StatusCode;
import ru.skillbox.model.*;
import ru.skillbox.repository.CountryRepository;
import ru.skillbox.repository.FriendsRepository;
import ru.skillbox.repository.PersonRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.response.CommentResponse;
import ru.skillbox.response.FeedsResponseOK;
import ru.skillbox.service.FeedsService;
import ru.skillbox.service.GeoService;
import ru.skillbox.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
public class FeedsController implements FeedsInterface {


    private final FeedsService feedsService;

    private final GeoService geoService;

    private PostRepository postRepository;

    private CountryRepository countryRepository;

    private FriendsRepository friendsRepository;

    private PersonRepository personRepository;

    private CloudinaryConfig config;

    private UserService userService;

    @Value("{${isTest}}")
    private String isTestString;


    @Autowired
    public FeedsController(FeedsService feedsService, PostRepository postRepository,
                           CountryRepository countryRepository, FriendsRepository friendsRepository,
                           UserService userService,
                           CloudinaryConfig config,
                           PersonRepository personRepository,
                           GeoService geoService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.feedsService = feedsService;
        this.countryRepository = countryRepository;
        this.friendsRepository = friendsRepository;
        this.config = config;
        this.personRepository = personRepository;
        this.geoService = geoService;
    }

    @Override
    @GetMapping("/api/v1/post")
    public ResponseEntity<FeedsResponseOK> getFeedsSearch(HttpServletRequest httpServletRequest

    )
            throws  IOException {

        Pageable pageable = feedsService.generatePageableObjectByServlet(httpServletRequest);

        return feedsService.getObjectResponseEntity(pageable, isTestString.equals("{true}"));
    }


    @GetMapping("/api/v1/geo/countries")
    public ResponseEntity<List<CountryDto>> getCountries(){
        return geoService.getCountries();
    }


    @GetMapping("api/v1/geo/cities/{countryId}")
    public ResponseEntity<List<CityDto>> getCities(@PathVariable long countryId){
        return geoService.getCities(countryId);
    }


    //Пока заглушка, ждем, когда подготовят эндпоинты к аккаунту
    @GetMapping("/api/v1/account/{id}")
    public ResponseEntity<Object> getOne(@PathVariable long id){
        Person person = personRepository.findById(id).get();
        return
                ResponseEntity.ok("{\n" +
                "  \"id\": " + person.getId() + ",\n" +
                "  \"email\": \"" + person.getEmail() + "\",\n" +
                "  \"phone\": \"" + person.getPhone() + "\",\n" +
                "  \"photo\": \"\",\n" +
                "  \"about\": \"" + person.getAbout() + "\",\n" +
                "  \"city\": \"string\",\n" +
                "  \"country\": \"string\",\n" +
                "  \"token\": \"string\",\n" +
                "  \"statusCode\": \"FRIEND\",\n" +
                "  \"firstName\": \"" + person.getFirstName() +  "\",\n" +
                "  \"lastName\": \"" + person.getLastName() + "\",\n" +
                "  \"regDate\": \"2022-11-16T05:00:33.669Z\",\n" +
                "  \"birthDate\": \"2022-11-16T05:00:33.669Z\",\n" +
                "  \"messagePermission\": \"string\",\n" +
                "  \"lastOnlineTime\": \"2022-11-16T05:00:33.669Z\",\n" +
                "  \"isOnline\": true,\n" +
                "  \"isBlocked\": true,\n" +
                "  \"isDeleted\": false,\n" +
                "  \"photoId\": \"string\",\n" +
                "  \"photoName\": \"string\",\n" +
                "  \"role\": \"USER\",\n" +
                "  \"createdOn\": \"2022-11-16T05:00:33.669Z\",\n" +
                "  \"updatedOn\": \"2022-11-16T05:00:33.669Z\",\n" +
                "  \"password\": \"string\"\n" +
                "}");
    }


    //Пока заглушка, чтобы не плодить сервисы
    @GetMapping("/api/v1/friends/count")
    public ResponseEntity countF(){
        Long myId = userService.getCurrentUser().getId();
        List<Friendship> friendshipList = friendsRepository.findAll();
        int count = 0;
        for(Friendship friendship : friendshipList){
            if (friendship.getDestPersonId().getId()==myId && friendship.getCode().equals(StatusCode.FRIEND)){
                count++;
            }
        }

        return ResponseEntity.ok(count);
    }

    @GetMapping("/api/v1/post/{id}/comment")
    public ResponseEntity<CommentResponse> getAllCommentsToPost(@PathVariable long id,
                                                                HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        Pageable pageable = feedsService.generatePageableObjectByServlet(httpServletRequest);
        return  feedsService.getComments(id,pageable,isTestString.equals("{true}"));
    }

    @GetMapping("/api/v1/post/{id}/comment/{commentId}/subcomment")
    public ResponseEntity<CommentResponse> getAllSubComments(@PathVariable long id, @PathVariable long commentId,
                                                    HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        Pageable pageable = feedsService.generatePageableObjectByServlet(httpServletRequest);
        return feedsService.getSubComments(id,commentId,pageable, isTestString.equals("{true}"));
    }
}
