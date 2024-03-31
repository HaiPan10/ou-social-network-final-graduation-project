import "./home.scss"
import { Post } from "../../components/post/Post"
import { useContext, useEffect, useRef, useState } from "react"
import { PostLayout } from "../../components/postLayout/PostLayout"
import { AuthContext } from "../../context/AuthContext"
import { authAPI, endpoints } from "../../configs/Api"
import { ReloadContext } from "../../context/ReloadContext"
// import {FireBaseTest} from "../../components/firebaseTest/FireBaseTest"
import Loading from "../../components/Loading"
import KeyboardCapslockIcon from '@mui/icons-material/KeyboardCapslock';
import { InView } from 'react-intersection-observer';
import { PostInvitation } from "../../components/post/PostInvitation"
import { PostSurvey } from "../../components/post/PostSurvey"
import { LeftBarContext } from "../../context/LeftBarContext"
import { load } from "react-cookies"
import { SocketContext } from "../../context/SocketContext"

export const Home = () => {
  const [posts, setPosts] = useState(null)
  const [user, userDispatch] = useContext(AuthContext)
  const { reload } = useContext(ReloadContext)
  const [page, setPage] = useState(1)
  const [isLoading, setIsLoading] = useState(false)
  const [isCaughtUp, setIsCaughtUp] = useState(false)
  const { reloadData } = useContext(ReloadContext)
  const { setCurrentMenu } = useContext(LeftBarContext)
  const [socketClient] = useContext(SocketContext)
  const [firstInit, setFirstInit] = useState(true)
  const [inViewList, setInViewList] = useState([])

  useEffect(() => {
    return () => {
      if (socketClient !== null) {
        socketClient.unsubscribe("home")
      }
    }
  }, [socketClient])

  useEffect(() => {
    if (socketClient !== null && posts !== null && firstInit) {
      setFirstInit(false)
      socketClient.subscribe('/home', (payload) => {
        let newPost = JSON.parse(payload.body).post
        if (newPost.userId.id !== user.id) {
          // setPosts(prevPosts => [...prevPosts, newPost])
          setPosts(prevState => {
            let data = [...prevState]
            let index = data.findIndex(item => item.isRendered === false);
            data[index] = {
              ...newPost,
              isRendered: false,
            }
            return data;
          })
        }
      }, { id: "home", user: user.id })
    }
  }, [socketClient, posts, inViewList])

  useEffect(() => {
    const loadNewFeeds = async () => {
      setIsLoading(true)
      try {
        let res = await authAPI().get(endpoints['posts'] + `?page=1`)
        setIsLoading(false)
        setPosts(res.data.map(post => ({ ...post, isRendered: false })))
        // setPosts(res.data)
      } catch (ex) {
        setIsCaughtUp(true)
        // console.clear()
      } finally {
        setIsLoading(false)
      }
    }

    setCurrentMenu('home')
    setInViewList([])
    setPage(1)
    setIsCaughtUp(false)
    setFirstInit(true)
    loadNewFeeds()
    window.scrollTo({ top: 0, left: 0, behavior: 'instant' })
  }, [reload])

  useEffect(() => {
    const loadNewFeeds = async () => {
      setIsLoading(true)
      try {
        let res = await authAPI().get(endpoints['posts'] + `?page=${page}`)
        setIsLoading(false)
        let updatedPosts = res.data.map(post => ({ ...post, isRendered: false }));
        setPosts(prevPosts => [...prevPosts, ...updatedPosts]);
        // setPosts(prevPosts => [...prevPosts, ...res.data])
      } catch (ex) {
        setIsCaughtUp(true)
        console.clear()
      } finally {
        setIsLoading(false)
      }
    }

    if (page > 1) {
      loadNewFeeds()
    }
  }, [page])

  const handleIntersection = (inView) => {
    if (inView && !isLoading && !isCaughtUp) {
      setPage(page + 1);
    }
  }

  // useEffect(() => {
  //   console.log(posts)
  // }, [posts])

  const postInView = (post) => {
    // console.log(" POST " + post.id + " RENDERED!")
    setPosts(prevState => {
      let data = [...prevState]
      let index = data.findIndex(item => item.id === post.id);
      data[index] = {
          ...data[index],
          isRendered: true,
      }
      return data;
    })
  }

  return (
    <div className="home">
      <div className="posts">
        {posts === null ? <Loading/> : 
        <>
          <PostLayout/>
            {posts.length !== 0 && posts.map(post=> (
                  post.postSurvey !== null ?
                    <PostSurvey postInView={() => postInView(post)} post={post} key={post.id} posts={posts} setPosts={setPosts}/>
                  :
                  post.postInvitation !== null ?
                    <PostInvitation postInView={() => postInView(post)} post={post} key={post.id} posts={posts} setPosts={setPosts}/>
                  :
                  <Post postInView={() => postInView(post)} key={post.id} post={post} posts={posts} setPosts={setPosts}/>
          ))}
          {isLoading && <div className="bottom-loading">
            <Loading/>
          </div>}
          {isCaughtUp && <div className="caught-up" onClick={() => {
            reloadData()
            setPage(1)
          }}>
            <div className="caught-up-content">Bạn đã xem hết nội dung hôm nay rồi!</div>
            <button><KeyboardCapslockIcon/> Đi tới trang đầu</button>
          </div>}
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
        </>
        }
      </div>
    </div>
  )
}
