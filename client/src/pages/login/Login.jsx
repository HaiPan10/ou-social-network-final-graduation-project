import { Link, Navigate } from "react-router-dom";
import "./login.scss"
import { AuthenBackground } from "../../components/authenBackground/AuthenBackground";
import { useContext, useState } from "react";
import Api, { endpoints } from "../../configs/Api";
import ErrorAlert from "../../components/ErrorAlert";
import { save, load } from 'react-cookies';
import { AuthContext } from "../../context/AuthContext";

export const Login = () => {
  const [err, setErr] = useState()
  const [disableButton, setDisableButton] = useState()
  const [user, userDispatch] = useContext(AuthContext)

  const [account, setAccount] = useState({
    "id": "",
    "email": "",
    "password": "",
  })

  const login = (evt) => {
    evt.preventDefault()
    setDisableButton(true);

    const process = async () => {
        try {
            let res = await Api.post(endpoints['login'], {
              "email": account.email,
              "password": account.password,
            })
            save('access-token', res.data.accessToken)
            save('current-user', res.data.user)
            // save('firebase-email', account.email)
            // save('firebase-password', account.password)
            save('role', res.data.role)

            userDispatch({
              "type": "LOGIN", 
              "payload": res.data.user
            })
        } catch (ex) {
          setErr(ex.response.data)
          setDisableButton(false);
        }
    }

    process()
  }

  if (user !== null) {
    return <Navigate to="/" />
  }

  return (
    <div className="theme-light">
      <AuthenBackground/>
      <div className="login">
        <div className="card">
          <div className="left">
          <div className="left-top">
              {/* <h1>Mạng xã hội cựu sinh viên trường đại học Mở TP.HCM</h1>
              <p>
                Chức năng này giành riêng cho cựu sinh viên trường đại học Mở TP.HCM. Vui lòng cung cấp đủ thông tin cần thiết!
              </p> */}
            </div>
            <div className="left-bottom">
              <span>Bạn chưa có tài khoản?</span>
              <Link to="/register">
                <button>Đăng ký ngay</button>
              </Link>
            </div>
          </div>
          <div className="right">
            <h1>Đăng nhập</h1>
            <form onSubmit={login}>
              {err?<ErrorAlert err={err} />:""}
              <input type="email" placeholder="Email" value={account.email} onChange={(e) => setAccount(account => ({...account, ["email"]:e.target.value}))} required/>
              <input type="password" placeholder="Mật khẩu" value={account.password} onChange={(e) => setAccount(account => ({...account, ["password"]:e.target.value}))} required/>
              <button disabled={disableButton}>Đăng nhập</button>
            </form>
            <p>Mạng xã hội cựu sinh viên trường đại học Mở TP.HCM được hình thành năm 2023 nhằm đáp ứng nhu cầu giao tiếp giữa trường và các cựu sinh viên sau khi ra trường.</p>
          </div>        
        </div>
      </div>
    </div>
  );
}
