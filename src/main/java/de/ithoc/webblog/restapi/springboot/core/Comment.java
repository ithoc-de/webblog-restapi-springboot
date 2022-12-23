package de.ithoc.webblog.restapi.springboot.core;


public class Comment {

	private String text;
	private String author;
	
	private Long postId;
	
	
	public Comment() {
	}
	
	public Comment(String text, String author) {
		this.text = text;
		this.author = author;
	}
	
	
	@Override
	public String toString() {
		return "CommentDto [text=" + text + ", author=" + author + "]";
	}


	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}
	
	
	
}
