import firebase_admin
from firebase_admin import credentials, firestore
from dotenv import load_dotenv


def initialize_firebase():
    load_dotenv()
    cred = credentials.Certificate("serviceAccountKey.json")
    firebase_admin.initialize_app(cred)
    print("Firebase App Initialized Successfully!")


def get_db():
    return firestore.client()
