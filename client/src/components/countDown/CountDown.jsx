
import React from 'react';
import moment from 'moment';
import './countDown.scss'

class CountDown extends React.Component {
    state = {
        days: undefined,
        hours: undefined,
        minutes: undefined,
        seconds: undefined
    };
    
    
    componentDidMount() {
        this.interval = setInterval(() => {
            const { thenInMS } = this.props;
            const countdown = new Date(thenInMS).getTime() - new Date().getTime();
            const days = Math.floor(countdown / (1000 * 60 * 60 * 24));
            const hours = Math.floor((countdown % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((countdown % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((countdown % (1000 * 60)) / 1000);
            this.setState({ days, hours, minutes, seconds });
        }, 1000);
    }
    
    componentWillUnmount() {
        if (this.interval) {
            clearInterval(this.interval);
        }
    }
    
    render() {
        const { days, hours, minutes, seconds } = this.state;
        
        return (
            <>
            {days + hours + minutes + seconds <= 0 ?
                <div className="countdown-wrapper">
                    <div className="expired">Sự kiện đã diễn ra</div>
                </div>
            :
                <div className="countdown-wrapper">
                    <div className="countdown-item">
                        {days}
                        <span>ngày</span>
                    </div>
                    <div className="countdown-item">
                        {hours}
                        <span>giờ</span>
                    </div>
                    <div className="countdown-item">
                        {minutes}
                        <span>phút</span>
                    </div>
                    <div className="countdown-item">
                        {seconds}
                        <span>giây</span>
                    </div>
                </div>
            }
            </>
        );
    }
}
export default CountDown;