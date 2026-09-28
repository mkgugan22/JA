# JARVIS — Phase 1A: Connectivity Foundation

This is the first implementation slice for the JARVIS project: a native Android (Kotlin/Compose) screen that calls a FastAPI health endpoint.

## What's here

- jarvis-backend/ — FastAPI service exposing GET /api/v1/health
- jarvis-android/ — Kotlin/Compose app with a "Check backend" button
- src/ — Expo/React Native app

## Running the backend

cd jarvis-backend
python3.12 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements-dev.txt
python -m uvicorn app.main:app --host 127.0.0.1 --port 8000

Verify with curl http://127.0.0.1:8000/api/v1/health

## Android

Requires JDK 17, Gradle 8.13, Android SDK 36, and adb. Use adb reverse tcp:8000 tcp:8000 for local backend access.

## Roadmap

1A Connectivity → 1B Identity & storage → 1C Mobile foundation → 2 Assistant → 3 Voice → 4 Usage intelligence → 5 Notification intelligence → 6 Personalization → 7 Memory → 8 Proactive actions → 9 Security review → 10 Production.
