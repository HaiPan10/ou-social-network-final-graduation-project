import React, { useContext, useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import './postDetail.scss'
import { authAPI, endpoints } from '../../configs/Api'
import { Post } from '../../components/post/Post'
import { PostSurvey } from '../../components/post/PostSurvey'
import { PostInvitation } from '../../components/post/PostInvitation'
import SearchOffIcon from '@mui/icons-material/SearchOff';
import { SocketContext } from '../../context/SocketContext'

export const PostDetail = () => {
  const { id } = useParams()
  const [post, setPost] = useState(null)
  const [socketClient] = useContext(SocketContext)
  const [render, setRender] = useState(false)
  const location = useLocation()

  let focusComment = null
  let focusParent = null

  if (location.state !== null) {
    focusComment = location.state.focusComment
    focusParent = location.state.focusParent
  }

  useEffect(() => {
    const getPost = async () => {
      try {
        let res = await authAPI().get(endpoints['posts'] + `/${id}`)
        setPost(res.data)
      } catch (ex) {
        setPost('empty')
        console.clear()
      } finally {
      }
    }

    setPost(null)
    getPost()
  }, [id])

  useEffect(() => {
    if (socketClient !== null) {
      setRender(true)
    }
  }, [socketClient])

  return (
    <div className='postDetail'>
      {render && post !== null &&
        <>
           {post !== 'empty' ?
            <>
              {post.postSurvey !== null ?
                <PostSurvey focusParent={focusParent} focusComment={focusComment} post={post}/>
              :
              post.postInvitation !== null ?
                <PostInvitation post={post}/>
              :
              <Post focusParent={focusParent} focusComment={focusComment} post={post}/>}
            </> :
            <div className="unavailablePost">
              <SearchOffIcon/> Rất tiếc! Bài đăng này không tồn tại
            </div>
          }
        </>
      }
    </div>
  )
}
