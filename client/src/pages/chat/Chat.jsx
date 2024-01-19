import { useContext, useEffect, useRef, useState } from 'react';
import './chat.scss'
import { collection, onSnapshot, where, query, orderBy, doc, getDoc } from 'firebase/firestore';
import { db } from '../../configs/firebase';
import { useParams } from 'react-router-dom';
import SendIcon from '@mui/icons-material/Send';
import { AuthContext } from '../../context/AuthContext';
import WestIcon from '@mui/icons-material/West';
import { authAPI, endpoints } from '../../configs/Api';
import Moment from 'react-moment';
import { LeftBarContext } from '../../context/LeftBarContext';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { InView } from 'react-intersection-observer';
import { Typing } from './Typing';

export const Chat = () => {
    const [newMessage, setNewMessage] = useState("")
    const messageRef = collection(db, "message")
    const userChatRoomRef = collection(db, "userChatRoom")
    const chatRoomRef = collection(db, "chatRoom")
    const [messages, setMessages] = useState([]);
    const { id } = useParams();
    const [targetUser, setTargetUser] = useState(null)
    const [user, userDispatch] = useContext(AuthContext)
    const [room, setRoom] = useState(null)
    const { setCurrentMenu } = useContext(LeftBarContext)
    const { setShowChat } = useContext(LeftBarContext)
    const { setSmallLeftBar } = useContext(LeftBarContext)
    const bottomRef = useRef(null);
    const [typing, setTyping] = useState(false)
    const [targetTyping, setTargetTyping] = useState(false)

    const handleSubmit = async (evt) => {
        evt.preventDefault()

        if (newMessage === "") return;
        if (room === null) {
            try {
                let res = await authAPI().post(endpoints['create_private_room'] + `/${targetUser.userId}`, {
                    "content": newMessage
                })
                if (res.status === 200) {
                    setRoom(room => ({ ...room, ["id"]: res.data }))
                }
            } catch (ex) {
            }
        } else {
            try {
                await authAPI().post(endpoints['messages'] + `/${room.id}`, {
                    "content": newMessage
                })
            } catch (ex) {
            }
        }
        setNewMessage("")
    }

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: 'instant' })
    }, [messages.length, targetTyping])

    useEffect(() => {
        setSmallLeftBar(true)
        setShowChat(true)
        setCurrentMenu('chat')
    }, [])

    useEffect(() => {
        let targetUserUnsubscribe
        let roomUnsubscribe

        const init = () => {
            setMessages([])
            setTargetUser(null)
            setRoom(null)
            setTyping(false)
        }

        const fetchTargetUser = async () => {
            const userDocRef = doc(db, "user", id);

            targetUserUnsubscribe = onSnapshot(userDocRef, (docSnapshot) => {
                if (docSnapshot.exists()) {
                    setTargetUser(docSnapshot.data());
                } else {
                    console.log("No such document!");
                }
            }, (error) => {
                console.error("Error getting document:", error);
            });
        }
        const fetchRoom = async () => {
            const chatRoomQuery = query(
                userChatRoomRef,
                where("userDocRef", "==", doc(db, "user", user.id.toString())),
            );

            roomUnsubscribe = onSnapshot(chatRoomQuery, (snapshot) => {
                console.log("DEBUG CHAT 1")
                const chatRoomDocRefs = []
                snapshot.forEach((doc) => {
                    let chatRoomDocRef = doc.data().chatRoomDocRef
                    if (chatRoomDocRef !== null && chatRoomDocRef !== undefined) {
                        chatRoomDocRefs.push(chatRoomDocRef)
                        console.log("DEBUG: " + chatRoomDocRef)
                    }
                })
                console.log("DEBUG CHAT 2: " + chatRoomDocRefs.length)

                if (chatRoomDocRefs.length !== 0 && chatRoomDocRefs !== undefined && chatRoomDocRefs !== null) {
                    chatRoomDocRefs.forEach((room) => console.log(room.id))
                    console.log("DEBUG CHAT 4")
                    const queryUserChatRooms = query(
                        userChatRoomRef,
                        where("chatRoomDocRef", "in", chatRoomDocRefs),
                        where("userDocRef", "==", doc(db, "user", id.toString()))
                    );

                    const unsubscribe = onSnapshot(queryUserChatRooms, (querySnapshot) => {
                        if (!querySnapshot.empty) {
                            console.log("DEBUG CHAT 5 IN queryUserChatRooMS")
                            const doc = querySnapshot.docs[0];
                            if (doc.exists()) {
                                console.log("DEBUG CHAT 6 - DONE")
                                getDoc(doc.data().chatRoomDocRef).then((chatRoomSnapshot) => {
                                    if (chatRoomSnapshot.exists()) {
                                        setRoom({ ...chatRoomSnapshot.data(), id: chatRoomSnapshot.id });
                                    } else {
                                        console.log("No chat room!");
                                    }
                                })
                            } else {
                                console.log("No user chat room!");
                            }
                        }
                    }, (error) => {
                        console.error("Error getting documents:", error);
                    });
                    return () => unsubscribe();
                }
            })
        }

        init()
        if (id !== undefined) {
            fetchTargetUser()
            fetchRoom()
        }

        return () => {
            if (targetUserUnsubscribe) {
                targetUserUnsubscribe()
            }
            if (roomUnsubscribe) {
                roomUnsubscribe()
            }
        };
    }, [id])

    useEffect(() => {
        let messageUnsubscribe
        let typingUnsubscribe

        const fetchMessage = async (documentId) => {
            const queryMessages = query(
                messageRef,
                where("chatRoomDoc", "==", doc(db, "chatRoom", documentId)),
                orderBy("createdAt")
            )
            messageUnsubscribe = onSnapshot(queryMessages, (snapshot) => {
                let messages = [];
                snapshot.forEach((doc) => {
                    messages.push({ ...doc.data(), id: doc.id });
                })
                setMessages(messages)
            })
        }

        const fetchTyping = async() => {
            const typingDocRef = doc(db, "typing", `typing_${targetUser.userId}_${room.id}`)
            if (typingDocRef !== null && typingDocRef !== undefined) {
                typingUnsubscribe = onSnapshot(typingDocRef, (typingSnapshot) => {
                    if (typingSnapshot.exists()) {
                        setTargetTyping(typingSnapshot.data().typing);
                    }
                })
            }
        }

        if (room !== null) {
            fetchMessage(room.id);
        } 

        if (room !== null && user !== null) {
            fetchTyping();
        }

        return () => {
            if (messageUnsubscribe) {
                messageUnsubscribe()
            }
            if (typingUnsubscribe) {
                typingUnsubscribe()
            }
        }
    }, [room, targetUser])

    const setMessageSeen = async (message) => {
        try {
            await authAPI().put(endpoints['seen'] + `/${message.id}`, {})
        } catch (ex) {
        }
    }

    let typingTimeout = null;
    const onTyping = async () => {
        if (room !== null && user !== null) {
            if (!typing) {
                setTyping(true);
                await authAPI().put(endpoints['in_typing'] + `/typing_${user.id}_${room.id}`, {})
            }
            clearTimeout(typingTimeout);
        }
    }

    const stopTyping = async () => {
        if (room !== null && user !== null) {
            clearTimeout(typingTimeout);
            if (typing) {
                typingTimeout = setTimeout(async() => {
                    setTyping(false)
                    await authAPI().put(endpoints['stop_typing'] + `/typing_${user.id}_${room.id}`, {})
                }, 1000);
            }
        }
    }

    return (
        <div className="chat-wrapper">
            <div className='chat'>
                <div className="target-user">
                    {targetUser &&
                        <div className="target-user-wrapper">
                            <div className="photoURL">
                                <img src={targetUser.photoUrl} alt="" />
                                {targetUser.activeStatus === "online" &&
                                    <div className="user-status-online"></div>
                                }
                            </div>
                            <div className="user-status">
                                <div className="room-info">
                                    <div className="displayName">{targetUser.displayName}</div>
                                </div>
                                {targetUser.activeStatus === "online" ?
                                    <div className="status">Đang hoạt động</div> :
                                    <div className="status">Hoạt động <Moment fromNow ago>{targetUser.updatedAt.toDate()}</Moment>
                                    </div>
                                }
                            </div>
                        </div>
                    }
                </div>
                <div className="chat-content">
                    {id !== undefined ?
                        <>
                            {messages.length !== 0 && messages.map((message, index) =>
                                <div className="message-item" key={message.id}>
                                    {message.userDoc.id === user.id.toString() ?
                                        <>
                                            <OverlayTrigger
                                                overlay={(props) => (
                                                    <Tooltip {...props}>
                                                        <Moment format="YYYY/MM/DD hh:mm:ss" fromNow>{message.createdAt.toDate()}</Moment>
                                                    </Tooltip>
                                                )}
                                                placement="left">
                                                <div className="own-message message">
                                                    {message.content}
                                                </div>
                                            </OverlayTrigger>
                                            {((index + 1 !== messages.length && messages[index + 1].userDoc.id === user.id.toString()
                                                && !messages[index + 1].seen && message.seen) ||
                                                (index + 1 === messages.length && message.userDoc.id === user.id.toString()
                                                    && message.seen)) &&
                                                <div className='message-status'>
                                                    <OverlayTrigger
                                                        overlay={(props) => (
                                                            <Tooltip {...props}>
                                                                {targetUser.displayName} đã xem <Moment fromNow>{message.updatedAt.toDate()}</Moment>
                                                            </Tooltip>
                                                        )}
                                                        placement="left">
                                                        <img src={targetUser.photoUrl}></img>
                                                    </OverlayTrigger>
                                                </div>
                                            }
                                        </>
                                        :
                                        <OverlayTrigger
                                            overlay={(props) => (
                                                <Tooltip {...props}>
                                                    <Moment format="YYYY/MM/DD hh:mm:ss" fromNow>{message.createdAt.toDate()}</Moment>
                                                </Tooltip>
                                            )}
                                            placement="right">
                                            {!message.seen ? <InView onChange={() => setMessageSeen(message)}>
                                                {({ ref }) => (
                                                    <div ref={ref} className="orther-message message">
                                                        {message.content}
                                                    </div>
                                                )}
                                            </InView>
                                                :
                                                <div className="orther-message message">
                                                    {message.content}
                                                </div>}
                                        </OverlayTrigger>
                                    }
                                </div>
                            )}
                            {messages.length !== 0 && messages[messages.length - 1].userDoc.id === user.id.toString() && !messages[messages.length - 1].seen &&
                                <div className='message-status'>Đã gửi <Moment fromNow ago>{messages[messages.length - 1].createdAt.toDate()}</Moment></div>
                            }
                            {/* <div className="typing">
                                {targetTyping.toString()}
                            </div> */}
                            {targetTyping && <div className="typing">
                                 <Typing/>
                            </div>}
                            <div id='scroll' ref={bottomRef} />
                        </>
                        :
                        <div className='empty'>
                            <WestIcon /> Chọn người dùng hoặc phòng chat để bắt đầu nhắn tin!
                        </div>
                    }
                </div>
                {id !== undefined && targetUser &&
                    <div className="chat-input">
                        <form className='form' onSubmit={handleSubmit}>
                            {/* <div className='btn-image'>
                                <label for="file-input">
                                    <ImageIcon/>
                                </label>
                                <input id="file-input" multiple type="file" accept="image/png, image/jpeg"/>
                            </div> */}
                            <input onChange={(e) => setNewMessage(e.target.value)} onKeyDown={onTyping} onKeyUp={stopTyping} type='text' placeholder='Nhập tin nhắn' value={newMessage} />
                            <button className='btn btn-chat' type='submit'><SendIcon /></button>
                        </form>
                    </div>}
            </div>
        </div>
    )
}
