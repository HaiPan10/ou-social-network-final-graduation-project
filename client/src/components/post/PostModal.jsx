import { useContext, useEffect } from "react"
import { Button, Modal } from "react-bootstrap"
import { DarkModeContext } from "../../context/DarkModeContext"
import './post.scss'
import { PostDetail } from "../../pages/postDetail/PostDetail"
import { PostInvitation } from "./PostInvitation"
import { Post } from "./Post"
import { PostSurvey } from "./PostSurvey"

export const PostModal = (props) => {
    const { darkMode } = useContext(DarkModeContext)

    return (
        <Modal
            {...props}
            aria-labelledby="contained-modal-title-vcenter"
            centered
            className={`theme-${darkMode ? "dark" : "light"}`}
        >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Bài viết của {props.post.userId.lastName + ' ' + props.post.userId.firstName}
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {props.post.postSurvey !== null ?
                    <PostSurvey post={props.post} />
                :
                props.post.postInvitation !== null ?
                    <PostInvitation post={props.post} />
                :
                    <Post post={props.post} />
                }
            </Modal.Body>
        </Modal>
    )
}
