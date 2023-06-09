import http from 'k6/http';
import { check, sleep } from 'k6';
import {scenario} from 'k6/execution';

export const options = {
    stages: [
        {duration: '2m', target: 2000}, // fast ramp-up to a high point
        // No plateau
        {duration: '1m', target: 0} // quick ramp-down to 0 users
    ]
};


// Add a setup function to get the start time
export function setup() {
    return { startTime: new Date().getTime() };
}

export default function () {
    // Update the calculation of userId: __VU: 1~750. __ITER: 0~1
    const userId = scenario.iterationInTest;
    const url = 'http://localhost:8080/api/v1/coupons/5/issuance';
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

    sleep(1)

    // // Control the RPS
    // const endTime = (new Date()).getTime() / 1000;
    // const elapsedTime = endTime - (data.startTime / 1000);
    // const sleepTime = (1 / targetRPS - elapsedTime % (1 / targetRPS)) || 0;
    // sleep(sleepTime);
}
