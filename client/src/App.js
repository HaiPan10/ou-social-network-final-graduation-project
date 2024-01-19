import { RouterProvider, createBrowserRouter } from "react-router-dom";
import { Login } from "./pages/login/Login";
import { Register } from "./pages/register/Register";
import { Profile } from "./pages/profile/Profile";
import { Home } from "./pages/home/Home";
import { AuthContext } from "./context/AuthContext";
import { useReducer } from "react";
import userReducer from "./reducers/userReducer";
import { load } from 'react-cookies';
import { Layout } from "./components/layout/Layout";
import { DarkModeContextProvider } from "./context/DarkModeContext";
import { ReloadContextProvider } from "./context/ReloadContext";
import './index.css'
import { Chat } from "./pages/chat/Chat";
import { LeftBarContextProvider } from "./context/LeftBarContext";
import { PostDetail } from "./pages/postDetail/PostDetail";

const App = () => {
  const [user, dispatch] = useReducer(userReducer, load('current-user') || null)

  const router = createBrowserRouter([
    {
      path:"/",
      element: (
        <DarkModeContextProvider>
          <ReloadContextProvider>
            <LeftBarContextProvider>              
                <Layout/>
            </LeftBarContextProvider>
          </ReloadContextProvider>
        </DarkModeContextProvider>
      ),
      children: [
        {
          path: "/",
          element:<Home />,
        },
        {
          path: "/profile/:id",
          element: <Profile />,
        },
        {
          path: "/chat",
          element: <Chat />,
        },
        {
          path: "/chat/:id",
          element: <Chat />,
        },
        {
          path: "/post/:id",
          element: <PostDetail />,
        }
      ],
    },
    {
      path:"/login",
      element:<Login/>,
    },
    {
      path:"/register",
      element:<Register/>,
    },
  ])

  return (    
    <div>
      <AuthContext.Provider value={[user, dispatch]}>
          <RouterProvider router={router}/>
      </AuthContext.Provider>
    </div>
  );
}

export default App;
