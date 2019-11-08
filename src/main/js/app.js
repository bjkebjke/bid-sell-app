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
		this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
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
            if(newItem.)

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
    onDelete(item) {
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
            <div>
                <p> Hello Hello Hello  {this.props.loggedInManager} </p>
                <CreateDialog attributes = {this.state.attributes} onCreate = {this.onCreate}/>
                <ItemList items = {this.state.items}
                          links = {this.state.links}
                          pageSize = {this.state.pageSize}
                          onNavigate={this.onNavigate}
                          onDelete={this.onDelete}
                          updatePageSize={this.updatePageSize}/>
            </div>
		)
	}
}

class ItemList extends React.Component{
    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    // tag::handle-page-size-updates[]
    handleInput(e) {
        e.preventDefault();
        const pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }
    // end::handle-page-size-updates[]

	handleNavFirst(e){
    	e.preventDefault();
    	this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
    	e.preventDefault();
    	this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
    	e.preventDefault();
    	this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
    	e.preventDefault();
    	this.props.onNavigate(this.props.links.last.href);
    }

	render() {
    		const items = this.props.items.map(item =>
    			<Item key={item._links.self.href} item={item} onDelete={this.props.onDelete}/>
    		);

    		const navLinks = [];
    		if ("first" in this.props.links) {
    			navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
    		}
    		if ("prev" in this.props.links) {
    			navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
    		}
    		if ("next" in this.props.links) {
    			navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
    		}
    		if ("last" in this.props.links) {
    			navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
    		}

    		return (
    			<div>
    				<input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>
    				<table>
    					<tbody>
    						<tr>
    							<th>Item Name</th>
    							<th>Description</th>
    							<th></th>
    						</tr>
    						{items}
    					</tbody>
    				</table>
    				<div>
    					{navLinks}
    				</div>
    			</div>
    		)
    	}
}

class Item extends React.Component{

	constructor(props) {
	    super(props);
	    this.handleDelete = this.handleDelete.bind(this);
	}

	handleDelete() {
        this.props.onDelete(this.props.item);
	}

	render() {
		return (
			<tr>
				<td>{this.props.item.itemName}</td>
				<td>{this.props.item.description}</td>
				<td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
			</tr>
		)
	}
}

ReactDOM.render(
    <App loggedInManager={document.getElementById('username').innerHTML} />,
    document.getElementById('react')
)