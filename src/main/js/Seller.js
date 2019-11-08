'use strict';

import CreateDialog from "./CreateDialog"

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const follow = require('./follow'); // function to hop multiple links by "rel"
const root = '/api';

export default class Seller extends React.Component {

    constructor(props) {
        super(props);
        this.state = {items: [], attributes: [], pageSize: 2, links: {}};
    }

    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'users/search/findByName?name='+{this.props.loggedInManager}, params: {size: pageSize}}]
        ).then(user => {
            return client({
                method: 'GET',
                path: userCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.entity;
                return userCollection;
            });
        }).done(itemCollection => {
            this.setState({
                items: itemCollection.entity._embedded.items,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: itemCollection.entity._links});
        });
    }

    render() {
        return ()
    }

}