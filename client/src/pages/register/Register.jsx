import { Link, Navigate, useNavigate } from "react-router-dom"
import "./register.scss"
import { AuthenBackground } from "../../components/authenBackground/AuthenBackground";
import { useContext, useState } from "react";
import Api, { endpoints } from "../../configs/Api";
import ErrorAlert from "../../components/ErrorAlert";
import { AuthContext } from "../../context/AuthContext";
import { save } from "react-cookies";
import { createUserWithEmailAndPassword, updateProfile } from "firebase/auth";
import { auth, db } from "../../configs/firebase";
import { doc, setDoc } from "firebase/firestore";
export const Register = () => {
  const [err, setErr] = useState()
  const [disableButton, setDisableButton] = useState()
  
  const [user, setUser] = useState({
    "firstName": "",
    "lastName": "",
  })

  const [account, setAccount] = useState({
    "email": "",
    "password": "",
    "confirmPassword": "",
  })

  const [userStudent, setUserStudent] = useState({
    "studentIdentical": "",
  })

  
  const [step, setStep] = useState(1);

  const [authUser, userDispatch] = useContext(AuthContext)
  
  if (authUser !== null) {
    return <Navigate to="/" />
  }

  // const createFirebaseAuth = async (res) => {
  //   try {
  //     const userCredential = await createUserWithEmailAndPassword(auth, account.email, account.password);

  //     if (userCredential && auth.currentUser) {
  //       try {
  //         await updateProfile(auth.currentUser, {
  //           displayName: user.lastName + " " + user.firstName,
  //           photoURL: "https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png"
  //         });

  //         await setDoc(doc(db, "users", auth.currentUser.uid), {
  //           uid: auth.currentUser.uid,
  //           user_id: res.user.id,
  //           displayName: auth.currentUser.displayName,
  //           photoURL: "https://res.cloudinary.com/dxjkpbzmo/image/upload/v1691907285/zp0am1x1g5puovvwfvzv.png",
  //         });
  //       } catch (error) {
  //         console.log(error);
  //       }
  //     }
  //   } catch (ex) {
  //     // console.log(ex);
  //   }
  // };

  const register = (evt) => {
    evt.preventDefault()
    setDisableButton(true);

    const process = async () => {
        try {
            let res = await Api.post(endpoints['register'], {
              "email": account.email,
              "password": account.password,
              "confirmPassword": account.confirmPassword,
              "user": {
                "firstName": user.firstName,
                "lastName": user.lastName,
                "userStudent": {
                  "studentIdentical": userStudent.studentIdentical
                }
              }
            })
            
            if (res.status === 201) {
              // await createFirebaseAuth(res.data)
              save('access-token', res.data.accessToken)
              save('current-user', res.data.user)
              // save('firebase-email', account.email)
              // save('firebase-password', account.password)
              save('role', res.data.role)
              userDispatch({
                "type": "LOGIN", 
                "payload": res.data.user
              })
            }
        } catch (ex) {
          setErr(ex.response.data)
          setDisableButton(false);
        }
    }

    if (account.password !== account.confirmPassword) {
      setErr("Mật khẩu không khớp!")
    }
    else {
      process()
    }
  }

  const nextStep = () => {
    setStep(step + 1);
  };

  const prevStep = () => {
    setStep(step - 1);
  };

  const renderForm = () => {
    switch (step) {
      case 1:
        return (
          <div className="input-field">
            <input type="email" placeholder="Email" value={account.email} onChange={(e) => setAccount(account => ({...account, ["email"]:e.target.value}))} required/>
            <input type="password" placeholder="Mật khẩu" value={account.password} onChange={(e) => setAccount(account => ({...account, ["password"]:e.target.value}))} required/>
            <input type="password" placeholder="Xác nhận mật khẩu" value={account.confirmPassword} onChange={(e) => setAccount(account => ({...account, ["confirmPassword"]:e.target.value}))} required/>
          </div>
        );
      case 2:
        return (
          <div className="input-field">
              <input type="text" placeholder="Họ" value={user.lastName} minLength="1" maxLength="20" onChange={(e) => setUser(user => ({...user, ["lastName"]:e.target.value}))} required/>
              <input type="text" placeholder="Tên" value={user.firstName} minLength="1" maxLength="10" onChange={(e) => setUser(user => ({...user, ["firstName"]:e.target.value}))} required/>
              <input type="text" placeholder="Mã số sinh viên" minLength="10" maxLength="10" value={userStudent.studentIdentical} onChange={(e) => setUserStudent(userStudent => ({...userStudent, ["studentIdentical"]:e.target.value}))} required/>
            </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="theme-light">
      <AuthenBackground/>
        <div className="register">
          <div className="card">
            <div className="right">
              <div className="right-top">
                {/* <h1>Mạng xã hội cựu sinh viên trường đại học Mở TP.HCM</h1> */}
                {/* <p>
                  Chức năng này giành riêng cho cựu sinh viên trường đại học Mở TP.HCM.
                </p> */}
              </div>
              <div className="right-bottom">
                <span>Bạn đã có tài khoản?</span>
                <Link to="/">
                  <button>Đăng nhập ngay</button>
                </Link>
              </div>
            </div>
            <div className="left">
            <h1>Đăng ký</h1>
            <form onSubmit={register}>
              {renderForm()}

              {err?<ErrorAlert err={err} />:""}
              
              <div className="btn-field">
                {step > 1 && <button className="btn-back" onClick={prevStep}>Quay lại</button>}
                {step < 2 ? (
                  <button className="btn-next" onClick={nextStep}>Tiếp tục</button>
                ) : (
                  <button disabled={disableButton} className="btn-submit" type="submit">Đăng ký</button>
                )}
              </div>
            </form>
            <p>Chức năng đăng ký chỉ giành cho cựu sinh viên trường Đại học Mở TP.HCM! Vui lòng cung cấp thông tin chính xác.</p>
          </div>
        </div>
      </div>
    </div>
  )
}