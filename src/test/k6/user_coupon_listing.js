import http from 'k6/http';
import { check } from 'k6';


export const options = {
    vus: 1500,
    duration: '1s'
};


// Add a setup function to get the start time
export function setup() {}

export default function () {
    // request 1500 RPS. userId: 1~1500
    const userId = __VU
    const url = `http://localhost:8080/api/v1/users/${userId}/coupons?page=1&size=5`
    const params = {
        headers: {
            accept: 'application/json',
        },
    };

    const response = http.get(url, params);

    check(response, {
        'status is 200': (r) => r.status === 200,
    });

}
