import React, { Component } from 'react';
import './ItemListing.css';

import productImage from '../item-image-placeholder.svg';
import {Card} from 'antd';
import {Link} from "react-router-dom";


class ItemListing extends Component {

    render() {
        return (
            <Card.Grid>
                <div className="listing-content">
                    <div className="item-image">
                        <img src={productImage} alt="placeholder item image" className="item-image-icon"/>
                    </div>
                    <div className="item-name">
                        <Link to={`/items/${this.props.item.id}`}>{this.props.item.itemName}</Link>
                    </div>
                    <div className="item-bid">
                        {this.props.item.currentBid}
                    </div>
                </div>
            </Card.Grid>
        )
    }
}

export default ItemListing