from fastapi import FastAPI
import firebase_admin
from firebase_admin import credentials
from dotenv import load_dotenv
from firebase_config import initialize_firebase
import logging

from routers import analysis  
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


load_dotenv()
initialize_firebase()

app = FastAPI(
    title="SPixels API", description="Backend API for the Soul Pixels application"
)


app.include_router(analysis.router)

@app.get("/")
def read_root():
    return {"status": "Server is running"}