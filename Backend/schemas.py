from pydantic import BaseModel,Field, EmailStr,SecretStr
from typing import Optional
from datetime import datetime as Date


class CreateUserRequest(BaseModel):
    email: EmailStr = Field(description="User's email address.")
    username :str  = Field(min_length=3, max_length=15, description="Username field.")
    password:str = Field(min_length=5, max_length=35, description="Password field of the user")
    userPlaylists: str = Field(description="User playlists")
    userProfilePhoto: Optional[str] = Field(None, description="URL or ID of user profile picture")
    userLocation: Optional[str] = Field(None, description="User's location")

class Token(BaseModel):
    access_token: str
    token_type:str
