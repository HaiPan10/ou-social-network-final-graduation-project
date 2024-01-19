import React, { useContext, useEffect, useRef, useState } from 'react'
import { AuthContext } from '../../context/AuthContext'
import './leftBar.scss'
import { Link } from 'react-router-dom'
import HomeIcon from '@mui/icons-material/Home';
import SettingsIcon from '@mui/icons-material/Settings';
import { DarkModeContext } from '../../context/DarkModeContext';
import LogoutOutlinedIcon from '@mui/icons-material/LogoutOutlined';
import DarkModeOutlinedIcon from "@mui/icons-material/DarkModeOutlined";
import LightModeOutlinedIcon from '@mui/icons-material/LightModeOutlined';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import UploadPost from '../postLayout/UploadPost';
import { ReloadContext } from '../../context/ReloadContext';
import LockPersonIcon from '@mui/icons-material/LockPerson';
import ChangePassword from '../changePassword/ChangePassword';
import SearchIcon from '@mui/icons-material/Search';
import { Search } from '../search/Search';
import MessageIcon from '@mui/icons-material/Message';
import { SideChat, sideChat } from '../sideChat/SideChat';
import { ChatContext, LeftBarContext } from '../../context/LeftBarContext';
import 'animate.css'
import { collection, doc, onSnapshot, query, where } from 'firebase/firestore';
import { db } from '../../configs/firebase';
import { SocketContext } from '../../context/SocketContext';

