from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)


def test_health_returns_online() -> None:
    response = client.get("/api/v1/health")

    assert response.status_code == 200
    assert response.json() == {"status": "online"}


def test_health_content_type_is_json() -> None:
    response = client.get("/api/v1/health")

    assert response.headers["content-type"].startswith("application/json")
