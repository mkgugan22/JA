from typing import Literal

from fastapi import FastAPI
from pydantic import BaseModel


class HealthResponse(BaseModel):
    status: Literal["online"] = "online"


def create_app() -> FastAPI:
    application = FastAPI(title="JARVIS AI", version="0.1.0")

    @application.get("/api/v1/health", response_model=HealthResponse)
    async def health() -> HealthResponse:
        return HealthResponse()

    return application


app = create_app()
