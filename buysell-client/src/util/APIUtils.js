import {API_BASE_URL, ACCESS_TOKEN, ITEM_LIST_SIZE} from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if(!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
};

export function getImage(imageId) {
    return request({
        url: API_BASE_URL + "/images/download/" + imageId,
        method: 'GET'
    });
}

export function getItem(itemId) {
    return request({
        url: API_BASE_URL + "/items/" + itemId,
        method: 'GET'
    });
}

export function getAllItems(page, size) {
    page = page || 0;
    size = size || ITEM_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/items?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function createItem(itemData, images) {
    console.log(images);
    var formData = new FormData();
    formData.append("itemRequest", JSON.stringify(itemData));

    for(var index = 0; index < images.length; index++) {
        formData.append("files", images[index]);
    }

    let options = {
        url: API_BASE_URL + "/items",
        method: 'POST',
        body: formData
    };

    const headers = new Headers();

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if(!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
}

export function makeBid(bidData) {
    return request({
        url: API_BASE_URL + "/items/" + bidData.itemId + "/bids",
        method: 'POST',
        body: JSON.stringify(bidData)
    });
}

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function checkUsernameAvailability(username) {
    return request({
        url: API_BASE_URL + "/user/checkUsernameAvailability?username=" + username,
        method: 'GET'
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: API_BASE_URL + "/user/checkEmailAvailability?email=" + email,
        method: 'GET'
    });
}


export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function getUserProfile(username) {
    return request({
        url: API_BASE_URL + "/users/" + username,
        method: 'GET'
    });
}

export function getUserCreatedItems(username, page, size) {
    page = page || 0;
    size = size || ITEM_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/users/" + username + "/items?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function getUserBiddedItems(username, page, size) {
    page = page || 0;
    size = size || ITEM_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/users/" + username + "/bids?page=" + page + "&size=" + size,
        method: 'GET'
    });
}