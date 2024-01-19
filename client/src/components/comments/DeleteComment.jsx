import { useContext } from 'react'
import './comment.scss'
import { authAPI, endpoints } from '../../configs/Api'
import 'moment/locale/vi'
import { DarkModeContext } from '../../context/DarkModeContext'
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';

export const DeleteConfirmation = (props) => {
    const { darkMode } = useContext(DarkModeContext)
    const deleteComment = (evt) => {
      evt.preventDefault()
      const process = async () => {
        props.onHide()
        props.setComments(props.comments.filter(comment => comment.id !== props.comment.id));
        props.setComment(null);
        try {
          let res = await authAPI().delete(endpoints['comment'] + `/${props.comment.id}`)
          if (res.status === 204) {
            // reloadData()
          }
        } catch (ex) {
  
        }
      }
  
      process()
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
            <DeleteOutlineOutlinedIcon /> Xóa bình luận
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Bạn có chắc chắn muốn xóa bình luận này ?
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={props.onHide} className="close-btn">Đóng</Button>
          <Button type="submit" onClick={deleteComment}>Xác nhận xóa</Button>
        </Modal.Footer>
      </Modal>
    );
  }
  