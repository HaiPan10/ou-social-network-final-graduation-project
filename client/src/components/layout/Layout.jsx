import React, { useContext, useEffect, useState } from 'react'
import { AuthContext } from '../../context/AuthContext';
import { Navigate, Outlet } from 'react-router-dom';
import { NavBar } from '../navBar/NavBar';
import { LeftBar } from '../leftBar/LeftBar';
import { RightBar } from '../rightBar/RightBar';
import { authAPI, endpoints, socketUrl } from '../../configs/Api';
import { EmailVerification } from '../../pages/emailVerification/EmailVerification';
import { AccountLocked } from '../../pages/accountLocked/AccountLocked';
import { AccountPending } from '../../pages/accountPending/AccountPending';
import { AccountRejected } from '../../pages/accountRejected/AccountRejected';
import { AuthenBackground } from '../authenBackground/AuthenBackground';
import '../../style.scss'
import './layout.scss'
import { DarkModeContext } from '../../context/DarkModeContext';
import 'react-image-lightbox/style.css';
import { ReloadContext } from "../../context/ReloadContext";
import { LeftBarContext } from '../../context/LeftBarContext';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs'
import { load } from 'react-cookies';
import { SocketContext } from '../../context/SocketContext';

export const Layout = () => {
    const [user, dispatch] = useContext(AuthContext)
    const [status, setStatus] = useState()
    const {darkMode} = useContext(DarkModeContext)
    const { showChat } = useContext(LeftBarContext)
    const [socketClient, setSocketClient] = useState(null)
    
    useEffect(() => {
        const getStatus = async () => {
            try {
                let res = await authAPI().get(endpoints['status'])
                setStatus(res.data)
            } catch (ex) {  
                setStatus("ERROR")
            }
        }

        if (user !== null && socketClient === null) {
            const socket = new SockJS(socketUrl)
            const client = Stomp.over(socket)
            client.debug = null
            client.connect({Authorization: `Bearer ${ load("access-token") }`}, () => {
              setSocketClient(client);
              getStatus()
            })
        } else {
            setStatus("LOGIN")
        }
    }, [user]);

    if (user === null) {
        return <Navigate to="/login" />
    }

    let pageContent;
    if (status === "ACTIVE" || status === "PASSWORD_CHANGE_REQUIRED") {
        pageContent = (
            <SocketContext.Provider value={[socketClient]}>
                <div className={`theme-${darkMode ? "dark" : "light"}`}>
                    <NavBar />
                    <div style={{ display: "flex", position: "relative" }}>
                        <div className='leftBarWrapper' style={{flex: showChat ? "2.88" : "2", position: "sticky", top: "0px", height: "100vh", zIndex: showChat ? "3": "3"}}><LeftBar status={status}/></div>
                        <div style={{flex: showChat ? "6" : "6", position: "relative", zIndex: "2"}}><Outlet/></div>
                        {!showChat && <RightBar />}
                    </div>
                </div>
            </SocketContext.Provider>
        )
    } else if (status === "EMAIL_VERIFICATION_PENDING") {
        pageContent = (
            <div>
                <AuthenBackground/>
                <EmailVerification accountId={user.id}/>
            </div>
        )

    } else if (status === "LOCKED") {
        pageContent = (
            <div>
                <AuthenBackground/>
                <AccountLocked/>
            </div>
        )
    } else if (status === "AUTHENTICATION_PENDING") {
        pageContent = (
            <div>
                <AuthenBackground/>
                <AccountPending/>
            </div>
        )
    } else if (status === "REJECT") {
        pageContent = (
            <div>
                <AuthenBackground/>
                <AccountRejected/>
            </div>
        )
    } else if (status === "ERROR") {
        pageContent = <NavBar/>
    } else if (status === "LOGIN") {
        return <Navigate to="/login" />
    }

    return (
        <div className='layout'>
            {pageContent}
        </div>
    )
}
