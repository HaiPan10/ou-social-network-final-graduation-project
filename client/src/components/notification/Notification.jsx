import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import './notification.scss'
import CircleNotificationsIcon from '@mui/icons-material/CircleNotifications';
import { useContext, useEffect, useState } from 'react';
import { NotificationItem } from './NotificationItem';
import { db } from '../../configs/firebase';
import { collection, doc, onSnapshot, orderBy, query, where } from 'firebase/firestore';
import { AuthContext } from '../../context/AuthContext';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { authAPI, endpoints } from '../../configs/Api';
import { Link } from 'react-router-dom';

export const Notification = () => {
    const [showNotification, setShowNotification] = useState(false)
    const [hideAnimation, setHideAnimation] = useState(false)
    const [privateNotifications, setPrivateNotifications] = useState([])
    const [publicNotifications, setPublicNotifications] = useState([])
    const [user, dispatch] = useContext(AuthContext)
    const [notificationMenu, setNotificationMenu] = useState('private')
    const [hidePrivateNotificationAnimation, setHidePrivateNotificationAnimation] = useState(false)
    const [hidePublicNotificationAnimation, setHidePublicNotificationAnimation] = useState(true)
    const [menuUI, setMenuUI] = useState('private')
    const [unSeen, setUnSeen] = useState([])

    const toggleNotification = () => {
        if (!showNotification) {
            setShowNotification(true)
        } else {
            setHideAnimation(true)
            setTimeout(() => {
                setShowNotification(false);
                setHideAnimation(false);
            }, 200);
        }
    }

    useEffect(() => {
        let privateNotificationUnsubscribe
        let publicNotificationUnsubscribe
        const fetchPrivateNotification = async() => {
            const notificationRef = collection(db, "notification")
            const notificationQuery = query(
                notificationRef,
                where("ownerUserDocRef", "==", doc(db, "user", user.id.toString())),
                orderBy ("createdAt", "desc")
            )
            privateNotificationUnsubscribe = onSnapshot(notificationQuery, (snapshot) => {
                let list = [];
                snapshot.forEach((doc) => {
                    if (doc.data().notificationType == "reaction") {
                        if (!list.some(item => item.notificationType == "reaction" && item.postId == doc.data().postId)) {
                            list.push({ ...doc.data(), id: doc.id });
                        }
                    } else {
                        list.push({ ...doc.data(), id: doc.id });
                    }
                })
                console.log(list)
                setPrivateNotifications(list)
            })
        }

        const fetchPublicNotification = async() => {
            const notificationRef = collection(db, "notification")
            const notificationQuery = query(
                notificationRef,
                where("ownerUserDocRef", "==", doc(db, "user", "0")),
                orderBy ("createdAt", "desc")
            )
            publicNotificationUnsubscribe = onSnapshot(notificationQuery, (snapshot) => {
                let list = [];
                snapshot.forEach((doc) => {
                    list.push({ ...doc.data(), id: doc.id });
                })
                setPublicNotifications(list)
            })
        }

        fetchPrivateNotification()
        fetchPublicNotification()

        return () => {
            if (privateNotificationUnsubscribe) {
                privateNotificationUnsubscribe()
            }
            if (publicNotificationUnsubscribe) {
                publicNotificationUnsubscribe()
            }
        }
    }, [notificationMenu])

    useEffect(() => {
        setUnSeen(privateNotifications.filter(notification => notification.seen === false))
    }, [privateNotifications])

    const toggleNotificationMenu = (menu) => {
        if (menu === 'private') {
            setMenuUI('private')
            setHidePrivateNotificationAnimation(false)
            setHidePublicNotificationAnimation(true)
            setTimeout(() => {
                setNotificationMenu('private');
            }, 200);
        } else {
            setMenuUI('public')
            setHidePrivateNotificationAnimation(true)
            setHidePublicNotificationAnimation(false)
            setTimeout(() => {
                setNotificationMenu('public');
            }, 200);
        }
    }

    const seenNotification = async(notificationId) => {
        await authAPI().put(endpoints['seen_notification'] + `/${notificationId}`, {})
    }

    const setSeenAll = () => {
        if (menuUI === 'private' && unSeen !== 0) {
            unSeen.forEach((notification) => {
                seenNotification(notification.id)
            })
        }
    }

    return (
        <div className='notification'>
            {showNotification && <div className={`notificationContainer animate__animated  ${
                showNotification ? "animate__fadeInUp" : ""} ${
                    hideAnimation ? "animate__fadeOutDown" : ""}`}>
                <div className="notificationTitle"><CircleNotificationsIcon /> Thông báo </div>
                <div className="notificationMenu">
                    <div className={`menuItem private-notification ${menuUI === 'private' ? 'active' : ''}`} onClick={() => toggleNotificationMenu('private')}>Thông báo riêng</div>
                    <div className={`menuItem public-notification ${menuUI === 'public' ? 'active' : ''}`} onClick={() => toggleNotificationMenu('public')}>Bảng tin chung</div>
                </div>
                <div className={`viewAll ${menuUI === 'private' && unSeen.length !== 0 ? '' : 'disable'} `} onClick={setSeenAll}><CheckCircleIcon/> Đánh dấu toàn bộ</div>
                <div className="notificationContent">
                    {notificationMenu === 'private' && <div className={`privateNotification container animate__animated ${!hidePrivateNotificationAnimation ? 'animate__fadeInLeft' : 'animate__fadeOutLeft'} `}>
                        {privateNotifications.length !== 0 && privateNotifications.map((notification) =>
                            <NotificationItem toggleNotification={toggleNotification} type="private" notification={notification} key={notification.id}/>
                        )}
                    </div>}
                    {notificationMenu === 'public' && <div className={`publicNotification container animate__animated  ${!hidePublicNotificationAnimation ? 'animate__fadeInRight' : 'animate__fadeOutRight'} `}>
                        {publicNotifications.length !== 0 && publicNotifications.map((notification) =>
                            <NotificationItem toggleNotification={toggleNotification} type={'public'} notification={notification} key={notification.id}/>
                        )}
                    </div>}
                </div>
            </div>}
            <div className="notificationIcon" onClick={() => toggleNotification()}>
                <NotificationsNoneIcon />
                {unSeen.length !== 0 && <div className="unSeenCount">{unSeen.length}</div>}
            </div>
        </div>
    )
}
