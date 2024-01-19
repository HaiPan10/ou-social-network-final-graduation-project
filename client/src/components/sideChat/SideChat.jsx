import { useContext, useEffect, useState } from 'react'
import './sideChat.scss'
import MessageIcon from '@mui/icons-material/Message';
import { AuthContext } from '../../context/AuthContext';
import { collection, doc, getDoc, getDocs, onSnapshot, orderBy, query, where } from 'firebase/firestore';
import { db } from '../../configs/firebase';
import Loading from '../Loading';
import { Link, useParams } from 'react-router-dom';
import { Room } from './Room';
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import ChatIcon from '@mui/icons-material/Chat';

export const SideChat = (props) => {
    const [users, setUsers] = useState([])
    const [userChatRooms, setUserChatRooms] = useState([])
    const [user, userDispatch] = useContext(AuthContext)
    const userRef = collection(db, "user")
    const userChatRoomRef = collection(db, 'userChatRoom')
    const chatRoomRef = collection(db, 'chatRoom')
    const [searchContent, setSearchContent] = useState('')
    const [chosenMenu, setChosenMenu] = useState('chat')
    const { id } = useParams()


    useEffect(() => {
        const fetchUsers = async () => {
            const queryUsers = query(
                userRef,
                where("userId", "!=", user.id),
            )
            const unsuscribe = onSnapshot(queryUsers, (snapshot) => {
                let userList = []
                snapshot.forEach((doc) => {
                    userList.push({ ...doc.data(), id: doc.id })
                })
                setUsers(userList)
            })

            return () => unsuscribe()
        }

        const fetchChatRooms = async () => {
            const chatRoomQuery = query(
                userChatRoomRef,
                where("userDocRef", "==", doc(db, "user", user.id.toString())),
            );

            const unsuscribe = onSnapshot(chatRoomQuery, (snapshot) => {
                const chatRoomDocRefs = []

                snapshot.forEach((doc) => {
                    let chatDocRef = doc.data().chatRoomDocRef
                    if (chatDocRef !== null && chatDocRef !== undefined) {
                        chatRoomDocRefs.push(chatDocRef)
                    }
                })

                if (chatRoomDocRefs.length !== 0 && chatRoomDocRefs !== undefined && chatRoomDocRefs !== null) {
                    const queryUserChatRooms = query(
                        userChatRoomRef,
                        where("chatRoomDocRef", "in", chatRoomDocRefs),
                        where("userDocRef", "!=", doc(db, "user", user.id.toString()))
                    );

                    const unsuscribe = onSnapshot(queryUserChatRooms, async (subSnapshot) => {
                        let userChatRoomsList = []
                        subSnapshot.forEach((doc) => {
                            const chatRoomDocRef = doc.data().chatRoomDocRef;
                            if (chatRoomDocRef !== null && chatRoomDocRef !== undefined) {
                                const unsubscribe = onSnapshot(chatRoomDocRef, (docSnapshot) => {
                                    if (docSnapshot.exists()) {
                                        const updatedAt = docSnapshot.data().updatedAt.toDate();
                                        const existingIndex = userChatRoomsList.findIndex(item => item.id === doc.id)
                                        if (existingIndex !== -1) {
                                            userChatRoomsList[existingIndex] = { ...doc.data(), updatedAt, id: doc.id }
                                        } else {
                                            userChatRoomsList.push({ ...doc.data(), updatedAt, id: doc.id });
                                        }
                                        userChatRoomsList.sort((a, b) => b.updatedAt - a.updatedAt)
                                        setUserChatRooms([...userChatRoomsList])
                                    } else {
                                        console.log("No such document!");
                                    }
                                }, (error) => {
                                    console.error("Error getting document:", error);
                                });
                                return () => unsubscribe()
                            }
                        });
                    })
                    return () => unsuscribe()
                }

            })

            return () => unsuscribe();
        }

        fetchUsers()
        fetchChatRooms()
    }, [])

    useEffect(() => {
        setChosenMenu('chat')
    }, [id])

    const filteredUsers = users.filter(user =>
        user.displayName.toLowerCase().includes(searchContent.toLowerCase())
    );

    return (
        <div className="side-chat">
            <div className="title"><MessageIcon /> Nhắn tin</div>
            <div className="side-menu">
                <div className={`side-menu-item chat-menu ${chosenMenu === 'chat' ? 'active' : ''}`} onClick={() => setChosenMenu('chat')}><ChatIcon /> Trò chuyện</div>
                <div className={`side-menu-item people-menu ${chosenMenu === 'people' ? 'active' : ''}`} onClick={() => setChosenMenu('people')}> <PeopleAltIcon /> Mọi người</div>
            </div>
            {chosenMenu === 'chat' &&
                <>
                    <hr />
                    {userChatRooms.length === 0 ? <div className='loading'></div> :
                        <div className='item-wrapper'>
                            {userChatRooms.map((userChatRoom) => {
                                return (
                                    <Room currentId={id} userChatRoom={userChatRoom} key={userChatRoom.id} />
                                )
                            })}
                        </div>
                    }
                </>
            }
            {chosenMenu === 'people' &&
                <>
                    <div className="searchbar">
                        <div className="input-group">
                            <input type="search" value={searchContent} onChange={(e) => setSearchContent(e.target.value)} className="form-control" placeholder="Tên người dùng" />
                        </div>
                    </div>
                    <hr />
                    {users.length === 0 ? <div className='loading'><Loading /></div> :
                        <div className='item-wrapper'>
                            {filteredUsers.map((user) => {
                                return (
                                    <Link to={`/chat/${user.userId}`} className='turnoff-link-style' key={user.userId}>
                                        <div className="chat-room-item" >
                                            <div className="avatar">
                                                <img src={user.photoUrl} alt="" />
                                                {user.activeStatus === "online" &&
                                                    <div className='user-status-online'></div>
                                                }
                                            </div>
                                            <div className="chat-info">
                                                <div className="displayName">{user.displayName}</div>
                                            </div>
                                        </div>
                                    </Link>
                                )
                            })}
                        </div>
                    }
                </>
            }
        </div>
    )
}
