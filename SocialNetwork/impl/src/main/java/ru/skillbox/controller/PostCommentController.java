package ru.skillbox.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dto.Pageable;

import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.request.CommentAddRequest;
import ru.skillbox.response.post.PostCommentResponse;
import ru.skillbox.service.PersonService;
import ru.skillbox.service.PostCommentService;
import ru.skillbox.service.PostService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post/{id}/comment")
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final PostService postService;
    private final PersonService personService;

    private static Logger logger = LogManager.getLogger(PostCommentController.class);


    @Autowired
    public PostCommentController(PostCommentService postCommentService, PostService postService, PersonService personService) {
        this.postCommentService = postCommentService;
        this.postService = postService;
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Object> addCommentByIdPost(
            @PathVariable String id, @RequestBody CommentAddRequest request)  {
        Post post = postService.getPostById(Long.parseLong(id));
        logger.info("getting post by id " + id);
        PostComment postComment = new PostComment();
        postComment.setCommentText(request.getCommentText());
        postComment.setPerson(personService.getCurrentPerson());
        postComment.setPost(post);
        postComment.setTime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        postComment.setIsBlocked(false);
        if(request.getParentId()!=null) {
            postComment.setParentId(request.getParentId());
        } else {
            postComment.setParentId(0L);
        }
        postCommentService.savePostComment(postComment);
        logger.info("saving comment");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteCommentByIdPost(@PathVariable String id,
                                                        @PathVariable String commentId) {
        Post post = postService.getPostById(Long.parseLong(id));
        PostComment postComment = postCommentService.getPostCommentById(Long.parseLong(commentId));
        if (post.getPostCommentList().contains(postComment)) {
            postCommentService.deletePostComment(postComment);
            logger.info("deleting comment");
            post.getPostCommentList().remove(postComment);
            postService.savePost(post);
            return ResponseEntity.ok(HttpStatus.OK);

        }
        return ResponseEntity.badRequest().body("bad request");
    }

    //@GetMapping
    public ResponseEntity<PostCommentResponse> getCommentByIdPost(@PathVariable String id,
                                                                  @RequestParam Pageable pageable) {

        PostCommentResponse response = new PostCommentResponse();
        List<PostComment> postComments = postService.getPostById(Long.parseLong(id)).getPostCommentList();
        logger.info("getting comments by post id");
        response.setContent(List.of(postCommentService.setPostCommentDto(postComments)));
        response.setTotalPages(pageable.getPage());
        response.setNumber(pageable.getPage());
        response.setSize(pageable.getSize());
        response.setPageable(pageable);
        response.setEmpty(postComments.isEmpty());
        response.setFirst(postComments.get(0) != null);
        response.setLast(postComments.get(postComments.size() - 1) != null);
        return ResponseEntity.ok(response);
    }

    //@GetMapping("/{commentId}/subcomment")
    public ResponseEntity<PostCommentResponse> getByPostIdAndCommentId(@PathVariable String id,
                                                                       @PathVariable String commentId,
                                                                       @RequestParam Pageable pageable) {
        PostCommentResponse response = new PostCommentResponse();
        List<PostComment> postComments = postService.getPostById(Long.parseLong(id)).getPostCommentList();
        PostComment comment = postCommentService.getPostCommentById(Long.parseLong(commentId));
        logger.info("getting post and comment");
        response.setPageable(pageable);
        response.setSize(pageable.getSize());
        response.setTotalPages(pageable.getPage());
        response.setContent(List.of(postCommentService.setPostCommentDto(comment)));
        response.setEmpty(postComments.isEmpty());
        response.setFirst(postComments.get(0) != null);
        response.setLast(postComments.get(postComments.size() - 1) != null);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<Object> putCommentByIdPost(@RequestBody CommentAddRequest request,
                                                     @PathVariable String id, @PathVariable String commentId)  {
        Post post = postService.getPostById(Long.parseLong(id));
        PostComment postComment = postCommentService.getPostCommentById(Long.parseLong(commentId));
        if (!post.getPostCommentList().isEmpty()) {
            postComment.setCommentText(request.getCommentText());
            postComment.setPerson(personService.getCurrentPerson());
            postComment.setPost(post);
            postComment.setTime(request.getTime());
            postComment.setTimeChanged((new Date().getTime()));
            postComment.setIsBlocked(request.getIsBlocked());
            postCommentService.savePostComment(postComment);
            logger.info("updating comment");
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @PutMapping("/api/v1/post/{id}/comments/{comment_id}/recover")
//    public ResponseEntity<PostCommentResponse> recoveryCommentByIdPost(@PathVariable String id,
//                                                                       @PathVariable String comment_id) {
//        PostCommentResponse response = new PostCommentResponse();
//
//        Post post = postService.getPostById(Long.parseLong(id));
//        PostComment postComment = postCommentService.getPostCommentById(Long.parseLong(comment_id));
//        if (post.getPostCommentList().contains(postComment)) {
//            postCommentService.savePostComment(postComment);
//        }
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/api/v1/post/{id}/comments/{comment_id}/report")
//    public ResponseEntity<CommentComplaintResponse> addCommentComplaint(
//            @PathVariable String id, @PathVariable String comment_id) {
//        CommentComplaintResponse response = new CommentComplaintResponse();
//        response.setErrorDescription("error");
//        response.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
//        ComplaintResponse complaintResponse = new ComplaintResponse();
//        complaintResponse.setMessage("message");
//        response.setData(List.of(complaintResponse));
//        Post post = postService.getPostById(Long.parseLong(id));
//        PostComment postComment = postCommentService.getPostCommentById(Long.parseLong(comment_id));
//        return ResponseEntity.ok(response);
//    }
}
