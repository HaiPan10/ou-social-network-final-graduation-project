import React, { useContext, useState } from 'react'
import './comment.scss'
import { authAPI, endpoints } from '../../configs/Api'
import SendIcon from '@mui/icons-material/Send';
import { AuthContext } from '../../context/AuthContext';
import CheckIcon from '@mui/icons-material/Check';
import CancelIcon from '@mui/icons-material/Cancel';

export const EditComment = (props) => {
    const [user, dispatch] = useContext(AuthContext)
    const [content, setContent] = useState(props.comment.content)
    const editComment = (evt) => {
        evt.preventDefault()
        const process = async () => {
            try {
                setContent('')
                let res = await authAPI().patch(endpoints['comment'], {
                    "id": props.comment.id,
                    "content": content
                })
                if (res.status === 200) {
                    cancel()
                    if (props.showLess) {
                        props.setComments(prevState => {
                            let data = [...prevState]
                            let index = data.findIndex(item => item.id === props.comment.id);
                            if (index !== -1) {
                                data[index] = {
                                    ...data[index],
                                    content: content
                                };
                            }
                            return data;
                        })
                    } else {
                        props.setReloadComment(true)
                    }
                }
            } catch (ex) {
            }
        }

        if (content !== props.comment.content) {
            process()
        } else {
            cancel()
        }
    }
    const cancel = () => {
        setContent(props.comment.content)
        props.setEditCommentId(null)
    }
    return (
        <div className="edit">
            <form onSubmit={editComment}>
                <img src={user.avatar} alt="" />
                <input type="text" value={content} maxLength="255" required onChange={(e) => { setContent(e.target.value) }} placeholder="sửa bình luận" />
                <button onClick={cancel}><CancelIcon /></button>
                <button type='submit'><CheckIcon /></button>
            </form>
        </div>
    )
}
