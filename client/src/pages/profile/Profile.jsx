import { useContext, useEffect, useRef, useState } from "react";
import { Post } from "../../components/post/Post";
import "./profile.scss"
import { AuthContext } from "../../context/AuthContext";
import { load, save } from "react-cookies";
import { Link, useParams } from "react-router-dom";
import { authAPI, endpoints } from "../../configs/Api";
import Loading from "../../components/Loading";
import EditIcon from '@mui/icons-material/Edit';
import MessageIcon from '@mui/icons-material/Message';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { DarkModeContext } from "../../context/DarkModeContext";
import { Form } from "react-bootstrap";
import { PostLayout } from "../../components/postLayout/PostLayout";
import { ReloadContext } from "../../context/ReloadContext";
import ImageModal from '../../components/imageInPost/ImageModal'
import { InView } from 'react-intersection-observer';
import { LeftBarContext } from "../../context/LeftBarContext";
import { PostSurvey } from "../../components/post/PostSurvey";
import { PostInvitation } from "../../components/post/PostInvitation";
import { SocketContext } from "../../context/SocketContext";

const UpdateAvatar = (props) => {
  const {darkMode} = useContext(DarkModeContext)
  const avatar = useRef()
  const [disableButton, setDisableButton] = useState(false);
  const [selectedAvatar, setSelectedAvatar] = useState(null);
  const [user, userDispatch] = useContext(AuthContext)

  const handleAvatarChange = (event) => {
    if (event.target.files[0]) {
      setSelectedAvatar(URL.createObjectURL(event.target.files[0]))
    } else {
      setSelectedAvatar(null)
    }
  };

  const close = () => {
    props.setEditAvatarShow(false)
    setSelectedAvatar(null)
    props.setEditProfileShow(true)
  };

  // const updateDocAvt = async (res) => {
  //   await updateDoc(doc(db, "users", auth.currentUser.uid), {
  //     photoURL: res.data.avatar,
  //   });
  // }

  const updateAvatar = (evt) => {
    evt.preventDefault()
    setDisableButton(true)
    const process = async () => {
      try {
        if (selectedAvatar !== null) {
          let form = new FormData()
          if (avatar.current.files.length > 0)
            form.append("uploadAvatar", avatar.current.files[0])
          let res = await authAPI().post(endpoints['update_avatar'], form, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          if (res.status === 200) {
            setDisableButton(false)
            // await updateProfile(auth.currentUser, {
            //   photoURL: res.data.avatar
            // })

            // updateDocAvt(res)

            save("current-user", res.data)
            userDispatch({
              "type": "LOGIN", 
              "payload": res.data
            })
          }
        } else {
          setDisableButton(false)
        }
      } catch (ex) {
        setDisableButton(false)
      }
    }
    process()
    close()
  }

  return (
      <Modal
        {...props}
        aria-labelledby="contained-modal-title-vcenter"
        centered
        className={`theme-${darkMode ? "dark" : "light"}`}
        onHide={close}
      >
        <Form onSubmit={updateAvatar}>
          <Modal.Header closeButton>
            <Modal.Title id="contained-modal-title-vcenter">
              <EditIcon/> Cập nhật ảnh đại diện
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
              <div className="avatar">
                <div className="row-title">
                  <h6>Ảnh đại diện</h6>
                </div>
                <div className="avatar-container">
                  <img src={selectedAvatar || props.profileUser.avatar} alt="" />
                </div>
                <div style={{display:"flex", justifyContent:"center"}}><input type="file" ref={avatar} name="uploadAvatar" onChange={handleAvatarChange} accept="image/png, image/jpeg"/></div>
              </div>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={close} className="close-btn">Đóng</Button>
            <Button type="submit" disabled={disableButton}>Lưu</Button>
          </Modal.Footer>
        </Form>
      </Modal>
  );
}

const UpdateCover = (props) => {
  const {darkMode} = useContext(DarkModeContext)
  const cover = useRef()
  const [disableButton, setDisableButton] = useState(false);
  const [selectedCover, setSelectedCover] = useState(null);
  const [user, userDispatch] = useContext(AuthContext)

  const handleCoverChange = (event) => {
    if (event.target.files[0]) {
      setSelectedCover(URL.createObjectURL(event.target.files[0]))
    } else {
      setSelectedCover(null)
    }
  };

  const close = () => {
    props.setEditCoverShow(false)
    setSelectedCover(null)
    props.setEditProfileShow(true)
  };

  const updateCover = (evt) => {
    evt.preventDefault()
    setDisableButton(true)
    const process = async () => {
      if (selectedCover !== null) {
        try {
          let form = new FormData()
          if (cover.current.files.length > 0)
              form.append("uploadCover", cover.current.files[0])
          let res = await authAPI().post(endpoints['update_cover'], form, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          if (res.status === 200) {
            setDisableButton(false)
            save("current-user", res.data)
            userDispatch({
              "type": "LOGIN", 
              "payload": res.data
            })
          }
        } catch (ex) {
          setDisableButton(false)
        }
      } else {
        setDisableButton(false)
      }
    }
    process()
    close()
  }

  return (
      <Modal
        {...props}
        aria-labelledby="contained-modal-title-vcenter"
        centered
        className={`theme-${darkMode ? "dark" : "light"}`}
        onHide={close}
      >
        <Form onSubmit={updateCover}>
          <Modal.Header closeButton>
            <Modal.Title id="contained-modal-title-vcenter">
              <EditIcon/> Cập nhật ảnh bìa
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
              <div className="cover">
                <div className="row-title">
                  <h6>Ảnh bìa</h6>
                </div>
                <div className="cover-container">
                  <img src={selectedCover || props.profileUser.coverAvatar} alt="" />
                </div>
                <div style={{display:"flex", justifyContent:"center"}}><input type="file" ref={cover} name="uploadCover" onChange={handleCoverChange} accept="image/png, image/jpeg"/></div>
              </div>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={close} className="close-btn">Đóng</Button>
            <Button type="submit" disabled={disableButton}>Lưu</Button>
          </Modal.Footer>
        </Form>
      </Modal>
  );
}

const UpdateInformation = (props) => {
  const {darkMode} = useContext(DarkModeContext)
  const [disableButton, setDisableButton] = useState(false);
  const [user, userDispatch] = useContext(AuthContext)
  const [updateUser, setUpdateUser] = useState({
    "dob": props.profileUser.dob,
  })

  const close = () => {
    props.setEditInformationShow(false);
    setUpdateUser(updateUser => ({ ...updateUser, dob: props.profileUser.dob }))
    props.setEditProfileShow(true);
  };

  const updateInformation = (evt) => {
    evt.preventDefault()
    setDisableButton(true)
    const process = async () => {
      try {
        let res = await authAPI().patch(endpoints['update_information'], {
          "dob": updateUser.dob
        })
        if (res.status === 200) {
          setDisableButton(false)
          save("current-user", res.data)
          userDispatch({
            "type": "LOGIN", 
            "payload": res.data
          })
        }
      } catch (ex) {
        
      }
    }

    if (updateUser.dob !== "" && updateUser.dob !== props.profileUser.dob) {
      process()
      close()
    } else {
      setDisableButton(false)
    }
  }

  return (
      <Modal
        {...props}
        aria-labelledby="contained-modal-title-vcenter"
        centered
        className={`theme-${darkMode ? "dark" : "light"}`}
        onHide={close}
      >
        <Form onSubmit={updateInformation}>
          <Modal.Header closeButton>
            <Modal.Title id="contained-modal-title-vcenter">
              <EditIcon/> Cập nhật thông tin
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <div className="user-info">
              <div className="info-row">
                <div className="info-title">Họ và tên:</div>
                <div>{props.profileUser.lastName} {props.profileUser.firstName}</div>
              </div>
              <div className="info-row">
                <div className="info-title">Ngày tháng năm sinh:</div>
                <div><input type="date" value={updateUser.dob} onChange={(e) => 
                  {
                    const selectedDateTime = e.target.value
                    setUpdateUser(updateUser => ({ ...updateUser, dob: selectedDateTime }))
                  }
                } /></div>
              </div>
            </div>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={close} className="close-btn">Đóng</Button>
            <Button type="submit" disabled={disableButton}>Lưu</Button>
          </Modal.Footer>
        </Form>
      </Modal>
  );
}

const EditProfile = (props) => {
  const {darkMode} = useContext(DarkModeContext)
  const [user, userDispatch] = useContext(AuthContext)

  const editAvatar = () => {
    props.setEditAvatarShow(true);
    props.setEditProfileShow(false);
  };
  const editCover = () => {
    props.setEditCoverShow(true);
    props.setEditProfileShow(false);
  };
  const editInformation = () => {
    props.setEditInformationShow(true);
    props.setEditProfileShow(false);
  };
  return (
      <Modal
        {...props}
        aria-labelledby="contained-modal-title-vcenter"
        centered
        className={`theme-${darkMode ? "dark" : "light"}`}
      >
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-vcenter">
            <EditIcon/> Chỉnh sửa trang cá nhân
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="avatar">
            <div className="row-title">
              <h6>Ảnh đại diện</h6>
              <div className="btn-edit" onClick={editAvatar}>Chỉnh sửa</div>
            </div>
            <div className="avatar-container">
              <img src={user.avatar} alt="" className="profilePic" />
            </div>
          </div>
          <div className="cover">
            <div className="row-title">
              <h6>Ảnh bìa</h6>
              <div className="btn-edit" onClick={editCover}>Chỉnh sửa</div>
            </div>
            <div className="cover-container">
              <img src={user.coverAvatar} alt="" className="cover" />
            </div>
          </div>
          <div className="user-information">
            <div className="row-title">
              <h6>Thông tin cá nhân</h6>
              <div className="btn-edit" onClick={editInformation}>Chỉnh sửa</div>
            </div>
            <div className="user-info">
              <div className="info-row">
                <div className="info-title">Họ và tên:</div>
                <div>{props.profileUser.lastName} {props.profileUser.firstName}</div>
              </div>
              <div className="info-row">
                <div className="info-title">Ngày tháng năm sinh:</div>
                <input type="date" readOnly value={user.dob} />
              </div>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={props.onHide}>Đóng</Button>
        </Modal.Footer>
      </Modal>
  );
}

export const Profile = () => {
  const { id } = useParams();
  const [user, userDispatch] = useContext(AuthContext)
  const [role, setRole] = useState(null)
  const [profileUser, setProfileUser] = useState(null)
  const [isValidUser, setValidUser] = useState(true)  
  const [editProfileShow, setEditProfileShow] = useState(false)
  const [editAvatarShow, setEditAvatarShow] = useState(false)
  const [editCoverShow, setEditCoverShow] = useState(false)
  const [editInformationShow, setEditInformationShow] = useState(false)
  const [posts, setPosts] = useState([])
  const { reload } = useContext(ReloadContext)
  const { reloadData } = useContext(ReloadContext)
  const [showAvatarModal, setShowAvatarModal] = useState(false);
  const [showCoverModal, setShowCoverModal] = useState(false);
  const [page, setPage] = useState(1)
  const [isLoading, setIsLoading] = useState(false)
  const [isCaughtUp, setIsCaughtUp] = useState(false)
  const { setCurrentMenu } = useContext(LeftBarContext)
  const [socketClient] = useContext(SocketContext)
  const [firstInit, setFirstInit] = useState(true)

  const toggleAvatarModal = () => setShowAvatarModal(!showAvatarModal);
  const toggleCoverModal = () => setShowCoverModal(!showCoverModal);

  useEffect(() => {
    return () => {
      if (socketClient !== null) {
        socketClient.unsubscribe(`profile-${id}`)
      }
    }
  }, [socketClient])

  useEffect(() => {
    if (socketClient !== null && posts !== null && firstInit) {
      setFirstInit(false)
      socketClient.subscribe('/profile' + `/${id}`, (payload) => {
        let newPost = JSON.parse(payload.body).post
        if (newPost.userId.id !== user.id) {
          setPosts(prevPosts => [newPost, ...prevPosts])
        }
      }, { id: `profile-${id}`, user: user.id })
    }
  }, [socketClient, posts])

  useEffect(() => {
    const loadProfile = async () => {
      setIsLoading(true)
      try {
        let res = await authAPI().get(endpoints['profile'] + `/${id}` + `?page=1`)
        if (res.data.posts.length === 0) {
          setIsCaughtUp(true)
        }
        setIsCaughtUp(false)
        setIsLoading(false)
        setFirstInit(true)
        setRole(res.data.role)
        setProfileUser(res.data.user)       
        setPosts(res.data.posts)
      } catch (ex) {
        setValidUser(false)
        // console.clear()
      } finally {
        setIsLoading(false)
      }
    }

    if (id === user.id.toString()) {
      setCurrentMenu('profile')
    } else if (id === "1") {
      setCurrentMenu('admin')
    } else {
      setCurrentMenu('anonymous')
    }
    setPage(1)
    setPosts([])
    loadProfile()
    window.scrollTo({ top: 0, left: 0, behavior: 'instant' })
  }, [id, reload])

  useEffect(() => {
    const loadProfile = async () => {
      setIsLoading(true)
      try {
        let res = await authAPI().get(endpoints['profile'] + `/${id}` + `?page=${page}`)
        if (res.data.posts.length === 0) {
          setIsCaughtUp(true)
        }
        setIsLoading(false)
        setPosts(prevPosts => [...prevPosts, ...res.data.posts])
      } catch (ex) {
        setValidUser(false)
        console.clear()
      } finally {
        setIsLoading(false)
      }
    }

    if (page > 1) {
      loadProfile()
    }
  }, [page])

  const handleIntersection = (inView) => {
    if (inView && !isLoading && !isCaughtUp) {
      setPage(page + 1);
    }
  }

  const postInView = (post) => {
    // console.log(" POST " + post.id + " RENDERED!")
    // setPosts(prevState => {
    //   let data = [...prevState]
    //   let index = data.findIndex(item => item.id === post.id);
    //   data[index] = {
    //       ...data[index],
    //       isRendered: true,
    //   }
    //   return data;
    // })
  }

  if ((profileUser === null || role === null) && isValidUser === true) {
    return <div className="profile">
      <div className="loading">
        <Loading/>
      </div>
    </div>
  } else if (isValidUser === false) {
    return <div className="profile">
      <div className="page_404">
        Bạn hiện không xem được nội dung này
      </div>
    </div>
  }
  return (
    <div className="profile">
      <div className="images">
        {profileUser.id === user.id ? (
          <>
            <img src={user.coverAvatar} alt="" className="cover" onClick={toggleCoverModal}/>
            {showCoverModal && (
              <ImageModal
                  images={[user.coverAvatar]}
                  index={0}
                  onClose={toggleCoverModal}
              />
            )}
          </>
        ) : (
          <>
            <img src={profileUser.coverAvatar} alt="" className="cover" onClick={toggleCoverModal}/>
            {showCoverModal && (
              <ImageModal
                  images={[profileUser.coverAvatar]}
                  index={0}
                  onClose={toggleCoverModal}
              />
            )}
          </>
        )}
      </div>
      <div className="profileContainer">
        <div className="uInfo">
          <div className="left">
            {profileUser.id === user.id ? (
              <>
                <img src={user.avatar} alt="" className="profilePic" onClick={toggleAvatarModal} />
                {showAvatarModal && (
                    <ImageModal
                        images={[user.avatar]}
                        index={0}
                        onClose={toggleAvatarModal}
                    />
                )}
              </>
            ) : (
              <>
                <img src={profileUser.avatar} alt="" className="profilePic" onClick={toggleAvatarModal}/>
                {showAvatarModal && (
                  <ImageModal
                      images={[profileUser.avatar]}
                      index={0}
                      onClose={toggleAvatarModal}
                  />
                )}
              </>
            )}
            
            <span className="user-name">{profileUser.lastName} {profileUser.firstName}</span>
            {role.id === 1 ? (
              <span className="user-role">Cựu sinh viên</span>
            ) : role.id === 2 ? (
              <span className="user-role">Giảng viên</span>
            ) : (
              <span className="user-role">Quản trị viên</span>
            )}
          </div>
          <div className="center">
          </div>
          {profileUser.id !== user.id ? 
            role.id !== 3 && <div className="right">
              <Link to={`/chat/${profileUser.id}`} className='turnoff-link-style' >
                <button className="softColor"><MessageIcon/> Nhắn tin</button>
              </Link>
            </div> :
            <div className="right">
              <button className="softColor" onClick={() => setEditProfileShow(true)}><EditIcon/> Sửa hồ sơ</button>
            </div>
          }
          <EditProfile show={editProfileShow} onHide={() => setEditProfileShow(false)} profileUser={profileUser}
            setEditProfileShow={setEditProfileShow} setEditAvatarShow={setEditAvatarShow} setEditCoverShow={setEditCoverShow} setEditInformationShow={setEditInformationShow}
          />
          <UpdateAvatar show={editAvatarShow} profileUser={profileUser}
            setEditProfileShow={setEditProfileShow} setEditAvatarShow={setEditAvatarShow}
          />
          <UpdateCover show={editCoverShow} profileUser={profileUser}
            setEditProfileShow={setEditProfileShow} setEditCoverShow={setEditCoverShow}
          />
          <UpdateInformation show={editInformationShow} profileUser={profileUser}
            setEditProfileShow={setEditProfileShow} setEditInformationShow={setEditInformationShow}
          />
        </div>      
      </div>
      <div className="posts">
      {/* <div className="posts" style={{paddingLeft: showSearch ? "21.5px" : ""}}> */}
        {profileUser.id === user.id ? <PostLayout/> : <></>}        
        {posts.length !== 0 && posts.map(post=>(
          post.postSurvey !== null ?
            <PostSurvey postInView={() => postInView(post)} post={post} key={post.id} posts={posts} setPosts={setPosts}/>
          :
          post.postInvitation !== null ?
            <PostInvitation postInView={() => postInView(post)} post={post} key={post.id} posts={posts} setPosts={setPosts}/>
          :
          <Post postInView={() => postInView(post)} post={post} key={post.id} posts={posts} setPosts={setPosts}/>
        ))}
        {posts.length !== 0 && <InView onChange={handleIntersection}>
              {({ inView, ref }) => (
                <div ref={ref}>
                  {inView && !isLoading && !isCaughtUp && (
                    <div className="bottom-loading">
                      <Loading />
                    </div>
                  )}
                </div>
              )}
        </InView >}
    </div>
  </div>
  )
}
