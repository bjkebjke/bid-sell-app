import React, { Component } from 'react';
import './Item.css';
import {Avatar, Layout, Button, Row, Col, InputNumber, notification, Divider, Card, Carousel} from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { formatDateTime } from '../util/Helpers';
import productImage from '../item-image-placeholder.svg';
import {getItem, makeBid, getImage} from '../util/APIUtils';
import LoadingIndicator from "../common/LoadingIndicator";

const { Header, Footer, Sider, Content } = Layout;


class Item extends Component {
    constructor(props) {
        super(props);
        this.state = {
            item: null,
            isLoading: true,
            newBid: 0
        };
        this.loadItem = this.loadItem.bind(this);
        this.onBidValChange = this.onBidValChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
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
    };

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
            })
            .catch(error => {
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

    onBidValChange(value) {
        this.setState({
            newBid: value
        });
    }

    onSubmit(event) {
        event.preventDefault();
        if(!this.props.isAuthenticated) {
            this.props.history.push("/login");
            notification.info({
                message: 'Marketplace App',
                description: "Please login to bid on item.",
            });
            return;
        }

        const item = this.state.item;
        const newBid = this.state.newBid;

        const bidData = {
            itemId: item.id,
            bidVal: newBid
        };

        makeBid(bidData)
            .then(response => {
                this.setState({
                    item: response
                });
            }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to bid');
            } else {
                notification.error({
                    message: 'Marketplace App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    }

    componentDidMount() {
        const {itemId} = this.props.match.params;
        this.loadItem(itemId);
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />
        }
        /*
        if(this.state.item.selectedChoice || this.state.item.expired) {
            // logic for already bidded or expired item
        } else {
            // logic for non expired item
        }

        <img src={productImage} alt="placeholder" className="item-image"/>
        */
        const maxBid = this.state.item.topBid === null ? 0 : this.state.item.topBid.bidVal;
        const b64imgs = this.state.item.base64Images;
        return (
            <Layout style={{ padding: '5% 20% 10%' }}>
                <Sider style={{ padding: '0% 2% 0% 0%' }}>
                    <Carousel autoplay={true}>
                        {b64imgs.map((img) => (
                            /*
                            <Card cover={<img src={"data:image/jpg;base64, " + img}/>}>

                            </Card>
                            */
                            <img src={"data:image/jpg;base64, " + img}/>
                        ))}
                    </Carousel>
                </Sider>
                <Layout style={{ padding: '0% 0% 0% 2%' }}>
                    <Content tyle={{ padding: '0% 2% 0% 0%' }}>
                        <Row>
                            {this.state.item.itemName}
                        </Row>
                        <Divider/>
                        <Row>
                            <Row>
                                Description : {this.state.item.description}
                            </Row>
                            <Row>
                                Time left : {this.getTimeRemaining(this.state.item)}
                            </Row>
                        </Row>
                        <Divider/>
                        <Row>
                            <Row>
                                Current bid : ${maxBid}
                            </Row>
                            <Row>
                                <Col span={2}>
                                    <InputNumber min={0} onChange={this.onBidValChange}/>
                                </Col>
                                <Col span={2} offset={2}>
                                    <Button type="primary" onClick={this.onSubmit}>Place bid</Button>
                                </Col>
                            </Row>
                        </Row>
                    </Content>
                    <Sider style={{ padding: '0% 0% 0% 2%' }}>
                        <Card title = 'Seller information'>
                            <Row>
                                Name :  {this.state.item.createdBy.name}
                            </Row>
                            <Divider dashed={true}/>
                            <Row>
                                <Link className="creator-link" to={`/users/${this.state.item.createdBy.username}`}>
                                    <Row>
                                        <Col span={12}>
                                            <Avatar className="poll-creator-avatar"
                                                    style={{ backgroundColor: getAvatarColor(this.state.item.createdBy.name)}} >
                                                {this.state.item.createdBy.name[0].toUpperCase()}
                                            </Avatar>
                                        </Col>
                                        <Col span={12}>
                                            @{this.state.item.createdBy.username}
                                        </Col>
                                    </Row>
                                </Link>
                            </Row>
                        </Card>
                    </Sider>
                </Layout>
            </Layout>
        );
    }
}

export default Item;