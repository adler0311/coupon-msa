import http from 'k6/http';
import { check, sleep } from 'k6';
import {scenario} from 'k6/execution';

export const options = {
    vus: 3,
    duration: '1m'
};


export default function () {
    const userId = scenario.iterationInTest;
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
    });

    sleep(1)
}
