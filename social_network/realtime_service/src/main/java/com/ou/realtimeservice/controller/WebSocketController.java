package com.ou.realtimeservice.controller;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ou.realtimeservice.pojo.CommentSocketModal;
import com.ou.realtimeservice.pojo.CommentTotalSocketModal;
import com.ou.realtimeservice.pojo.PostSocketModal;
import com.ou.realtimeservice.pojo.ReactionSocketModal;

@RestController
public class WebSocketController {
	@SendTo("/comment/{id}")
	public CommentSocketModal broadcastComment(@PathVariable Long id, @Payload CommentSocketModal comment) {
		return comment;
	}

	@SendTo("/reply/{id}")
	public CommentSocketModal broadcastReply(@PathVariable Long id, @Payload CommentSocketModal comment) {
		return comment;
	}

	@SendTo("/reaction/{id}")
	public ReactionSocketModal broadcastReaction(@PathVariable Long id, @Payload ReactionSocketModal reactionTotal) {
		return reactionTotal;
	}

	@SendTo("/comment-total/{id}")
	public CommentTotalSocketModal broadcastCommentTotal(@PathVariable Long id, @Payload CommentTotalSocketModal commentTotal) {
		return commentTotal;
	}

	@SendTo("/home")
	public PostSocketModal broadcastHome(@PathVariable Long id, @Payload PostSocketModal postModal) {
		return postModal;
	}

	@SendTo("/profile/{id}")
	public PostSocketModal broadcastProfile(@PathVariable Long id, @Payload PostSocketModal postModal) {
		return postModal;
	}
}
