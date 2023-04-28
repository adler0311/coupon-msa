import http from 'k6/http';
import { check, sleep } from 'k6';

const targetRPS = 1500;
const expectedResponseTime = 0.5;

export const options = {
    vus: 1500,
    duration: '1s'
};


// Add a setup function to get the start time
export function setup() {
    return { startTime: new Date().getTime() };
}

export default function () {
    // Update the calculation of userId: __VU: 1~750. __ITER: 0~1
    const userId = __VU
    const url = 'http://localhost:8080/api/v1/coupons/1/issuance';
    const headers = {
        'accept': 'application/json',
        'Content-Type': 'application/json',
    };
    const payload = JSON.stringify({
        userId: userId,
    });

    const response = http.post(url, payload, { headers: headers });

    check(response, {
        'status is 201': (r) => r.status === 201,
        'content-type is application/json': (r) => r.headers['Content-Type'] === 'application/json',
    });

    // // Control the RPS
    // const endTime = (new Date()).getTime() / 1000;
    // const elapsedTime = endTime - (data.startTime / 1000);
    // const sleepTime = (1 / targetRPS - elapsedTime % (1 / targetRPS)) || 0;
    // sleep(sleepTime);
}
