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
    const url = `http://localhost:8080/api/v1/users/${userId}/coupons/2/usage`;
    const headers = {
        'accept': 'application/json',
        'Content-Type': 'application/json',
    };
    const data = JSON.stringify({
        usageStatus: true,
    });

    const response = http.patch(url, data, { headers: headers });

    check(response, {
        'status is 200': (r) => r.status === 200,
    });

}
