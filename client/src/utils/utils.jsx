import React from 'react';

import { REGEXP } from './constants';

export const checkRegex = (str) => {
    if (REGEXP.PHONE.test(str)) {
        return 0;
    }
    if (REGEXP.EMAIL.test(str)) {
        return 1;
    }
    if (REGEXP.PASSWORD.test(str)) {
        return 2;
    }
    return -1;
};

export const pseudo = (pseudoSelector, style) => {
    const className = `pseudo-${Math.floor(Math.random() * 1000000)}`;

    // simple encoding of style dictionary to CSS
    // for simplicity, assumes that keys are already in slug case and units have
    // been added, unlike React.CSSProperties
    const styleCSS =
        '{' +
        Object.entries(style)
            .map(([name, value]) => `${name}: ${value};`)
            .join('') +
        '}';

    return {
        className,
        injectStyle: () => <style>{`.${className}${pseudoSelector} ${styleCSS}`}</style>,
    };
};

export const getName = (name) => {
    if (name.length > 12) {
        name = name.slice(0, 12) + '...';
    }
    return name;
};

export const toSlug = (str) => {
    str = str.replace(/^\s+|\s+$/g, ''); // trim
    str = str.toLowerCase();

    // remove accents, swap ñ for n, etc
    var from = 'ãàáäâẽèéëêìíïîõòóöôùúüûñç·/_,:;';
    var to = 'aaaaaeeeeeiiiiooooouuuunc------';
    for (var i = 0, l = from.length; i < l; i++) {
        str = str.replace(new RegExp(from.charAt(i), 'g'), to.charAt(i));
    }

    str = str
        .replace(/[^a-z0-9 -]/g, '') // remove invalid chars
        .replace(/\s+/g, '-') // collapse whitespace and replace by -
        .replace(/-+/g, '-'); // collapse dashes

    return str;
};

export const toLocationSlug = (str) => {
    if (str) {
        str = str.toLowerCase();
        str = str.replace(/(TP[.]|T[.]|thành phố|H[.]|quần đảo|huyện đảo|đảo|huyện|thị trấn|quận|P[.]|X[.]|thị xã|xã|phường)/gi, "");
        str = str.replace(/\s/gi, " ").trim();
        return str;
    }
    return '';
};

export const toAddressSlug = (address) => {
    return {
        ...address,
        ward: toLocationSlug(address?.ward),
        district: toLocationSlug(address?.district),
        city: toLocationSlug(address?.city),
    };
};

export const toFullAddress = (address) => {
    let addressString = 'Việt Nam';
    if (address) {
        if (address?.city) {
            addressString = address?.city + ', ' + addressString;
            if (address?.district) {
                addressString = address?.district + ', ' + addressString;
                if (address?.ward) {
                    addressString = address?.ward + ', ' + addressString;
                    if (address?.homeAdd) {
                        addressString = address?.homeAdd + ', ' + addressString;
                    }
                }
            }
        }
    }
    return addressString;
};

export const getNameForProductCard = (name) => {
    if (name.length > 90) {
        name = name.slice(0, 90) + '...';
    }
    return name;
}
export const getProductsGroupByShop = (products) => {
    return products.reduce((acc, item) => {
        let shopCart = acc.find((shopCart) => shopCart?.id === item?.shop?.id) || null;
        if (shopCart === null) {
            return [...acc, { ...item?.shop, products: [item] }];
        } else {
            acc = acc.filter(shop => shop?.id !== shopCart?.id);
            return [...acc, { ...shopCart, products: [...shopCart.products, item] }];
        }
    }, []);
}

export const parseQueryString = function(search) {
    let objURL = {};

    search.replace(
        new RegExp( "([^?=&]+)(=([^&]*))?", "g" ),
        function( $0, $1, $2, $3 ){
            objURL[ $1 ] = $3;
        }
    );
    return objURL;
};

export const isObjEmpty = function(obj) {
    return Object.keys(obj).length === 0;
}

export const getSentimentByResponse = function(response) {
    let sentiment = response?.data?.sentiment?.trim() || null;
    return sentiment !== 0 ? sentiment : 'Neutral';
}