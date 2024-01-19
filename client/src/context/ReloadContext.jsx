import { createContext, useEffect, useState } from "react";

export const ReloadContext = createContext()

export const ReloadContextProvider = ({children}) => {
    const [reload, setReload] = useState (
        JSON.parse(localStorage.getItem("reload")) || true
    )

    const reloadData = () => {
        setReload(!reload)
    }

    useEffect(() => {
        localStorage.setItem("reload", reload)
    }, [reload])

    return (
        <ReloadContext.Provider value={{ reload, reloadData }}>
            {children}
        </ReloadContext.Provider>
    )
}