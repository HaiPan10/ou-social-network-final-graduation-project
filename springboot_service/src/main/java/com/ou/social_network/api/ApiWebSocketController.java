package com.ou.social_network.api;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ou.social_network.socketModal.SocketCommentModal;
import com.ou.social_network.socketModal.SocketCommentTotalModal;
import com.ou.social_network.socketModal.SocketPostModal;
import com.ou.social_network.socketModal.SocketReactionModal;

@RestController
public class ApiWebSocketController {
	@SendTo("/comment/{id}")
	public SocketCommentModal broadcastComment(@PathVariable Long id, @Payload SocketCommentModal comment) {
		return comment;
	}

	@SendTo("/reply/{id}")
	public SocketCommentModal broadcastReply(@PathVariable Long id, @Payload SocketCommentModal comment) {
		return comment;
	}

	@SendTo("/reaction/{id}")
	public SocketReactionModal broadcastReaction(@PathVariable Long id, @Payload SocketReactionModal reactionTotal) {
		return reactionTotal;
	}

	@SendTo("/comment-total/{id}")
	public SocketCommentTotalModal broadcastCommentTotal(@PathVariable Long id, @Payload SocketCommentTotalModal commentTotal) {
		return commentTotal;
	}

	@SendTo("/home")
	public SocketPostModal broadcastHome(@PathVariable Long id, @Payload SocketPostModal postModal) {
		return postModal;
	}

	@SendTo("/profile/{id}")
	public SocketPostModal broadcastProfile(@PathVariable Long id, @Payload SocketPostModal postModal) {
		return postModal;
	}
}
