'use strict';

import CreateDialog from "./CreateDialog"

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

const follow = require('./follow'); // function to hop multiple links by "rel"

const root = '/api';

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {items: [], attributes: [], pageSize: 2, links: {}};
		this.onCreate = this.onCreate.bind(this);
	}

	loadFromServer(pageSize) {
    		follow(client, root, [
    			{rel: 'items', params: {size: pageSize}}]
    		).then(itemCollection => {
    			return client({
    				method: 'GET',
    				path: itemCollection.entity._links.profile.href,
    				headers: {'Accept': 'application/schema+json'}
    			}).then(schema => {
    				this.schema = schema.entity;
    				return itemCollection;
    			});
    		}).done(itemCollection => {
    			this.setState({
    				items: itemCollection.entity._embedded.items,
    				attributes: Object.keys(this.schema.properties),
    				pageSize: pageSize,
    				links: itemCollection.entity._links});
    		});
    }

    onCreate(newItem) {
    		follow(client, root, ['items']).then(itemCollection => {
    			return client({
    				method: 'POST',
    				path: itemCollection.entity._links.self.href,
    				entity: newItem,
    				headers: {'Content-Type': 'application/json'}
    			})
    		}).then(response => {
    			return follow(client, root, [
    				{rel: 'items', params: {'size': this.state.pageSize}}]);
    		}).done(response => {
    			if (typeof response.entity._links.last !== "undefined") {
    				this.onNavigate(response.entity._links.last.href);
    			} else {
    				this.onNavigate(response.entity._links.self.href);
    			}
    		});
    }

    // tag::delete[]
    onDelete(employee) {
        client({method: 'DELETE', path: item._links.self.href}).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }
    // end::delete[]

    // tag::navigate[]
    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(itemCollection => {
            this.setState({
                items: itemCollection.entity._embedded.employees,
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: itemCollection.entity._links
            });
        });
    }
    // end::navigate[]

    // tag::update-page-size[]
    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }


	componentDidMount() {
		this.loadFromServer(this.state.pageSize);
	}

	render() {
		return (
		    <CreateDialog attributes = {this.state.attributes} onCreate = {this.onCreate}/>
			<ItemList items = {this.state.items}
			          links = {this.state.links}
			          pageSize = {this.state.pageSize}
			          onNavigate={this.onNavigate}
                      onDelete={this.onDelete}
                      updatePageSize={this.updatePageSize}/>
		)
	}
}

class ItemList extends React.Component{
	render() {
		const items = this.props.items.map(item =>
			<Item key={item._links.self.href} item={item}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>Item Name</th>
						<th>Item Description</th>
					</tr>
					{items}
				</tbody>
			</table>
		)
	}
}

class Item extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.item.itemName}</td>
				<td>{this.props.item.description}</td>
			</tr>
		)
	}
}

ReactDOM.render(<App />, document.getElementById('react'))