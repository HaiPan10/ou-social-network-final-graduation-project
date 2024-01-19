import React from 'react'
import './rightBar.scss'
import { Notification } from '../notification/Notification'

export const RightBar = () => {
  return (
    <>
      <div className='rightBar'>
        <div className="container">
          <Notification/>
        </div>
      </div>
    </>    
  )
}
