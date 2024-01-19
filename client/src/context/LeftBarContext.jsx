import { createContext, useState } from "react";

export const LeftBarContext = createContext()

export const LeftBarContextProvider = ({children}) => {
    const [showChat, setShowChat] = useState (false)
    const [showSearch, setShowSearch] = useState (false)
    const [currentMenu, setCurrentMenu] = useState('home')
    const [smallLeftBar, setSmallLeftBar] = useState(false)

    return (
        <LeftBarContext.Provider value={{ setShowChat, showChat, showSearch, setShowSearch, currentMenu, setCurrentMenu, smallLeftBar, setSmallLeftBar}}>
            {children}
        </LeftBarContext.Provider>
    )
}