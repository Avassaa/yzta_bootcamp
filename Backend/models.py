from database_operations import Base
from sqlalchemy import Column, Integer, String, ForeignKey,VARCHAR, Date, Boolean,DateTime
from sqlalchemy.sql.schema import Index
from datetime import datetime

class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True, index=True)
    email = Column(String(120), unique=True, nullable=False)
    username= Column(String(15),unique=True,nullable=False)
    hashedPassword = Column(String(120), nullable=False)
    userLocation = Column(String(120), nullable=True)
    createdAt=Column(DateTime, default=datetime.utcnow)
