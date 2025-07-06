from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import sessionmaker,Session
from sqlalchemy.ext.declarative import declarative_base
from fastapi import Depends
from typing import Annotated
CONNECTION_STRING=""
engine= create_engine(CONNECTION_STRING)
session= sessionmaker(autocommit=False, autoflush=False,bind=engine)

Base = declarative_base()

def get_db():
    db=session()
    try:
        yield db
    finally:
        db.close()

db_dependency=Annotated[Session, Depends(get_db)]
