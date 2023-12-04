# Web Blog REST API Java

## CORS
Per default, CORS policy is disabled to grant JavaScript clients permission for accessing the API.
To test that CORS is disabled, you can use the following JavaScript code.
```javascript
function callArticles() {
    console.log("callArticles");

    // Define the access token
    const accessToken = 'eyJhbGciOiJIUzM...';

    // Define the API endpoint
    const apiEndpoint = 'https://ithoc-web-blog-rest-api-java.azurewebsites.net/api/articles';
//    const apiEndpoint = 'http://localhost:18080/api/articles';

    // Options for the fetch call
    const fetchOptions = {
        method: 'GET', // or 'POST' if required
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }
    };

    // Making the fetch call
    fetch(apiEndpoint, fetchOptions)
        .then(response => {
            // Check if the response is ok (status code 200-299)
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            // Process the data
            console.log(data);
        })
        .catch(error => {
            // Handle any errors
            console.error('Error fetching data: ', error);
        });
}
```


