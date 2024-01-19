import React, { useContext, useEffect, useRef, useState } from 'react'
import { AuthContext } from '../../context/AuthContext'
import DarkModeOutlinedIcon from "@mui/icons-material/DarkModeOutlined";
import GridViewOutlinedIcon from "@mui/icons-material/GridViewOutlined";
import NotificationsOutlinedIcon from "@mui/icons-material/NotificationsOutlined";
import EmailOutlinedIcon from "@mui/icons-material/EmailOutlined";
import LightModeOutlinedIcon from '@mui/icons-material/LightModeOutlined';
import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import './navBar.scss'
import { DarkModeContext } from '../../context/DarkModeContext';
import LogoutOutlinedIcon from '@mui/icons-material/LogoutOutlined';
import AccountBoxOutlinedIcon from '@mui/icons-material/AccountBoxOutlined';
import { Link } from 'react-router-dom';
import LockPersonIcon from '@mui/icons-material/LockPerson';
import ChangePassword from '../changePassword/ChangePassword';
import { ReloadContext } from '../../context/ReloadContext';
import SupervisorAccountIcon from '@mui/icons-material/SupervisorAccount';
import { authAPI, endpoints } from '../../configs/Api';
import Loading from '../Loading';

export const NavBar = () => {
  const [user, dispatch] = useContext(AuthContext)
  const { toggle } = useContext(DarkModeContext)
  const {darkMode} = useContext(DarkModeContext)
  const [menuVisible, setMenuVisible] = useState(false)
  const [changePasswordShow, setChangePasswordShow] = useState(false)
  const navbarRef = useRef(null)
  const { reloadData } = useContext(ReloadContext)
  const [searchContent, setSearchContent] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [searchResult, setSearchResult] = useState()

  useEffect(() => {
    const search = async () => {
      setIsLoading(true)
      try {
        let res = await authAPI().get(endpoints['search'] + searchContent)
        setSearchResult(res.data)
      } catch (ex) {
      } finally {
        setIsLoading(false)
      }
    }
      
    if (searchContent !== '') {
        search()
    }
  }, [searchContent])

  const handleClickOutside = (event) => {
    if (navbarRef.current && !navbarRef.current.contains(event.target)) {
      setMenuVisible(false)
    }
  }

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [])

  const toggleDropdown = () => {
    setMenuVisible(!menuVisible);
  }

  const logout = (evt) => {
    evt.preventDefault()
    dispatch({
      "type": "LOGOUT",
    })
  }

  return (
    <>
      <div className='navBar'>
        <div className='left'>
          <Link to="/" className='logo-link'>
            <div className='logo' onClick={reloadData}>
              <img />
            </div>
          </Link>
          <div className='search-nav'>
            <input type="search" value={searchContent} onChange={(e) => setSearchContent(e.target.value)} className="form-control" placeholder="Tìm kiếm ở đây" />
            <button type="button" className="btn btn-outline-primary"><SearchOutlinedIcon/></button>
          </div>
        </div>
        <div className='right'>
          {/* <EmailOutlinedIcon/>
          <NotificationsOutlinedIcon/> */}
          <div className="dropdown" ref={navbarRef}>
            <div className='user' onClick={toggleDropdown}>
              <img src={user.avatar} alt="" />
              {/* <span>{user.firstName}</span> */}
            </div>
            {menuVisible && <div className="dropdown-content">
              <div onClick={logout}><LogoutOutlinedIcon/> Đăng xuất</div>
              <div onClick={() =>setChangePasswordShow(true)}><LockPersonIcon/> Đổi mật khẩu</div>
              <Link to={`/profile/${user.id}`} style={{ textDecoration: 'none' }}>
                <div><AccountBoxOutlinedIcon/>Trang cá nhân </div>
              </Link>
              {!darkMode ? <div onClick = {toggle}><DarkModeOutlinedIcon/> Chế độ tối</div> : 
              <div onClick = {toggle}><LightModeOutlinedIcon/> Chế độ sáng</div>}
              <Link to={`/profile/1`} style={{ textDecoration: 'none' }}>
                <div><SupervisorAccountIcon/> Trang quản trị viên</div>
              </Link>
            </div>}
            <ChangePassword show={changePasswordShow} setChangePasswordShow={setChangePasswordShow}/>
          </div>
          {/* <button onClick={logout}>Đăng xuất</button>         */}
        </div>
      </div>
      {searchContent !== '' && <div className="search-nav-result">
          {isLoading ? <div className="loading"><Loading/></div> : 
            <div className="content">
            {searchResult && searchResult.map(user=>(
                <Link to={`/profile/${user.id}`} onClick={() => setSearchContent('')} className='turnoff-link-style'>
                    <div className="search-row">
                        <div className="avatar">
                            <img src={user.avatar} alt="" />
                        </div>
                        <div className="name">
                            {user.lastName} {user.firstName}
                        </div>
                    </div>
                </Link>
            ))}
        </div>
          }
      </div>}
    </>

  )
}
