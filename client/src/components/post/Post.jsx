import React, { useContext, useEffect, useRef, useState } from 'react'
import "./post.scss"
import TextsmsOutlinedIcon from "@mui/icons-material/TextsmsOutlined";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { Comment } from '../comments/Comment';
import Moment from 'react-moment';
import ImageInPost from '../imageInPost/ImageInPost'
import { AuthContext } from '../../context/AuthContext';
import 'moment/locale/vi'
import SpeakerNotesOffOutlinedIcon from '@mui/icons-material/SpeakerNotesOffOutlined';
import EditNoteOutlinedIcon from '@mui/icons-material/EditNoteOutlined';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
import EditPost from '../postLayout/EditPost';
import { authAPI, endpoints } from '../../configs/Api';
import { DarkModeContext } from '../../context/DarkModeContext';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import SentimentVerySatisfiedRoundedIcon from '@mui/icons-material/SentimentVerySatisfiedRounded';
import ThumbUpOutlinedIcon from '@mui/icons-material/ThumbUpOutlined';
import ThumbUpRoundedIcon from '@mui/icons-material/ThumbUpRounded';
import FavoriteOutlinedIcon from '@mui/icons-material/FavoriteBorderTwoTone';
import FavoriteRoundedIcon from '@mui/icons-material/FavoriteRounded';
import Reaction from '../reaction/Reaction';
import { PostModal } from './PostModal';
import SendIcon from '@mui/icons-material/Send';
import { InView, useInView } from 'react-intersection-observer';
import { SocketContext } from '../../context/SocketContext';
import { load } from 'react-cookies';
import { ReloadContext } from '../../context/ReloadContext';

