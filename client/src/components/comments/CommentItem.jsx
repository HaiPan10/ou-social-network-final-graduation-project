import React, { useContext, useEffect, useRef, useState } from 'react'
import { EditComment } from './EditComment'
import { Link } from 'react-router-dom'
import Moment from 'react-moment'
import { AuthContext } from '../../context/AuthContext'
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ReplyIcon from '@mui/icons-material/Reply';
import KeyboardDoubleArrowUpIcon from '@mui/icons-material/KeyboardDoubleArrowUp';
import './comment.scss'
import { authAPI, endpoints } from '../../configs/Api'
import { DeleteConfirmation } from './DeleteComment'
import { UploadComment } from './UploadComment'
import { SocketContext } from '../../context/SocketContext'
import { load } from 'react-cookies'
import 'animate.css'

export const CommentItem = (props) => {
    const formattedDate = props.comment.createdDate.replace(/(\d{2})-(\d{2})-(\d{4}) (\d{2}:\d{2}:\d{2})/, '$3-$2-$1 $4')
    // const [editComment, setEditComment] = useState(false)
    const [user, dispatch] = useContext(AuthContext)
    const [showReply, setShowReply] = useState(false)
    const [replies, setReplies] = useState(null)
    // const [editReplyId, setEditReplyId] = useState(null)
    const [reloadReplies, setReloadReplies] = useState(true)
    const [deleteReplyShow, setDeleteReplyShow] = useState(false)
    const [comment, setComment] = useState(null)
    const [editCommentId, setEditCommentId] = useState(null)
    // const [replyCommentShow, setReplyCommentShow] = useState(false)
    const [repliedUser, setRepliedUser] = useState(props.comment.user)
    const [repliedComment, setRepliedComment] = useState(props.comment)
    const [socketClient] = useContext(SocketContext)
    const commentRef = useRef()

    useEffect(() => {
        const loadReplies = async () => {
            try {
                let res = await authAPI().get(endpoints['comment'] + `/${props.post.id}/${props.comment.id}`)
                setReplies(res.data)
                setReloadReplies(false)
            } catch (ex) {
                console.clear()
            }
        }

        const handleNewReply = () => {
            socketClient.unsubscribe(`reply-${props.comment.id}`)
            socketClient.subscribe('/reply' + `/${props.comment.id}`, (payload) => {
                let newReply = JSON.parse(payload.body).comment
                let action = JSON.parse(payload.body).action
                // if (newReply.user.id !== user.id) {
                //     if (action === "CREATE") {
                //         setReplies(prevComments => [...prevComments, newReply]);
                //     } else if (action === "UPDATE") {
                //         setReplies(prevState => {
                //             let data = [...prevState]
                //             let index = data.findIndex(item => item.id === newReply.id)
                //             if (index !== -1) {
                //                 data[index] = {
                //                     ...data[index],
                //                     content: newReply.content
                //                 };
                //             }
                //             return data;
                //         })
                //     } else {
                //         setReplies(prevData => prevData.filter(item => item.id !== newReply.id))
                //     }
                // }
                if (action === "CREATE") {
                    setReplies(prevComments => [...prevComments, newReply]);
                } else if (action === "UPDATE") {
                    setReplies(prevState => {
                        let data = [...prevState]
                        let index = data.findIndex(item => item.id === newReply.id)
                        if (index !== -1) {
                            data[index] = {
                                ...data[index],
                                content: newReply.content
                            };
                        }
                        return data;
                    })
                } else {
                    setReplies(prevData => prevData.filter(item => item.id !== newReply.id))
                }
            }, { id: `reply-${props.comment.id}`, user: user.id })
        }

        if (showReply && reloadReplies) {
            loadReplies()
        }
        if (showReply && props.comment.level === 1) {
            handleNewReply()
        } else {
            socketClient.unsubscribe(`reply-${props.comment.id}`)
        }
    }, [showReply, reloadReplies])

    const reply = () => {
        if (props.showLess) {
            props.showModal()
        } else {
            if (props.comment.level === 1) {
                setShowReply(true)
                setRepliedUser(props.comment.user)
                setRepliedComment(props.comment)
            } else {
                props.setRepliedUser(props.comment.user)
                props.setRepliedComment(props.comment)
            }
            props.setReplyCommentId(props.parentComment.id)
        }
    }

    useEffect(() => {
        if ((replies === null || replies.length === 0) && props.replyCommentId !== props.comment.id) {
            setShowReply(false)
        }
    }, [props.replyCommentId])

    useEffect(() => {
        if (props.focusParent !== null && props.focusParent !== undefined) {
            if (props.comment.id === props.focusParent) {
                setShowReply(true)
            }
        }
    }, [props.focusParent])

    useEffect(() => {
        if (props.focusComment !== null && props.focusComment !== undefined) {
            if (props.replies === undefined) {
                commentRef.current?.scrollIntoView({ behavior: 'smooth', block: 'center', inline: 'center' })
            } else if (props.replies.length !== 0) {
                commentRef.current?.scrollIntoView({ behavior: 'smooth', block: 'center', inline: 'center' })
            }
        }
    }, [props.focusComment, props.replies])

    const showReplies = () => {
        if (props.showLess) {
            props.showModal()
        } else {
            setShowReply(true)
        }
    }

    return (
        <div>
            <div ref={props.focusComment !== null && props.focusComment !== undefined && props.comment.id === props.focusComment ? commentRef : null}
             className="comment">
                <Link to={`/profile/${props.comment.user.id}`}>
                    <img src={props.comment.user.avatar} alt="" />
                </Link>
                <div className='comment-content'>
                    <div className={`info ${props.focusComment !== null && props.focusComment !== undefined && props.comment.id === props.focusComment ? "animate__animated animate__flash" : " "}`}>
                        <Link to={`/profile/${props.comment.user.id}`} style={{ textDecoration: "none", color: "inherit" }}>
                            <span>{props.comment.user.lastName} {props.comment.user.firstName}</span>
                        </Link>
                        <p>
                            {props.comment.level !== 1 && <span className='repliedUserInfo'>{props.comment.repliedUser.lastName} {props.comment.repliedUser.firstName} </span>}
                            {props.comment.content}
                        </p>
                    </div>
                    <div className='comment-action'>
                        <div className="reply" onClick={reply}>Phản hồi</div>
                        <span className="date"><Moment locale="vi" fromNow>{formattedDate}</Moment></span>
                        {(props.post.user.id === user.id || props.comment.user.id === user.id) &&
                            <>
                                <div className="dropdown">
                                    <MoreHorizIcon className='comment-more' />
                                    <div className="dropdown-content">
                                        {props.post.user.id === user.id ? <>
                                            {props.comment.user.id === user.id && <span onClick={() => props.setEditCommentId(props.comment.id)}><EditIcon /> Sửa </span>}
                                            <span onClick={() => {
                                                props.setDeleteCommentShow(true)
                                                props.setComment(props.comment)
                                            }}> <DeleteIcon /> Xóa</span>
                                        </> : props.comment.user.id === user.id && <>
                                            <span ><EditIcon onClick={() => props.setEditCommentId(props.comment.id)} /> Sửa </span>
                                            <span onClick={() => {
                                                props.setDeleteCommentShow(true)
                                                props.setComment(props.comment)
                                            }}> <DeleteIcon /> Xóa</span>
                                        </>}
                                    </div>
                                </div>
                            </>}
                    </div>
                    {props.comment.repliesTotal !== null && props.comment.repliesTotal !== 0 && !showReply && <div className="replied" onClick={() => showReplies(props.comment.id)}>
                        <ReplyIcon /> <div className='repliedInfo'><span className="repliedUser">{props.comment.firstReply.user.lastName} {props.comment.firstReply.user.firstName}</span> <span> đã trả lời
                        </span></div><div>•</div> <div className='repliedTotal'>{props.comment.repliesTotal} phản hồi</div>
                    </div>}
                    {showReply &&
                        <>
                            <div className="replyList">
                                {replies !== null && replies.map((comment) => {
                                    return (
                                        <div key={comment.id}>
                                            {editCommentId === comment.id ?
                                                <div className="edit-comment">
                                                    <EditComment comments={replies} setComments={setReplies} showLess={props.showLess} setEditCommentId={setEditCommentId} post={props.post} comment={comment} setReloadComment={setReloadReplies} />
                                                </div>
                                                :
                                                <CommentItem focusComment={props.focusComment} replies={replies} setRepliedComment={setRepliedComment} setRepliedUser={setRepliedUser} parentComment={props.parentComment} replyCommentId={props.replyCommentId} setReplyCommentId={props.setReplyCommentId} showLess={props.showLess} showModal={props.showModal} setEditCommentId={setEditCommentId} post={props.post} setDeleteCommentShow={setDeleteReplyShow} setComment={setComment} comment={comment} setReloadComment={setReloadReplies} />}
                                        </div>
                                    )
                                })}
                            </div>
                            {props.replyCommentId === props.comment.id && <div className="replyInput">
                                <UploadComment repliedComment={repliedComment} repliedUser={repliedUser} post={props.post} setReloadComment={setReloadReplies} />
                            </div>}
                            <div className="replied hideLess" onClick={() => setShowReply(false)}>
                                <KeyboardDoubleArrowUpIcon /> <div className='repliedInfo'>Ẩn bớt</div>
                            </div>
                            <DeleteConfirmation show={deleteReplyShow} onHide={() => setDeleteReplyShow(false)} comment={comment} comments={replies} setComment={setComment} setComments={setReplies} />
                        </>
                    }
                </div>
            </div>
        </div>
    )
}
