from flask import Flask, rendertemplate, request
import os, subprocess

app = Flask(name)
app.secretkey = os.urandom(32)

@app.route('/')
def index():
    return rendertemplate('index.html')

@app.route('/encode', methods=['POST'])
def encode():
    # append encode code
    pass

@app.route('/decode', methods=['POST'])
def decode():
    # append decode code
    pass

if __name == '__main':
    app.run(host="0.0.0.0", port=80)