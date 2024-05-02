import React, { useContext, useEffect, useState } from 'react'
import { AuthContext } from '../../context/AuthContext'
import './notificationItem.scss'
import FavoriteRoundedIcon from '@mui/icons-material/FavoriteRounded';
import ThumbUpRoundedIcon from '@mui/icons-material/ThumbUpRounded';
import SentimentVerySatisfiedRoundedIcon from '@mui/icons-material/SentimentVerySatisfiedRounded';
import TextsmsOutlinedIcon from "@mui/icons-material/TextsmsOutlined";
import PollIcon from '@mui/icons-material/Poll';
import EmailIcon from '@mui/icons-material/Email';
import { onSnapshot } from 'firebase/firestore';
import Moment from 'react-moment';
import FeedIcon from '@mui/icons-material/Feed';
import { authAPI, endpoints } from '../../configs/Api';
import { Link } from 'react-router-dom';
import ReplyIcon from '@mui/icons-material/Reply';

export const NotificationItem = ({ notification, type, toggleNotification }) => {
    const [user, dispatch] = useContext(AuthContext)
    const [interactUser, setInteractUser] = useState(null)
    const [postModalShow, setPostModalShow] = useState(false)
    const [totalReaction, setTotalReaction] = useState(0)

    useEffect(() => {
        const fetchInteractUser = async () => {
            const userDocRef = notification.interactUserDocRef;

            const unsubscribe = onSnapshot(userDocRef, (docSnapshot) => {
                if (docSnapshot.exists()) {
                    setInteractUser(docSnapshot.data());
                } else {
                    console.log("No such document!");
                }
            }, (error) => {
                console.error("Error getting document:", error);
            });

            return () => unsubscribe();
        }

        const countReaction = async () => {
            let res = await authAPI().get(endpoints['count_reactions'] + `/${notification.postId}`)
            if (res.status === 200) {
                setTotalReaction(res.data)
            }
        }    

        fetchInteractUser()
        if (notification.notificationType == "reaction") {
            countReaction()
        }
    }, [])

    const seenNotification = async () => {
        await authAPI().put(endpoints['seen_notification'] + `/${notification.id}`, {})
    }
    const viewNotification = () => {
        if (!notification.seen) {
            seenNotification()
        }
        toggleNotification()
    }

    return (
        <>
            <Link onClick={viewNotification} style={{ textDecoration: 'none' }} to={`/post/${notification.postId}`} state={{focusComment: notification.commentId, focusParent: notification.parentCommentId}}>
                <div className={`notificationItem ${!notification.seen ? 'active' : ''}`}>
                    {interactUser && <div className="notificationImage">
                        <img src={interactUser.photoUrl} />
                        <div className="notificationSubIcon">
                            {notification.notificationType === "reaction" && (
                                <>
                                    {notification.reactionId === 1 && <ThumbUpRoundedIcon />}
                                    {notification.reactionId === 2 && <SentimentVerySatisfiedRoundedIcon />}
                                    {notification.reactionId === 3 && <FavoriteRoundedIcon />}
                                </>
                            )}
                            {notification.notificationType === "comment" && (
                                <>
                                    <TextsmsOutlinedIcon />
                                </>
                            )}
                            {notification.notificationType === "survey" && (
                                <>
                                    <PollIcon />
                                </>
                            )}
                            {notification.notificationType === "invitation" && (
                                <>
                                    <EmailIcon />
                                </>
                            )}
                            {notification.notificationType === "post" && (
                                <>
                                    <FeedIcon />
                                </>
                            )}
                            {notification.notificationType === "reply" && (
                                <>
                                    <ReplyIcon />
                                </>
                            )}
                        </div>
                    </div>}
                    {interactUser && <div className="textWrapper">
                        <div className="notificationText">
                            {notification.notificationType === "reaction" && (
                                <>
                                    <b>{interactUser.displayName}</b>
                                    {totalReaction - 1 != 0 && <span>
                                    {' '}và {totalReaction - 1} người khác{' '}
                                    </span>}
                                    {' '}đã bày tỏ cảm xúc với bài viết của bạn: <b>{notification.content}</b>
                                </>
                            )}
                            {notification.notificationType === "comment" && (
                                <>
                                    <b>{interactUser.displayName}</b> đã bình luận về bài viết của bạn: <b>{notification.content}</b>
                                </>
                            )}
                            {notification.notificationType === "survey" && (
                                <>
                                    <b>{interactUser.displayName}</b> đã tiến hành cuộc khảo sát: <b>{notification.content}</b>
                                </>
                            )}
                            {notification.notificationType === "invitation" && (
                                <>
                                    {type === 'private' ?
                                        <><b>{interactUser.displayName}</b> đã mời bạn tham gia một sự kiện: <b>{notification.content}</b></>
                                        :
                                        <><b>{interactUser.displayName}</b> đã mời tất cả mọi người tham gia một sự kiện: <b>{notification.content}</b></>
                                    }
                                </>
                            )}
                            {notification.notificationType === "post" && (
                                <>
                                    <b>{interactUser.displayName}</b> đã đăng bài đăng mới lên bảng tin: <b>{notification.content}</b>
                                </>
                            )}
                            {notification.notificationType === "reply" && (
                                <>
                                    <b>{interactUser.displayName}</b> đã phản hồi bình luận của bạn: <b>{notification.content}</b>
                                </>
                            )}
                        </div>
                        {type === 'private' ?
                            <div className="notificationTimestamp"><Moment fromNow>{notification.createdAt.toDate()}</Moment></div>
                            :
                            <div className="notificationTimestamp"><Moment format="DD/MM/YYYY hh:mm:ss">{notification.createdAt.toDate()}</Moment></div>
                        }
                    </div>}
                </div>
            </Link>
        </>
    )
}
