import { useContext, useEffect, useState } from 'react'
import './comment.scss'
import { AuthContext } from '../../context/AuthContext'
import { authAPI, endpoints, socketUrl } from '../../configs/Api'
import Loading from '../Loading'
import Moment from 'react-moment';
import 'moment/locale/vi'
import SendIcon from '@mui/icons-material/Send';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { Link } from 'react-router-dom'
import { UploadComment } from './UploadComment'
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import { EditComment } from './EditComment'
import { DarkModeContext } from '../../context/DarkModeContext'
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
import { load } from 'react-cookies'
import { SocketContext } from '../../context/SocketContext'
import ReplyIcon from '@mui/icons-material/Reply';
import { CommentItem } from './CommentItem'
import { DeleteConfirmation } from './DeleteComment'

// const DeleteConfirmation = (props) => {
//   const { darkMode } = useContext(DarkModeContext)
//   const deleteComment = (evt) => {
//     evt.preventDefault()
//     const process = async () => {
//       props.onHide()
//       props.setComments(props.comments.filter(comment => comment.id !== props.comment.id));
//       props.setComment(null);
//       try {
//         let res = await authAPI().delete(endpoints['comment'] + `/${props.comment.id}`)
//         if (res.status === 204) {
//           // reloadData()
//         }
//       } catch (ex) {

//       }
//     }

//     process()
//   }

//   return (
//     <Modal
//       {...props}
//       aria-labelledby="contained-modal-title-vcenter"
//       centered
//       className={`theme-${darkMode ? "dark" : "light"}`}
//     >
//       <Modal.Header closeButton>
//         <Modal.Title id="contained-modal-title-vcenter">
//           <DeleteOutlineOutlinedIcon /> Xóa bình luận
//         </Modal.Title>
//       </Modal.Header>
//       <Modal.Body>
//         Bạn có chắc chắn muốn xóa bình luận này ?
//       </Modal.Body>
//       <Modal.Footer>
//         <Button onClick={props.onHide} className="close-btn">Đóng</Button>
//         <Button type="submit" onClick={deleteComment}>Xác nhận xóa</Button>
//       </Modal.Footer>
//     </Modal>
//   );
// }

export const Comment = (props) => {
  const [user, dispatch] = useContext(AuthContext)
  const [comments, setComments] = useState(null)
  const [reloadComment, setReloadComment] = useState(true)
  const [editCommentId, setEditCommentId] = useState(null)
  const [replyCommentId, setReplyCommentId] = useState(null)
  const [deleteCommentShow, setDeleteCommentShow] = useState(false)
  const [commment, setComment] = useState(null)
  const [socketClient] = useContext(SocketContext)

  useEffect(() => {
    const loadComment = async () => {
      try {
        let res = await authAPI().get(endpoints['comment'] + `/${props.post.id}`)
        if (res.status === 200) {
          // setComments(res.data)
          setComments(res.data.map(comment => ({ ...comment, isOpenning: false })))
          setReloadComment(false)
        }
      } catch (ex) {
      }
    }

    if (reloadComment) {
      if (props.showLess === false) {
        loadComment()
      } else {
        setComments([])
        setReloadComment(false)
      }

    }
  }, [props.post.id, reloadComment])

  // useEffect(() => {
  //   if (comments !== null && !props.showLess) {
  //     props.setCommentTotal(comments.length)
  //   }
  // }, [comments])

  useEffect(() => {
    if (!props.showLess) {
      socketClient.subscribe('/comment' + `/${props.post.id}`, (payload) => {
        let newComment = JSON.parse(payload.body).comment
        let action = JSON.parse(payload.body).action
        // if (newComment.userId.id !== user.id) {
        //   if (action === "CREATE") {
        //     setComments(prevComments => [newComment, ...prevComments,]);
        //   } else if (action === "UPDATE") {
        //     setComments(prevState => {
        //       let data = [...prevState]
        //       let index = data.findIndex(item => item.id === newComment.id)
        //       if (index !== -1) {
        //         data[index] = {
        //           ...data[index],
        //           content: newComment.content,
        //           repliesTotal: newComment.repliesTotal,
        //           firstReply: newComment.firstReply
        //         }
        //       }
        //       return data;
        //     })
        //   } else {
        //     setComments(prevData => prevData.filter(item => item.id !== newComment.id))
        //   }
        // }
        if (action === "CREATE") {
          setComments(prevComments => [newComment, ...prevComments]);
        } else if (action === "UPDATE") {
          setComments(prevState => {
            let data = [...prevState]
            let index = data.findIndex(item => item.id === newComment.id)
            if (index !== -1) {
              data[index] = {
                ...data[index],
                content: newComment.content,
                repliesTotal: newComment.repliesTotal,
                firstReply: newComment.firstReply
              }
            }
            return data;
          })
        } else {
          setComments(prevData => prevData.filter(item => item.id !== newComment.id))
        }
      }, { id: `comment-${props.post.id}`, user: user.id })

      return () => {
        socketClient.unsubscribe(`comment-${props.post.id}`)
      }
    }
  }, [])

  useEffect(() => {
    if (props.showLess) {
      if (props.latestComment !== null && props.latestComment !== undefined) {
        setComments(props.latestComment)
      } else {
        setComments([])
      }
    }
  }, [props.latestComment])

  return (
    <div className='comments'>
      {!props.showLess && <UploadComment post={props.post} setReloadComment={setReloadComment} />}
      {comments !== null ?
        <>
          {comments.map((comment) => {
            return (
              <div key={comment.id}>
                {editCommentId === comment.id ?
                <div className="edit-comment">
                  <EditComment comments={comments} setComments={setComments} showLess={props.showLess} setEditCommentId={setEditCommentId} post={props.post} comment={comment} setReloadComment={setReloadComment} />
                </div>
                :
                <CommentItem focusParent={props.focusParent} focusComment={props.focusComment} setComments={setComments} parentComment={comment} replyCommentId={replyCommentId} setReplyCommentId={setReplyCommentId} showLess={props.showLess} showModal={props.showModal} setEditCommentId={setEditCommentId} post={props.post} setDeleteCommentShow={setDeleteCommentShow} setComment={setComment} comment={comment} setReloadComment={setReloadComment}/>}          
              </div>
            )
          })}
          <DeleteConfirmation show={deleteCommentShow} onHide={() => setDeleteCommentShow(false)} comment={commment} comments={comments} setComment={setComment} setComments={setComments} />
          {props.showLess && comments.length !== 0 && <div className="viewMoreComment" onClick={props.showModal} >Xem thêm bình luận ...</div>}
        </>
        :
        <div className="loading">
          <Loading />
        </div>
      }
    </div>
  )
}
