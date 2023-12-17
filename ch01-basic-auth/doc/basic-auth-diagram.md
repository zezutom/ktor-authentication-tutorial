```mermaid
sequenceDiagram
    autoNumber
    Client->>+Server: GET /
    Server->>-Client: 401 Unauthorized, WWW-Authenticate: Basic
    Client->>+Client: Ask user to enter credentials
    Client->>+Server: GET /, Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=
    Server->>-Client: 200 OK or 401 Unauthorized
```