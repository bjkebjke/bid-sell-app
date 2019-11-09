import React, { Component } from 'react';
import './Item.css';
import { Avatar, Icon } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { formatDateTime } from '../util/Helpers';
import productImage from '../item-image-placeholder.svg';
import {getItem} from '../util/APIUtils';


import { Button } from 'antd';

class Item extends Component {
    constructor(props) {
        super(props);
        this.state = {
            item: null,
            isLoading: false
        }
        this.loadItem = this.loadItem.bind(this);
    }

    getTimeRemaining = (item) => {
        const expirationTime = new Date(item.expirationDateTime).getTime();
        const currentTime = new Date().getTime();

        var difference_ms = expirationTime - currentTime;
        var seconds = Math.floor( (difference_ms/1000) % 60 );
        var minutes = Math.floor( (difference_ms/1000/60) % 60 );
        var hours = Math.floor( (difference_ms/(1000*60*60)) % 24 );
        var days = Math.floor( difference_ms/(1000*60*60*24) );

        let timeRemaining;

        if(days > 0) {
            timeRemaining = days + " days left";
        } else if (hours > 0) {
            timeRemaining = hours + " hours left";
        } else if (minutes > 0) {
            timeRemaining = minutes + " minutes left";
        } else if(seconds > 0) {
            timeRemaining = seconds + " seconds left";
        } else {
            timeRemaining = "less than a second left";
        }

        return timeRemaining;
    }

    loadItem(itemId) {
        this.setState({
            isLoading: true
        });

        getItem(itemId)
            .then(response => {
                this.setState({
                    item: response,
                    isLoading: false
                });
            }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });
            }
        });
    }

    componentDidMount() {
        const itemId = this.props.match.params.itemId;
        this.loadItem(itemId);
    }

    render() {
        if(this.state.item.selectedChoice || this.state.item.expired) {
            // logic for already bidded or expired item
        } else {
            // logic for non expired item
        }
        return (
            <div className="item-content">
                <div className="item-header">
                    <div className="item-creator-info">
                        <Link className="creator-link" to={`/users/${this.state.item.createdBy.username}`}>
                            <Avatar className="item-creator-avatar"
                                    style={{ backgroundColor: getAvatarColor(this.state.item.createdBy.name)}} >
                                {this.state.item.createdBy.name[0].toUpperCase()}
                            </Avatar>
                            <span className="item-creator-name">
                                {this.state.item.createdBy.name}
                            </span>
                            <span className="item-creator-username">
                                @{this.state.item.createdBy.username}
                            </span>
                            <span className="item-creation-date">
                                {formatDateTime(this.state.item.creationDateTime)}
                            </span>
                        </Link>
                    </div>
                    <div className="item-name">
                        {this.props.item.itemName}
                    </div>
                </div>
                /*item description here */
                <div className="item-body">
                    <div className="item-icon">
                        <img src={productImage} alt="image placeholder" className="item-image"/>
                    </div>
                    <div className="item-description">
                        {this.props.item.description}
                    </div>
                    <div className="item-bid">
                        /* item's top bid here */
                        {this.props.item.topBid.bidVal}
                    </div>
                </div>
                <div className="item-footer">
                    /*
                    {
                        !(this.props.poll.selectedChoice || this.props.poll.expired) ?
                            (<Button className="vote-button" disabled={!this.props.currentVote} onClick={this.props.handleVoteSubmit}>Vote</Button>) : null
                    }
                    <span className="total-bids">{this.props.item.totalBids} bids</span>
                    <span className="separator">•</span>
                    <span className="time-left">
                        {
                            this.props.item.expired ? "Final results" :
                                this.getTimeRemaining(this.props.item)
                        }
                    </span>
                    */
                </div>
            </div>
        );
    }
}

export default Item;