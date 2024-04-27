import axios from "axios";
import { load } from 'react-cookies';

export const endpoints = {
    "register": "/accounts/register",
    "verify": "/email/verify",
    "login": "/accounts/login",
    "status": "/accounts/status",
    "profile": "/posts/profile",
    "update_avatar": "/users/update_avatar",
    "update_cover": "/users/update_cover",
    "update_information": "/users/update_information",
    "upload": "/posts/upload",
    "posts": "/posts",
    "comment": "/comments",
    "change-password": "/accounts/change-password",
    "post_reactions": "/post_reactions",
    "search": "/accounts/search?kw=",
    "responses": "/responses",
    "create_private_room": "/realtime/chat_room/private",
    "messages": "/realtime/message",
    "seen": "/realtime/message/seen",
    "in_typing": "/realtime/typing/in_typing",
    "stop_typing": "/realtime/typing/stop_typing",
    "seen_notification": "/realtime/notification/seen",
    "socket": "/ws"
}

export const authAPI = () => axios.create({
    // baseURL: "http://127.0.0.1:8080/social_network/api",
    baseURL: 'http://127.0.0.1:8080/api',
    // baseURL: "http://34.101.48.117:80/api",
    headers: {
        "Authorization": `Bearer ${ load("access-token") }`
    }
})

export const socketUrl = 'http://127.0.0.1:8080/api/ws'
// export const socketUrl = 'http://34.101.48.117/api/ws'

export default axios.create({
    // baseURL: 'http://127.0.0.1:8080/social_network/api',
    baseURL: 'http://127.0.0.1:8080/api',
    // baseURL: 'http://34.101.48.117:80/api'
})