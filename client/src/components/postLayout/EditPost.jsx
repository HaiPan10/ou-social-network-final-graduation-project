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

const EditPost = (props) => {
    const {darkMode} = useContext(DarkModeContext)
    const [disableButton, setDisableButton] = useState(false)
    const [user, userDispatch] = useContext(AuthContext)
    const [isActiveComment, setActiveComment] = useState(props.post.isActiveComment)
    const [selectedFiles, setSelectedFiles] = useState(props.post.imageInPostList.map(image => image.imageUrl))
    const [content, setContent] = useState(props.post.content)
    const [err, setErr] = useState()
    const fileTypes = ["JPG", "PNG"]
    const droppedFilesRef = useRef([]);
    const { reloadData } = useContext(ReloadContext)
    const [isEditImage, setIsEditImage] = useState(false)

    useEffect(() => {
      const fetchAndAddFiles = async () => {
        for (let image of props.post.imageInPostList) {
          try {
            const response = await fetch(image.imageUrl);
            const imageBlob = await response.blob();
        
            const file = new File([imageBlob], `image${image.id}`, { type: image.contentType });
            droppedFilesRef.current.push(file);
          } catch (error) {
            console.error("Error fetching or creating file:", error);
          }
        }
      };
    
      if (props.show) {
        fetchAndAddFiles()
        setActiveComment(props.post.isActiveComment)
        setSelectedFiles(props.post.imageInPostList.map(image => image.imageUrl))
        setContent(props.post.content)
        setIsEditImage(false)
        setErr()
      }
    }, [props.show]);
    

    const handleContentChange = (event) => {
      setContent(event.target.value)
    }

    const handleDrop = (droppedFile) => {
      const imageUrls = [];
      console.log([...droppedFile])
      droppedFilesRef.current = [...droppedFilesRef.current, ...droppedFile];
      for (let i = 0; i < droppedFile.length; i++) {
        imageUrls.push(URL.createObjectURL(droppedFile[i]));
      }
      // const imageUrl = URL.createObjectURL(droppedFile);
      setSelectedFiles(prevSelectedFiles => [...prevSelectedFiles, ...imageUrls]);
      setIsEditImage(true)
    }
  
    const clear = () => {
      setSelectedFiles([])
      droppedFilesRef.current = []
      setIsEditImage(true)
    }
    const handleClick = () => setActiveComment(!isActiveComment)
  
    const editPost = (evt) => {
      evt.preventDefault()
      const process = async () => {
        props.onHide()
        setDisableButton(true)
        try {
          let form = new FormData()
          if (selectedFiles.length > 0) {
            for (let i = 0; i < droppedFilesRef.current.length; i++) {
              form.append("images", droppedFilesRef.current[i])
            }
          }
          form.append("id", props.post.id)
          form.append("content", content)
          form.append("isActiveComment", isActiveComment)
          form.append("isEditImage", isEditImage)
          let res = await authAPI().post(endpoints['posts'], form, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          if (res.status === 200) {
            droppedFilesRef.current = []
            reloadData()
            setDisableButton(false)
          }
        } catch (ex) {
          props.setEditPostShow(true)
          setErr(ex.response.data)
          setDisableButton(false)
        }
      }
  
      if (selectedFiles.length === 0 && (content === '')) {
        setErr( <><PriorityHighIcon/> Bài đăng này rỗng!</>)
      } else if (!isEditImage && content===props.post.content && isActiveComment===props.post.isActiveComment) {
        setErr( <><PriorityHighIcon/> Không có thay đổi nào!</>)
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
          id='modal-post'
        >
          <Form onSubmit={editPost}>
            <Modal.Header closeButton>
              <Modal.Title id="contained-modal-title-vcenter">
                Chỉnh sửa bài viết
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

export default EditPost