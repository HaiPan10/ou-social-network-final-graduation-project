import React from 'react'
import { DarkModeContext } from "../../context/DarkModeContext";
import Modal from 'react-bootstrap/Modal';
import "./reaction.scss"
import { AuthContext } from "../../context/AuthContext"
import { useContext, useEffect, useRef, useState } from "react"
import SentimentVerySatisfiedRoundedIcon from '@mui/icons-material/SentimentVerySatisfiedRounded';
import ThumbUpRoundedIcon from '@mui/icons-material/ThumbUpRounded';
import FavoriteRoundedIcon from '@mui/icons-material/FavoriteRounded';
import { authAPI, endpoints } from '../../configs/Api';
import Loading from '../Loading';
import { Link } from 'react-router-dom';

const Reaction = (props) => {
    const {darkMode} = useContext(DarkModeContext)
    const [user, userDispatch] = useContext(AuthContext)
    const [users, setusers] = useState(null)

    useEffect(() => {
        const loadUserReactions = async () => {
            try {
                let res = await authAPI().get(endpoints['post_reactions'] + `/${props.post.id}` + `/${props.reaction}`)
                if (res.status === 200) {
                    console.log(res.data)
                    setusers(res.data)
                }
            } catch (ex) {
            }
        }

        if (props.show === true) {
            loadUserReactions()
        }
    }, [props.show]);    

    return (
        <Modal
          {...props}
          aria-labelledby="contained-modal-title-vcenter"
          centered
          className={`theme-${darkMode ? "dark" : "light"}`}
          id='modal-reaction'
        >
        <Modal.Header closeButton>
            <Modal.Title id="contained-modal-title-vcenter">
                {props.reaction === 1 ? <div className='reaction-title'><ThumbUpRoundedIcon/>{props.total}<span>lượt thích</span></div> : 
                    props.reaction === 2 ? <div className='reaction-title'><SentimentVerySatisfiedRoundedIcon/>{props.total}<span>lượt haha</span></div> :
                    <div className='reaction-title'><FavoriteRoundedIcon/>{props.total}<span> lượt yêu thích </span></div>
                }
            </Modal.Title>
        </Modal.Header>
        <Modal.Body className="reaction-body">
            {users !== null ? 
                users.map(user=>(
                    <Link to={`/profile/${user.id}`} style={{textDecoration:"none", color:"inherit"}}>
                        <div key={user.id} className="user_list">
                            <div className="avatar"><img src={user.avatar} alt="" /></div>
                            <div className="name">{user.lastName} {user.firstName}</div>
                        </div>
                    </Link>
                ))
            : <Loading/>
            }
        </Modal.Body>
        <Modal.Footer>
        </Modal.Footer>
        </Modal>
    );
  }

export default Reaction