function gotoLoginPage() {
    let code_verifier = generateRandomString();
    localStorage.setItem("code_verifier", code_verifier)
    challenge_from_verifier(code_verifier).then(
        (code_challenge) => {
        window.location.replace('http://localhost:8088/realms/exkc/protocol/openid-connect/auth?' +
            'client_id=exkc-app&redirect_uri=http%3A%2F%2Flocalhost:9090%2F&' +
            'response_type=code&scope=openid+address+email+microprofile-jwt+offline_access+phone+profile+roles+web-origins+openid&' +
            'code_challenge=' + code_challenge + '&code_challenge_method=S256')
    });
}

function getToken(code, code_verifier) {
    fetch('http://localhost:8088/realms/exkc/protocol/openid-connect/token', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: "client_id=exkc-app&grant_type=authorization_code&code=" + code +
        "&redirect_uri=http://localhost:9090/&code_verifier=" + code_verifier
    })
    .then(response => response.json())
    .then((body) => {

        localStorage.setItem("access_token", body.access_token);
        localStorage.setItem("id_token", body.id_token);

        setTimeout(() => {
            refreshToken(body.refresh_token);
        }, "10000" /*10 seconds*/);
    });
}

function refreshToken(refresh_token) {
    fetch('http://localhost:8088/realms/exkc/protocol/openid-connect/token', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: "client_id=exkc-app&grant_type=refresh_token&" +
        "refresh_token=" + refresh_token
    })
    .then(response => response.json())
    .then((body) => {
        localStorage.setItem("access_token", body.access_token);
        localStorage.setItem("id_token", body.id_token);

        setTimeout(() => {
            refreshToken(body.refresh_token);
        }, "10000" /*10 seconds*/);
    });
}

function logout() {
    localStorage.removeItem('access_token');
    const id_token = localStorage.getItem('id_token');
    window.location.replace('http://localhost:8088/realms/exkc/protocol/openid-connect/logout?' +
        'post_logout_redirect_uri=http%3A%2F%2Flocalhost:9090%2F&' +
        'id_token_hint=' + id_token);
    localStorage.removeItem('id_token');
}