import React, { useContext, useEffect, useRef, useState } from 'react'
import './comment.scss'
import { authAPI, endpoints } from '../../configs/Api'
import SendIcon from '@mui/icons-material/Send';
import { AuthContext } from '../../context/AuthContext';
import ReplyIcon from '@mui/icons-material/Reply';

export const UploadComment = (props) => {
    const [user, dispatch] = useContext(AuthContext)
    const [content, setContent] = useState('')    
    const inputRef = useRef()
    const uploadComment = (evt) => {
        evt.preventDefault()
        const process = async () => {
            try {
                setContent('')
                let res = await authAPI().post(endpoints['comment'] + `/${props.post.id}` + (props.repliedComment !== undefined ? `/${props.repliedComment.id}` : ''), {
                    "content": content,
                    "level": props.repliedComment !== undefined ? null : 1
                })
                if (res.status === 201) {
                    // props.setReloadComment(true)
                }
            } catch (ex) {
            }
        }

        process()
    }

    useEffect(() => {
        inputRef.current?.scrollIntoView({ behavior: 'smooth', block: 'center', inline: 'center' });
    }, [])

    return (
        <div className="write">
            <form onSubmit={uploadComment}>
                <img src={user.avatar} alt="" />
                {/* <input type="text" value={content} maxLength="255" required onChange={(e) => { setContent(e.target.value) }} placeholder="Viết bình luận" />
                <button type='submit'><SendIcon /></button> */}
                <div className='inputField'>
                    <div className="inputBar">
                        <input autoFocus ref={inputRef} type="text" value={content} maxLength="255" required onChange={(e) => { setContent(e.target.value) }} placeholder="Viết bình luận" />
                        <button type='submit'><SendIcon /></button>
                    </div>
                    {props.repliedComment !== undefined && <div className="repliedUser">
                        <ReplyIcon/> Phản hồi bình luận của <b>{props.repliedUser.lastName} {props.repliedUser.firstName}</b>
                    </div>}
                </div>
            </form>
        </div>
    )
}
