const express = require('express')
const cookieParser = require('cookie-parser')
const app = express()
app.use(cookieParser())
const http = require('http')

app.use(express.static(__dirname + '/public'))

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html');
})

app.listen(9090)
