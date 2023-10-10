function call_backend(method, idel) {
    const div = document.querySelector('#' + idel);
    div.innerHTML = '';
    const access_token = localStorage.getItem('access_token');
    var bearer = 'Bearer ' + access_token;
    fetch('http://localhost:8080/' + method, {
        method: 'GET',
        withCredentials: true,
        credentials: 'include',
        headers: {
            'Authorization': bearer
        }
    })
    .then(response => response.text())
    .then((body) => {
        div.innerHTML = body;
    });
}