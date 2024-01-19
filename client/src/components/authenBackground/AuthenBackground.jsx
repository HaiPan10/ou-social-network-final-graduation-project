import React from 'react'
import './authenBackground.scss'
import ParticlesComponent from '../Particles'

export const AuthenBackground = () => {
    return (
        <div className='authenBackground'>
            <div className='logo'></div>
            <div className='top'></div>
            <div className='bottom'></div>
            <ParticlesComponent id="tsparticles"/>
        </div>
    )
}
