import React, { Component } from 'react';
import './ItemListing.css';

import productImage from '../item-image-placeholder.svg';
import {Link} from "react-router-dom";


class ItemListing extends Component {

    render() {
        return (
            <div className="listing-content">
                <div className="item-image">
                    <img src={productImage} alt="placeholder item image" className="item-image-icon"/>
                </div>
                <div className="item-name">
                    <Link to={`/items/${this.props.item.id}`}>this.props.item.name</Link>
                </div>
                <div className="item-bid">
                    {this.props.currentBid}
                </div>
            </div>
        )
    }
}

export default ItemListing