package ru.skillbox.model;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.common.SearchPersonDto;
import ru.skillbox.response.Responsable;
import ru.skillbox.response.data.PersonDto;

@CrossOrigin
@RequestMapping("/api/v1/friends")
//@PreAuthorize("hasAuthority('developers:read')")
public interface FriendsController {

    @GetMapping
    ResponseEntity<Page<PersonDto>> getRelationships(SearchPersonDto dto);

    @PostMapping(value = "/{id}/request")
    ResponseEntity<Responsable> sendFriendRequest(@PathVariable Long id);

    @PutMapping(value = "/{id}/approve")
    ResponseEntity<Responsable> approveFriendRequest(@PathVariable Long id);

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Responsable> deleteFriend(@PathVariable Long id);

    @PostMapping("/subscribe/{id}")
    ResponseEntity<Responsable> subscribe(@PathVariable Long id) ;

    @PutMapping("/block/{id}")
    ResponseEntity<Responsable> block(@PathVariable Long id) ;

    @GetMapping("/recommendations")
    ResponseEntity<Object> recommendations();

//    @GetMapping("/{accountId}")
//    List<Long>

    @GetMapping("/count")
    int count();
}
