import React, { Component } from 'react';
import { Image, Modal, Grid, Row, Col } from 'react-bootstrap';
import Lightbox from 'react-image-lightbox';

class ImageModal extends Component {
    constructor(props) {
        super(props)
        this.state = {
            images: props.images || [],
            currentImageIndex: props.index
        };

        this.onMovePrevRequest = this.onMovePrevRequest.bind(this);
        this.onMoveNextRequest = this.onMoveNextRequest.bind(this);
    }

    onMovePrevRequest() {
        const {currentImageIndex, images} = this.state;

        this.setState({
            currentImageIndex: (currentImageIndex + images.length - 1) % images.length,
        })
    }

    onMoveNextRequest() {
        const {currentImageIndex, images} = this.state;

        this.setState({
            currentImageIndex: (currentImageIndex + 1) % images.length,
        })
    }

    render(){
        const {images, currentImageIndex} = this.state;
        const {onClose, index} = this.props;
        const totalImages = images.length;

        return(
            <Lightbox
                mainSrc={images[currentImageIndex]}
                nextSrc={totalImages > 1 ? images[(currentImageIndex + 1) % images.length] : null}
                prevSrc={totalImages > 1 ? images[(currentImageIndex + images.length - 1) % images.length] : null}
                onCloseRequest={onClose}
                onMovePrevRequest={totalImages > 1 ? this.onMovePrevRequest : null}
                onMoveNextRequest={totalImages > 1 ? this.onMoveNextRequest : null}
            />
    )
    }

}

export default ImageModal;