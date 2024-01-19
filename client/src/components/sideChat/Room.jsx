import { useEffect, useState } from 'react'
import './room.scss'
import { doc, getDoc, onSnapshot } from 'firebase/firestore'
import { Link } from 'react-router-dom'
import Moment from 'react-moment'
import { db } from '../../configs/firebase'
import { Typing } from '../../pages/chat/Typing'

export const Room = ({ userChatRoom, currentId }) => {
    const [targetUser, setTargetUser] = useState(null)
    const [room, setRoom] = useState(null)
    const [latestMessage, setLatestMessage] = useState(null);
    const [latestMessageOwner, setLatestMessageOwner] = useState(null);
    const [targetTyping, setTargetTyping] = useState(false)

    useEffect(() => {
        const fetchRoom = async () => {
            const chatRoomDocRef = userChatRoom.chatRoomDocRef;

            console.log("DEBUG ROOM 1")
            const unsubscribe = onSnapshot(chatRoomDocRef, (docSnapshot) => {
                if (docSnapshot.exists() && docSnapshot !== null) {
                    console.log("DEBUG ROOM 2")
                    setRoom({ ...docSnapshot.data(), id: docSnapshot.id });

                    let latestMessageRef
                    if (docSnapshot.data().latestMessageDoc !== null && docSnapshot.data().latestMessageDoc !== undefined) {
                        latestMessageRef = doc(db, "message", docSnapshot.data().latestMessageDoc.id)
                    }
                    if (latestMessageRef !== null && latestMessageRef !== undefined) {
                        const unsubscribeLatestMessage = onSnapshot(latestMessageRef, (messageSnapshot) => {
                            if (messageSnapshot.exists()) {
                                console.log("DEBUG ROOM 3")
                                setLatestMessage({ ...messageSnapshot.data(), id: messageSnapshot.id });
                                getDoc(messageSnapshot.data().userDoc).then((messageOwnerSnapshot) => {
                                    if (messageOwnerSnapshot.exists()) {
                                        console.log("DEBUG ROOM 4")
                                        setLatestMessageOwner({ ...messageOwnerSnapshot.data(), id: messageOwnerSnapshot.id });
                                    } else {
                                        console.log("No latest message!");
                                    }
                                })
                            } else {
                                console.log("No latest message!");
                            }
                        })
    
                        return () => unsubscribeLatestMessage()
                    }
                } else {
                    console.log("No such document!");
                }
            }, (error) => {
                console.error("Error getting document:", error);
            });

            return () => unsubscribe();
        }

        const fetchTargetUser = async () => {
            const userDocRef = userChatRoom.userDocRef;
            console.log("DEBUG ROOM 5")

            const unsubscribe = onSnapshot(userDocRef, (docSnapshot) => {
                if (docSnapshot.exists()) {
                    console.log("DEBUG ROOM 6")
                    setTargetUser(docSnapshot.data());
                } else {
                    console.log("No such document!");
                }
            }, (error) => {
                console.error("Error getting document:", error);
            });

            return () => unsubscribe();
        }

        fetchRoom()
        fetchTargetUser()
    }, [])

    useEffect(() => {
        const fetchTyping = async () => {
            const typingUnsubscribe = onSnapshot(doc(db, "typing", `typing_${targetUser.userId}_${room.id}`), (typingSnapshot) => {
                if (typingSnapshot.exists()) {
                    setTargetTyping(typingSnapshot.data().typing);
                }
            })

            return () => typingUnsubscribe()
        }


        if (targetUser !== null && room !== null) {
            fetchTyping()
        }
    }, [targetUser, room])

    return (
        <div className='room'>
            {targetUser !== null && room !== null && latestMessageOwner !== null && latestMessage !== null &&
                <Link to={`/chat/${targetUser.userId}`} className='turnoff-link-style' key={targetUser.userId}>
                    <div className={currentId === targetUser.userId.toString() ? 'chat-room-item active' : 'chat-room-item'}>
                        <div className='avatar'>
                            <img src={targetUser.photoUrl} alt="" />
                            {targetUser.activeStatus === "online" &&
                                <div className='user-status-online'></div>
                            }
                        </div>
                        <div className="chat-info">
                            <div className="displayName">{targetUser.displayName}</div>
                            <div className="content">
                                <div className='latestMessage'>
                                    {targetTyping ? <Typing /> :
                                        latestMessageOwner.userId !== targetUser.userId ?
                                            <span>Bạn: {latestMessage.content}</span>
                                            :
                                            <span style={{ fontWeight: latestMessage.seen ? "normal" : "bold" }}>{latestMessage.content}</span>

                                    }
                                </div>
                                {!targetTyping && <div className="timeStamp">
                                    <Moment fromNow ago>{latestMessage.createdAt.toDate()}</Moment>
                                </div>}
                            </div>
                        </div>
                    </div>
                </Link>
            }
        </div>
    )
}