export const LeftBar = (props) => {
  const [user, dispatch] = useContext(AuthContext)
  const { toggle } = useContext(DarkModeContext)
  const { darkMode } = useContext(DarkModeContext)
  const [uploadPostShow, setUploadPostShow] = useState(false)
  const [dropdownVisible, setDropdownVisible] = useState(false)
  const dropdownRef = useRef(null)
  const { reloadData } = useContext(ReloadContext)
  const [changePasswordShow, setChangePasswordShow] = useState(false)
  const { showChat } = useContext(LeftBarContext)
  const { setShowChat } = useContext(LeftBarContext)
  const { smallLeftBar } = useContext(LeftBarContext)
  const { setSmallLeftBar } = useContext(LeftBarContext)
  const { showSearch } = useContext(LeftBarContext)
  const { setShowSearch } = useContext(LeftBarContext)
  const { currentMenu } = useContext(LeftBarContext)
  const [unSeenTotal, setUnSeenTotal] = useState(0)
  const [socketClient] = useContext(SocketContext)

  const handleClickOutside = (event) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
      setDropdownVisible(false)
    }

  }

  useEffect(() => {
    const addEvent = () => {
      document.addEventListener('mousedown', handleClickOutside)
      return document.removeEventListener('mousedown', handleClickOutside)
    }

    const fetchChatRooms = async () => {
      const chatRoomQuery = query(
        collection(db, 'userChatRoom'),
        where("userDocRef", "==", doc(db, "user", user.id.toString())),
      );

      const unsuscribe = onSnapshot(chatRoomQuery, (snapshot) => {
        let unSeenList = []

        snapshot.forEach(async (doc) => {
          const chatRoomDocRef = doc.data().chatRoomDocRef
          if (chatRoomDocRef !== null && chatRoomDocRef !== undefined) {
            const unsubscribeRoom = onSnapshot(chatRoomDocRef, (chatRoomSnapShot) => {
              const messageDocRef = chatRoomSnapShot.data().latestMessageDoc
              if (messageDocRef !== null && messageDocRef !== undefined) {
                const unsubscribeLatestMessage = onSnapshot(messageDocRef, (messageSnapShot) => {
                  if (messageSnapShot.data() !== null && messageSnapShot.data() !== undefined) {
                    if (!messageSnapShot.data().seen && messageSnapShot.data().userDoc.id !== user.id.toString()) {

                      const existingIndex = unSeenList.findIndex(item => item.id === chatRoomSnapShot.id)
                      if (existingIndex !== -1) {
                        unSeenList[existingIndex] = { ...messageSnapShot.data(), id: chatRoomSnapShot.id }
                      } else {
                        unSeenList.push({ ...messageSnapShot.data(), id: chatRoomSnapShot.id })
                      }
                      setUnSeenTotal(unSeenList.length)
                    } else {
                      // console.log(chatRoomSnapShot.id + " SEEN !")
                      unSeenList = unSeenList.filter(item => item.id !== chatRoomSnapShot.id)
                      // console.log(unSeenList.length)
                      setUnSeenTotal(unSeenList.length)
                    }
                  }
                })
                return () => unsubscribeLatestMessage()
              }
            })
            return () => unsubscribeRoom()
          }
        })
      })

      return () => unsuscribe();
    }

    addEvent()
    fetchChatRooms()
  }, [])

  const toggleDropdown = () => {
    setDropdownVisible(!dropdownVisible);
  }

  const logout = (evt) => {
    evt.preventDefault()
    socketClient.disconnect()

    dispatch({
      "type": "LOGOUT"
    })
  }

  const toggleSearch = () => {
    if (!showSearch) {
      setSmallLeftBar(true)
    } else if (!showChat) {
      setSmallLeftBar(false)
    }
    setShowSearch(!showSearch)
  }

  const toggleChat = () => {
    setSmallLeftBar(true)
    setShowChat(true)
    setShowSearch(false)
  }

  const navigate = (menuName) => {
    reloadData()
    setShowSearch(false)
    setShowChat(false)
    setSmallLeftBar(false)
  }

  return (
    <>
      <div className='leftBar' style={{ flex: "2", width: smallLeftBar && !showChat ? "180%" : "100%" }}>
        {/* <div className='leftBar' style={{flex: smallLeftBar ? "3" : "2"}}> */}
        <div className='container' style={{ width: smallLeftBar ? "20%" : "100%" }}>
          <div className='menu'>
            <div className='menu-top'>
              <div className='item'>
                <div className="logo" >
                  <img />
                </div>
              </div>
              <Link to="/" className='turnoff-link-style'>
                <div className={currentMenu === 'home' ? 'item active' : 'item'} onClick={() => navigate("home")} style={{ justifyContent: smallLeftBar ? "center" : "" }}>
                  <HomeIcon className="icon" />
                  <span style={{ display: smallLeftBar ? "none" : "block" }}>Trang chủ</span>
                </div>
              </Link>
              <Link to={`/profile/${user.id}`} className='turnoff-link-style' >
                <div className={currentMenu === 'profile' ? 'user item active' : 'user item'} onClick={() => navigate("profile")} style={{ justifyContent: smallLeftBar ? "center" : "" }}>
                  <img src={user.avatar} alt="" />
                  <span style={{ display: smallLeftBar ? "none" : "block" }}>Trang cá nhân</span>
                </div>
              </Link>
              <div className="item" onClick={() => setUploadPostShow(true)} style={{ justifyContent: smallLeftBar ? "center" : "" }}>
                <AddCircleOutlineIcon className='icon' />
                <span style={{ display: smallLeftBar ? "none" : "block" }}>Đăng bài</span>
              </div>
              <Link to={`/profile/1`} className='turnoff-link-style' >
                <div className={currentMenu === 'admin' ? 'user item active' : 'user item'} onClick={() => navigate("admin")} style={{ justifyContent: smallLeftBar ? "center" : "" }}>
                  <img src="https://ou.edu.vn/wp-content/uploads/2019/01/OpenUniversity.png" alt="" />
                  <span style={{ display: smallLeftBar ? "none" : "block" }} >Trang quản trị viên</span>
                </div>
              </Link>
              <div className={showSearch ? 'search-active item ' : 'item'} onClick={toggleSearch} style={{ justifyContent: smallLeftBar ? "center" : "" }}>
                <SearchIcon className='icon' />
                <span style={{ display: smallLeftBar ? "none" : "block" }}>Tìm kiếm</span>
              </div>
              <Link to={`/chat`} className='turnoff-link-style' >
                <div className={currentMenu === 'chat' ? 'item active' : 'item'} onClick={() => toggleChat()} style={{ justifyContent: smallLeftBar ? "center" : "" }}>
                  <div className="icon-wrapper">
                    <MessageIcon className='icon' />
                    {unSeenTotal > 0 && <div className="messageUnseenTotal">{unSeenTotal}</div>}
                  </div>
                  <span style={{ display: smallLeftBar ? "none" : "block" }}>Nhắn tin</span>
                </div>
              </Link>
              <UploadPost show={uploadPostShow} onHide={() => setUploadPostShow(false)} setUploadPostShow={setUploadPostShow} />
            </div>
            <div className='menu-bottom'>
              <div className="dropdown" ref={dropdownRef}>
                <div className="setting item" onClick={toggleDropdown} style={{ justifyContent: smallLeftBar ? "center" : "" }}>
                  <div className='icon-wrapper'>
                    <SettingsIcon className='icon' />
                    {props.status === "PASSWORD_CHANGE_REQUIRED" && <div className='piority' />}
                  </div>
                  {!(smallLeftBar) && <span>Cài đặt</span>}
                </div>
                {dropdownVisible && <div className="dropdown-content">
                  <div className='icon-wrapper' onClick={logout} style={{ justifyContent: smallLeftBar ? "center" : "" }}> <LogoutOutlinedIcon /> <span style={{ display: smallLeftBar ? "none" : "block" }}>Đăng xuất</span></div>
                  <div className='icon-wrapper' onClick={() => setChangePasswordShow(true)} style={{ justifyContent: smallLeftBar ? "center" : "" }}>
                    <div > <LockPersonIcon />
                      {(smallLeftBar) && props.status === "PASSWORD_CHANGE_REQUIRED" && <div className='piority-setting' >
                        <div className="piority-item"></div>
                      </div>}
                      <span style={{ display: smallLeftBar ? "none" : "" }}>Đổi mật khẩu</span>
                    </div>
                    {props.status === "PASSWORD_CHANGE_REQUIRED" && <div className='piority-setting'>
                      <div className="piority-item"></div>
                    </div>}
                  </div>
                  {!darkMode ? <div className='icon-wrapper' onClick={toggle} style={{ justifyContent: smallLeftBar ? "center" : "" }}> <DarkModeOutlinedIcon /> <span style={{ display: smallLeftBar ? "none" : "" }}>Chế độ tối</span></div> :
                    <div className='icon-wrapper' onClick={toggle} style={{ justifyContent: smallLeftBar ? "center" : "" }}> <LightModeOutlinedIcon /> <span style={{ display: smallLeftBar ? "none" : "" }}>Chế độ sáng</span></div>}
                </div>}
                <ChangePassword status={props.status} show={changePasswordShow} setChangePasswordShow={setChangePasswordShow} />
              </div>
            </div>
          </div>
        </div>

        {smallLeftBar &&
          <div className={`animate__animated ${!showSearch ? "animate__fadeOutLeft" : "animate__fadeInLeft"
            } ${showChat ? "pressDiv" : "appendDiv"}`}>
            <Search />
          </div>}

        {smallLeftBar && showChat && <div className="appendDiv">
          <SideChat status={props.status} />
        </div>}
      </div>
    </>
  )
}
