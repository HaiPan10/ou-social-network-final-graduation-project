import React, { useContext, useEffect, useRef, useState } from 'react'
import "./post.scss"
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import { Link } from "react-router-dom";
import Moment from 'react-moment';
import { AuthContext } from '../../context/AuthContext';
import 'moment/locale/vi'
import EditNoteOutlinedIcon from '@mui/icons-material/EditNoteOutlined';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
import EditPost from '../postLayout/EditPost';
import { authAPI, endpoints } from '../../configs/Api';
import { DarkModeContext } from '../../context/DarkModeContext';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import EmailIcon from '@mui/icons-material/Email';
import PostImage from '../../images/invitation-post-image.png'
import CountDown from '../countDown/CountDown';
import { InView } from 'react-intersection-observer';

const DeleteConfirmation = (props) => {
    const {darkMode} = useContext(DarkModeContext)
    // const { reloadData } = useContext(ReloadContext)
    const [user, userDispatch] = useContext(AuthContext)
  
    const deletePost = (evt) => {
      evt.preventDefault()
      const process = async () => {
        props.onHide()
        props.setPosts(props.posts.filter(post => post.id !== props.post.id));
        try {
          let res = await authAPI().delete(endpoints['posts'] + `/${props.post.id}`)
          if (res.status === 204) {
            // reloadData()
          }
        } catch (ex) {
          
        }
      }

      if (user.id === props.post.userId.id) {
        process()
      }
    }
  
    return (
        <Modal
          {...props}
          aria-labelledby="contained-modal-title-vcenter"
          centered
          className={`theme-${darkMode ? "dark" : "light"}`}
        >
          <Modal.Header closeButton>
            <Modal.Title id="contained-modal-title-vcenter">
              <DeleteOutlineOutlinedIcon/> Xóa bài đăng
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Bạn có chắc chắn muốn xóa bài đăng này ?
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={props.onHide} className="close-btn">Đóng</Button>
            <Button type="submit" onClick={deletePost}>Xác nhận xóa</Button>
          </Modal.Footer>
        </Modal>
    );
  }

  export const PostInvitation = ({post, posts, setPosts, postInView }) => {
    const [commentOpen, setCommentOpen] = useState(posts === undefined ? true : false)
    const images = post.imageInPostList.map(image => image.imageUrl)
    const [user, userDispatch] = useContext(AuthContext)
    const [dropdownVisible, setDropdownVisible] = useState(false)
    const [editPostShow, setEditPostShow] = useState(false)
    const [deletePostShow, setDeletePostShow] = useState(false)
    const [currentReaction, setCurrentReaction] = useState(post.currentReaction?.id || null)
    const [reactionShow, setReactionShow] = useState(false)
    const [reaction, setReaction] = useState(null)
    const [reloadReaction, setReloadReaction] = useState(false)
    const [total, setTotal] = useState(null)
    const dropdownRef = useRef(null)
    const [inView, setInView] = useState(false)
    const [firstInit, setFirstInit] = useState(true)

    const [datePart, timePart] = post.postInvitation.startAt.split(' ')
    const [day, month, year] = datePart.split('-').map(Number);
    const [hours, minutes, seconds] = timePart.split(':').map(Number)
    const date = new Date(year, month - 1, day)

    const dayNames = [
      'Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy'
    ];
  
    const formattedInvitationDate = `${dayNames[date.getDay()]}, ngày ${day} tháng ${month} năm ${year}`
    const formattedInvitationTime = `${hours} giờ ${minutes} phút`
    const dateInCowntDown = new Date(year, month - 1, day, hours, minutes, seconds)
    const dateInMS = dateInCowntDown.getTime()
    const toggleDropdown = () => {
        setDropdownVisible(!dropdownVisible);
    }

    const handleClickOutside = (event) => {
        if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
          setDropdownVisible(false)
        }
    }

    const getReactionTotal = (reactionId) => {
      const total = post.reactionTotal[reactionId];
      return total !== undefined ? total : 0;
    }

    const [like, setLike] = useState(getReactionTotal(1))
    const [lol, setLol] = useState(getReactionTotal(2))
    const [favourite, setFavourite] = useState(getReactionTotal(3))

    const react = (evt, reaction_id) => {
      if (reaction_id === 1) {
        setLike(like + 1)
      } else if (reaction_id === 2) {
        setLol(lol + 1)
      } else if (reaction_id === 3) {
        setFavourite(favourite + 1)
      }
      if (currentReaction === 1) {
        setLike(like - 1)
      } else if (currentReaction === 2) {
        setLol(lol - 1)
      } else if (currentReaction === 3) {
        setFavourite(favourite - 1)
      }
      setCurrentReaction(reaction_id)
      evt.preventDefault()
      const postReaction = async () => {
        try {
          let res = await authAPI().post(endpoints['post_reactions'] + `/${post.id}`, {
            "id": reaction_id
          })
          if (res.status === 201) {
            setReloadReaction(!reloadReaction)
          }
        } catch (ex) {
        }
      }

      const putReaction = async () => {
        try {
          let res = await authAPI().put(endpoints['post_reactions'] + `/${post.id}`, {
            "id": reaction_id
          })
          if (res.status === 200) {
            setReloadReaction(!reloadReaction)
          }
        } catch (ex) {
        }
      }

      if (currentReaction === null) {
        postReaction()
      } else if (currentReaction !== reaction_id) {
        putReaction()
      }
    }

    const deleteReaction = (evt, reaction_id) => {
      if (reaction_id === 1) {
        setLike(like - 1)
      } else if (reaction_id === 2) {
        setLol(lol - 1)
      } else if (reaction_id === 3) {
        setFavourite(favourite - 1)
      }
      setCurrentReaction(null)
      evt.preventDefault()
      const process = async () => {
        try {
          let res = await authAPI().delete(endpoints['post_reactions'] + `/${post.id}`)
          if (res.status === 204) {
            setReloadReaction(!reloadReaction)
          }
        } catch (ex) {
        }
      }
      process()
    }

    useEffect(() => {
      document.addEventListener('mousedown', handleClickOutside);
      return () => {
        document.removeEventListener('mousedown', handleClickOutside)
      }
    }, [])

    useEffect(() => {
      if (inView && posts !== undefined) {
        postInView()
      }
    }, [inView])

    const showReactionInformation = (reaction_id) => {
      setReaction(reaction_id)
      if (reaction_id === 1) {
        setTotal(like)
      } else if (reaction_id === 2) {
        setTotal(lol)
      } else {
        setTotal(favourite)
      }
      setReactionShow(true)
    }

    const closeReactionInformation = () => {
      setReactionShow(false)
      // setReaction(null)
      // setTotal(null)
    }

    const formattedDate = post.createdAt.replace(/(\d{2})-(\d{2})-(\d{4}) (\d{2}:\d{2}:\d{2})/, '$3-$2-$1 $4');

    return (
    <InView onChange={setInView}>
      {({ ref }) => (
        <div ref={ref} className='post'>
            <div className="postContainer">
                <div className="user">
                    <div className="userInfo">
                        <Link to={`/profile/${post.userId.id}`}>
                          {post.userId.id === user.id ? 
                            <img src={user.avatar} alt=""/> :
                            <img src={post.userId.avatar} alt=""/>
                          }
                        </Link>
                        
                        <div className="details">
                            <Link to={`/profile/${post.userId.id}`} style={{textDecoration:"none", color:"inherit"}}>
                                <span className='name'>{post.userId.lastName} {post.userId.firstName} <span>mời bạn tham gia sự kiện <EmailIcon/></span></span>
                            </Link>
                            <span className='date'><Moment locale="vi" fromNow>{formattedDate}</Moment></span>
                        </div>
                    </div>
                    {post.userId.id===user.id &&
                        <>
                            <div className="dropdown" ref={dropdownRef} onClick={toggleDropdown}>
                                <div className='btn-edit' ><MoreHorizIcon/></div>
                                {dropdownVisible && <div className="dropdown-content">
                                    <div onClick={() => setEditPostShow(true)}><EditNoteOutlinedIcon/> Chỉnh sửa</div>
                                    <div onClick={() => setDeletePostShow(true)}><DeleteOutlineOutlinedIcon/> Xóa</div>
                                </div>}
                            </div>
                            <EditPost show={editPostShow} onHide={() => setEditPostShow(false)} setEditPostShow={setEditPostShow} post={post} />
                            <DeleteConfirmation show={deletePostShow} onHide={() => setDeletePostShow(false)} post={post} posts={posts} setPosts={setPosts}/>
                        </>
                    }
                </div>
                <div className="content">
                    <h3 className='title'>{post.postInvitation.eventName}</h3>
                    <p style={{marginBottom: "2px"}}>{post.content}</p>
                    <div className="image-post">
                      <div className="layout">
                        <div className="layout-title">
                          Sự kiện sẽ diễn ra sau
                        </div>
                        <div className="countdown">
                          <CountDown thenInMS={dateInMS}></CountDown>
                        </div>
                      </div>
                      <img style={{marginTop: "0"}} src={PostImage} alt="" />
                    </div>
                    <div className="start_at">{formattedInvitationDate}<span> lúc {formattedInvitationTime}</span></div>
                </div>
            </div>
        </div>
      )}
    </InView>
  )
}