const DeleteConfirmation = (props) => {
  const { darkMode } = useContext(DarkModeContext)
  const [user, userDispatch] = useContext(AuthContext)
  const navigate = useNavigate();

  const deletePost = (evt) => {
    evt.preventDefault()
    const process = async () => {
      props.onHide()
      if (props.posts !== undefined) {
        props.setPosts(props.posts.filter(post => post.id !== props.post.id));
      }
      try {
        let res = await authAPI().delete(endpoints['posts'] + `/${props.post.id}`)
        if (res.status === 204) {
          // reloadData()
          if (props.posts === undefined) {
            navigate('/');
          }
        }
      } catch (ex) {

      }
    }

    if (user.id === props.post.user.id) {
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
          <DeleteOutlineOutlinedIcon /> Xóa bài đăng
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

export const Post = ({ post, posts, setPosts, postInView, focusComment, focusParent }) => {
  const images = (post.imageInPostList !== null ? post.imageInPostList.map(image => image.imageUrl) : []);
  const [user, userDispatch] = useContext(AuthContext)
  const [dropdownVisible, setDropdownVisible] = useState(false)
  const [editPostShow, setEditPostShow] = useState(false)
  const [deletePostShow, setDeletePostShow] = useState(false)
  const [currentReaction, setCurrentReaction] = useState(post.currentReaction?.id || null);
  const [reactionShow, setReactionShow] = useState(false)
  const [reaction, setReaction] = useState(null)
  const [reloadReaction, setReloadReaction] = useState(false);
  const [total, setTotal] = useState(null);
  const dropdownRef = useRef(null);
  const [postModalShow, setPostModalShow] = useState(false);
  // const [commentTotal, setCommentTotal] = useState(post.commentTotal)
  const [inView, setInView] = useState()
  const [socketClient] = useContext(SocketContext)
  const [reactionMap, setReactionMap] = useState(post.reactionTotal)
  const getReactionTotal = (reactionId) => {
    const total = reactionMap[reactionId];
      return total !== undefined ? total : 0;
  }
  const [like, setLike] = useState(getReactionTotal(1))
  const [lol, setLol] = useState(getReactionTotal(2))
  const [favourite, setFavourite] = useState(getReactionTotal(3))
  const [subcribe, setSubcribe] = useState(false)
  const [latestComment, setLatestComment] = useState(null)

  useEffect(() => {
    let reactionSubcribe = null
    let commentTotalSubcribe = null
    const handleSubscription = () => {
      if (inView) {
        setSubcribe(true)
        reactionSubcribe = socketClient.subscribe(`/reaction/${post.id}`, (payload) => {
          let buffer = JSON.parse(payload.body);
          if (buffer.interactUser !== user.id) {
            setReactionMap(buffer.reactionTotal);
          }
        }, { id: `reaction-${post.id}`, user: user.id});

        if (posts !== undefined) {
          commentTotalSubcribe = socketClient.subscribe(`/comment-total/${post.id}`, (payload) => {
            let buffer = JSON.parse(payload.body)
            setPosts(prevState => {
              let data = [...prevState]
              let index = data.findIndex(item => item.id === post.id)
              if (index !== -1) {
                data[index] = {
                  ...data[index],
                  commentTotal: buffer.commentTotal,
                }
              }
              return data;
            })
            // setCommentTotal(buffer.commentTotal)
            setLatestComment(buffer.latestComment)
          }, { id: `comment-total-${post.id}`, user: user.id})

          postInView()
        }
      } else {
        if (subcribe) {
          setSubcribe(false)
          socketClient.unsubscribe(`reaction-${post.id}`)
          if (posts !== undefined) {
            socketClient.unsubscribe(`comment-total-${post.id}`)
          }
        }
      }
    }

    handleSubscription()
    return () => {
      if (reactionSubcribe !== null) {
        socketClient.unsubscribe(`reaction-${post.id}`)
      }
      if (commentTotalSubcribe !== null) {
        socketClient.unsubscribe(`comment-total-${post.id}`)
      }
    }
  }, [inView]);

  useEffect(() => {
    setLike(getReactionTotal(1))
    setLol(getReactionTotal(2))
    setFavourite(getReactionTotal(3))
  }, [reactionMap]);
  
  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [])

  const toggleDropdown = () => {
    setDropdownVisible(!dropdownVisible);
  }

  const handleClickOutside = (event) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
      setDropdownVisible(false)
    }
  }

  const react = (evt, reaction_id) => {
    setReactionMap(prevReaction => ({
      ...prevReaction,
      [reaction_id]: (reactionMap[reaction_id] !== undefined ? reactionMap[reaction_id] + 1 : 1)
    }));
    if (currentReaction !== null) {
      setReactionMap(prevReaction => ({
        ...prevReaction,
        [currentReaction]: reactionMap[currentReaction] - 1
      }));
    }
    setCurrentReaction(reaction_id)
    evt.preventDefault()
    const postReaction = async () => {
      try {
        let res = await authAPI().post(endpoints['post_reactions'] + `/${post.id}`, {
          "id": reaction_id
        })
        if (res.status === 200) {
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
    setReactionMap(prevReaction => ({
      ...prevReaction,
      [reaction_id]: reactionMap[reaction_id] - 1
    }));
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
  }

  const showModal = () => {
    if (posts !== undefined) {
      setPostModalShow(true)
    }
  }

  const formattedDate = post.createdAt.replace(/(\d{2})-(\d{2})-(\d{4}) (\d{2}:\d{2}:\d{2})/, '$3-$2-$1 $4');

  return (
    <InView onChange={setInView}>
      {({ ref }) => (
        <div ref={ref} className='post'>
          <div className="postContainer">
            <div className="user">
              <div className="userInfo">
                <Link to={`/profile/${post.user.id}`}>
                  {post.user.id === user.id ?
                    <img src={user.avatar} alt="" /> :
                    <img src={post.user.avatar} alt="" />
                  }
                </Link>

                <div className="details">
                  <Link to={`/profile/${post.user.id}`} style={{ textDecoration: "none", color: "inherit" }}>
                    <span className='name'>{post.user.lastName} {post.user.firstName}</span>
                  </Link>
                  <span className='date'><Moment locale="vi" fromNow>{formattedDate}</Moment></span>
                </div>
              </div>
              {post.user.id === user.id &&
                <>
                  <div className="dropdown" ref={dropdownRef} onClick={toggleDropdown}>
                    <div className='btn-edit' ><MoreHorizIcon /></div>
                    {dropdownVisible && <div className="dropdown-content">
                      <div onClick={() => setEditPostShow(true)}><EditNoteOutlinedIcon /> Chỉnh sửa</div>
                      <div onClick={() => setDeletePostShow(true)}><DeleteOutlineOutlinedIcon /> Xóa</div>
                    </div>}
                  </div>
                  <EditPost show={editPostShow} onHide={() => setEditPostShow(false)} setEditPostShow={setEditPostShow} post={post} />
                  <DeleteConfirmation show={deletePostShow} onHide={() => setDeletePostShow(false)} post={post} posts={posts} setPosts={setPosts} />
                </>
              }
            </div>
            <div className="content">
              <p>{post.content}</p>
              <ImageInPost hideOverlay={true} images={images} />
              {/* <img src={post.img} /> */}
            </div>
            <div className="info">
              <div className="item">
                {currentReaction === 1 ? <ThumbUpRoundedIcon onClick={(e) => deleteReaction(e, 1)} className='reaction-icon selected' /> : <ThumbUpOutlinedIcon onClick={(e) => react(e, 1)} className='reaction-icon' />}
                <div className="reaction_number" onClick={() => showReactionInformation(1)}>{like}</div>
                {currentReaction === 2 ? <SentimentVerySatisfiedRoundedIcon onClick={(e) => deleteReaction(e, 2)} className='reaction-icon selected' /> : <SentimentVerySatisfiedRoundedIcon onClick={(e) => react(e, 2)} className='reaction-icon' />}
                <div className="reaction_number" onClick={() => showReactionInformation(2)}>{lol}</div>
                {currentReaction === 3 ? <FavoriteRoundedIcon onClick={(e) => deleteReaction(e, 3)} className='reaction-icon selected' /> : <FavoriteOutlinedIcon onClick={(e) => react(e, 3)} className='reaction-icon' />}
                <div className="reaction_number" onClick={() => showReactionInformation(3)}>{favourite}</div>
                <Reaction show={reactionShow} onHide={closeReactionInformation} reloadReaction={reloadReaction} post={post} reaction={reaction} total={total} />
              </div>
              {post.isActiveComment ? <div className="item reaction-icon" onClick={showModal}>
                <TextsmsOutlinedIcon /> {post.commentTotal} bình luận
              </div> :
                <div className='lockComment'> <SpeakerNotesOffOutlinedIcon /> Bình luận bị khóa! </div>}
            </div>
            {posts !== undefined && <div className="comments" onClick={showModal}>
              <div className="read">
                <img src={user.avatar} alt="" />
                <input type="text" readOnly placeholder="Viết bình luận" />
              </div>
            </div>}
            {post.isActiveComment && <Comment focusParent={focusParent} focusComment={focusComment} showModal={showModal} latestComment={posts !== undefined && latestComment !== null ? latestComment : null } post={post} showLess={posts === undefined ? false : true} />}
            <PostModal
              post={post}
              show={postModalShow}
              onHide={() => setPostModalShow(false)}
            />
          </div>
        </div>
      )}
    </InView>
  )
}
