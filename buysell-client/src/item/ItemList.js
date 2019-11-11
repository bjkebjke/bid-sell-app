import React, { Component } from 'react';
import {getAllItems, getUserCreatedItems, getUserBiddedItems} from '../util/APIUtils';
import ItemListing from './ItemListing'

import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon, Card } from 'antd';
import { ITEM_LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import './ItemList.css';

class ItemList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            items: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            isLoading: false
        };
        this.loadItemList = this.loadItemList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadItemList(page = 0, size = ITEM_LIST_SIZE) {
        let promise;
        if(this.props.username) {
            if(this.props.type === 'USER_CREATED_ITEMS') {
                promise = getUserCreatedItems(this.props.username, page, size);
            } else if (this.props.type === 'USER_BIDDED_ITEMS') {
                promise = getUserBiddedItems(this.props.username, page, size);
            }
        } else {
            promise = getAllItems(page, size);
        }

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const items = this.state.items.slice();

                this.setState({
                    items: items.concat(response.content),
                    page: response.page,
                    size: response.size,
                    totalElements: response.totalElements,
                    totalPages: response.totalPages,
                    last: response.last,
                    isLoading: false
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    componentDidMount() {
        this.loadItemList();
    }

    componentDidUpdate(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                items: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                isLoading: false
            });
            this.loadItemList();
        }
    }

    handleLoadMore() {
        this.loadItemList(this.state.page + 1);
    }

    render() {
        const itemViews = [];
        this.state.items.forEach((item, itemIndex) => {
            console.log(item);
            itemViews.push(<ItemListing
                key={item.id}
                item={item}/>)
        });

        return (
            <div className="items-container">
                <Card>
                    {itemViews}
                </Card>

                {
                    !this.state.isLoading && this.state.items.length === 0 ? (
                        <div className="no-items-found">
                            <span>No Items Found.</span>
                        </div>
                    ): null
                }
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-items">
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <Icon type="plus" /> Load more
                            </Button>
                        </div>): null
                }
                {
                    this.state.isLoading ?
                        <LoadingIndicator />: null
                }
            </div>
        );
    }
}

export default withRouter(ItemList);