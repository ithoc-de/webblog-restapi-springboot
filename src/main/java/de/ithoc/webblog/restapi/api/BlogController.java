package de.ithoc.webblog.restapi.api;

import de.ithoc.webblog.restapi.core.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/blogs")
@Slf4j
public class BlogController {


	@GetMapping("/read-posts")
	public ResponseEntity<List<Post>> readPosts() {
		
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@GetMapping("/read-post")
	public ResponseEntity<Post> readPost(@RequestParam("id") String id) {
		log.trace("id: {}", id);

		Post post = new Post();
		post.setId(id);
		post.setTitle("Magic Vehicle");
		post.setContent("Magic (supernatural), beliefs and actions employed " +
				"to influence supernatural beings and forces");

		log.trace("post: {}", post);
		return new ResponseEntity<>(post, HttpStatus.OK);
	}


	@GetMapping("/search-posts")
	public ResponseEntity<List<Post>> searchPosts(@RequestParam("text") String text) {
		log.trace("text: {}", text);

		Post post = new Post(
				UUID.randomUUID().toString(),
				"Text Search",
				"In computer science, string-searching algorithms, " +
						"sometimes called string-matching algorithms, " +
						"are an important class of string algorithms " +
						"that try to find a place where one or several strings " +
						"(also called patterns) are found within a larger string or text.");
		post.setContent(post.getContent() + " " + text);

		return new ResponseEntity<>(List.of(post), HttpStatus.OK);
	}


	@PostMapping("/create-post")
	public ResponseEntity<Post> createPost(@RequestBody Post post) {
		
		return new ResponseEntity<>(post, HttpStatus.OK);
	}


	@PostMapping(value = "/attach-picture")
	public ResponseEntity<Void> attachPicture(@RequestPart("picture") MultipartFile picture) {
		log.trace("picture: {}", picture);

		log.trace("OK");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
