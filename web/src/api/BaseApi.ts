export default class BaseApi {
    static async exchange(endpoint: string, data?: object) {
        const content = {
            method: data ? 'POST' : 'GET',
            headers: {'Content-Type': 'application/json'},
            body: data ? JSON.stringify(data) : null
        };

        const response = await fetch(endpoint, content);
        if (response.ok) {
            return await response.json();
        }

        throw new Error(`Server Response Error: ${response.status} - ${response.statusText}`);
    }
}