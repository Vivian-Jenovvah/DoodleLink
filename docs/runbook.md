# Runbook and troubleshooting

If the server cannot start, first check `docker compose ps`, then validate the `DB_*` settings. The server never switches to H2. If the desktop client reports a connection failure, check that the server URL is an HTTP(S) base URL and that port 8080 is reachable; the WebSocket endpoint is derived automatically as `ws(s)://host/ws`.

The client reconnects after a transport loss and asks the server for canonical state. An expired room is rejected; create a new room. Use browser/dev network tooling only for TLS/proxy diagnosis—do not put database credentials in client properties.
