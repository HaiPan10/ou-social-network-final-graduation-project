import React, { useContext } from 'react'
import './accountLocked.scss'
import { AuthContext } from '../../context/AuthContext'

export const AccountLocked = () => {
    const [user, dispatch] = useContext(AuthContext)
    const logout = (evt) => {
        evt.preventDefault()
        dispatch({
          "type": "LOGOUT",
        })
      }

    return (
        <div>
            <div className="accountLocked">
                <div className="card">
                    <div className='title'>
                        <img src={require('../../images/account_locked.png')} />
                        <h1>Tài khoản bị khóa</h1>
                    </div>
                    <div className='content'>
                        <p>Tài khoản của bạn đã bị khóa!</p>
                        <p>Chúng tôi rất tiếc phải thông báo tài khoản của bạn đã bị khóa.
                            Vui lòng liên hệ với quản trị viên để được mở khóa tài khoản.
                        </p>
                    </div>
                    <div className='logout'>
                        <button onClick={logout}>Đăng xuất</button>
                    </div>
                </div>
            </div>
        </div>
    )
}
