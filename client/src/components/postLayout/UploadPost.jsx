import { Form, ToggleButton } from "react-bootstrap";
import { DarkModeContext } from "../../context/DarkModeContext";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { MDBSwitch } from 'mdb-react-ui-kit';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import ImageInPost from '../imageInPost/ImageInPost'
import ClearIcon from '@mui/icons-material/Clear';
import { FileUploader } from "react-drag-drop-files";
import { authAPI, endpoints } from "../../configs/Api";
import ErrorAlert from "../ErrorAlert"
import PriorityHighIcon from '@mui/icons-material/PriorityHigh';
import "./postLayout.scss"
import { AuthContext } from "../../context/AuthContext"
import { useContext, useEffect, useRef, useState } from "react"
import { ReloadContext } from "../../context/ReloadContext";

const UploadPost = (props) => {
    const {darkMode} = useContext(DarkModeContext)
    const [disableButton, setDisableButton] = useState(false)
    const [user, userDispatch] = useContext(AuthContext)
    const [isActiveComment, setActiveComment] = useState(true)
    const [selectedFiles, setSelectedFiles] = useState([])
    const [content, setContent] = useState('')
    const [err, setErr] = useState()
    const fileTypes = ["JPG", "PNG"]
    const droppedFilesRef = useRef([]);
    const { reloadData } = useContext(ReloadContext)
  
    const handleContentChange = (event) => {
      setContent(event.target.value);
    }

    const handleDrop = (droppedFile) => {
      const imageUrls = [];
      droppedFilesRef.current = [...droppedFilesRef.current, ...droppedFile];
      for (let i = 0; i < droppedFile.length; i++) {
        imageUrls.push(URL.createObjectURL(droppedFile[i]));
      }
      // const imageUrl = URL.createObjectURL(droppedFile);
      setSelectedFiles(prevSelectedFiles => [...prevSelectedFiles, ...imageUrls]);
    };
  
    const clear = () => {
      setSelectedFiles([])
      droppedFilesRef.current = []
    }

    const close = () => {
      props.setUploadPostShow(false)
      clear()
      droppedFilesRef.current = []
      setActiveComment(true)
      setContent('')
      setDisableButton(false)
    }

    const handleClick = () => setActiveComment(!isActiveComment)
  
    const uploadPost = (evt) => {
      evt.preventDefault()
      const process = async () => {
        props.onHide()
        setDisableButton(true)
        try {
          let form = new FormData()
          if (selectedFiles.length > 0) {
            console.log(droppedFilesRef.current.length)
            for (let i = 0; i < droppedFilesRef.current.length; i++) {
              form.append("images", droppedFilesRef.current[i])
            }
          }
          form.append("postContent", content)
          form.append("isActiveComment", isActiveComment)              
          let res = await authAPI().post(endpoints['upload'], form, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          if (res.status === 201) {
            clear()
            droppedFilesRef.current = []
            setActiveComment(true)
            setContent('')
            reloadData()
            setDisableButton(false)
          }
        } catch (ex) {
          props.setUploadPostShow(true)
          setErr(ex.response.data)
          setDisableButton(false)
        }
      }
  
      if (selectedFiles.length === 0 && (content === '')) {
        setErr( <><PriorityHighIcon/> Bài đăng này rỗng!</>)
      } else {
        setErr()
        process()
      }
    }
  
    return (
        <Modal
          {...props}
          aria-labelledby="contained-modal-title-vcenter"
          centered
          className={`theme-${darkMode ? "dark" : "light"}`}
          onHide={close}
          id='modal-post'
        >
          <Form onSubmit={uploadPost}>
            <Modal.Header closeButton>
              <Modal.Title id="contained-modal-title-vcenter">
                Tạo bài viết
              </Modal.Title>
            </Modal.Header>
            <Modal.Body className="post-body">
              <div className="user">
                <div className="avatar">
                  <img src={user.avatar} alt="" />
                </div>
                <div className="info">
                  <div>{user.lastName} {user.firstName}</div>
                  <div className="comment-toggle">
                    <MDBSwitch id='flexSwitchCheckDefault' checked={isActiveComment} onClick={handleClick} label='Cho phép bình luận'/>
                  </div>
                </div>
              </div>
              <div className="content">
                <textarea placeholder="Chia sẻ trạng thái của bạn" rows={3} maxlength="255" value={content} onChange={handleContentChange}/>
              </div>
              {err?<ErrorAlert err={err} />:""}
              <div className="image">
                <FileUploader hoverTitle="Thả vào đây!" multiple={true} handleChange={handleDrop} name="file" types={fileTypes}/>
                {selectedFiles.length === 0 ? 
                <>
                </> :
                  <div className="selected-images">
                    <div className="editImage">
                      <div>
                        <label class="custom-file-input" onClick={clear}><ClearIcon/></label>
                      </div>
                    </div>
                    <ImageInPost hideOverlay={true} images={selectedFiles} />
                  </div>
                }
              </div>
            </Modal.Body>
            <Modal.Footer className="post-footer">
              <Button className="submit" type="submit" disabled={disableButton}>Đăng</Button>
            </Modal.Footer>
          </Form>
        </Modal>
    );
  }

export default UploadPost