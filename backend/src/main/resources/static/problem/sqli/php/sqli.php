<?php
if (isset($_POST['ip'])) {
    $ip = $_POST['ip'];

    # append code here
}
?>`,
                'python' :
`from flask import Flask, render_template, request
import os, subprocess

app = Flask(__name__)
app.secret_key = os.urandom(32)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/encode', methods=['POST'])
def encode():
    # append encode code
    pass

@app.route('/decode', methods=['POST'])
def decode():
    # append decode code
    pass

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=80)