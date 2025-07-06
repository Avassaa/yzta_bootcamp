from fastapi import FastAPI
from routers.auth import auth

from database_operations import session,Base,engine
app = FastAPI()
app.include_router(auth)


Base.metadata.create_all(bind=engine)
