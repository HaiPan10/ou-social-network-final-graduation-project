import React, { useContext } from 'react'
import Api, { authAPI, endpoints } from "../../configs/Api";
import './emailVerification.scss'
import { AuthContext } from '../../context/AuthContext';

export const EmailVerification = (props) => {
    const resend = (evt) => {
        evt.preventDefault()
        const process = async () => {
            try {
                let res = await authAPI().get(endpoints['verify'])
                if (res.status === 200) {
                    console.log("resent email!")
                }
            } catch (ex) {
                console.log("resent failed!")
            }
        }

        process()
    }

    const [user, dispatch] = useContext(AuthContext)
    const logout = (evt) => {
        evt.preventDefault()
        dispatch({
          "type": "LOGOUT",
        })
      }

    return (
        <div>
            <div className="emailVerification">
                <div className="card">
                    <div className='title'>
                        <img src={require('../../images/email_verification.png')} />
                        <h1>Xác thực email</h1>
                    </div>
                    <div className='content'>
                        <p>Tài khoản của bạn cần được xác thực email!</p>
                        <p>Chúng tôi đã gửi mail xác thực về email của bạn, vui lòng nhấn vào đường link trong mail.</p>
                        <button onClick={resend}>Gửi lại email</button>
                    </div>
                    <div className='logout'>
                        <button onClick={logout}>Đăng xuất</button>
                    </div>
                </div>
            </div>
        </div>
    )
}
