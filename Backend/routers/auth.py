from fastapi import APIRouter,Path, Query, Depends, Request
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from starlette.exceptions import HTTPException
from database_operations import db_dependency
from schemas import CreateUserRequest, Token
from starlette import status
from models import User
from passlib.context import CryptContext
from jose import jwt,JWTError
from datetime import timedelta,datetime
from typing import Annotated
auth = APIRouter(prefix="/auth", tags=["Authentication"])

bcrypt_context = CryptContext(schemes=["bcrypt"],deprecated="auto")
RANDOM_KEY="ZexkwzjKfw"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MIN=120


oauth2_pass_bearer= OAuth2PasswordBearer(tokenUrl="/auth/login")


async def get_current_user(token: Annotated[Token, Depends(oauth2_pass_bearer)]):
    try:
        payload= jwt.decode(token,RANDOM_KEY, algorithms=[ALGORITHM])
        id= payload.get("id")
        username= payload.get("sub")
        if username is None or id is None:
            raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED,detail="Invalid authentication credentials")
        return {"username":username,"id":id}
    except JWTError:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED,detail="Invalid authentication credentials")

@auth.post("/register", status_code=status.HTTP_201_CREATED)
async def register_user(db:db_dependency,create_user_request:CreateUserRequest):
    user = User(
        email=create_user_request.email,
        username=create_user_request.username,
        hashedPassword=bcrypt_context.hash(create_user_request.password),

    )
    db.add(user)
    db.commit()

def create_access_token(username : str ,user_id:str,expires_delta: timedelta):
    payload = {
        "sub" : username,
        "id" :user_id,
        "exp" : datetime.utcnow()+expires_delta
    }
    return jwt.encode(payload,key=RANDOM_KEY,algorithm=ALGORITHM)

def authenticate_user(username:str, password:str,db:db_dependency):
    user = db.query(User).filter(User.username==username).first()
    if not user:
        return False
    if not bcrypt_context.verify(password,user.hashedPassword):
        return False
    return user



@auth.post("/login", response_model=Token)
async def login_for_access_token(form_data: Annotated[OAuth2PasswordRequestForm, Depends()],db:db_dependency):
    user=authenticate_user(form_data.username,form_data.password,db)
    if not user:
       return HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="No user could be found with given e-mail and password.")
    token= create_access_token(user.username,user.id,timedelta(minutes=ACCESS_TOKEN_EXPIRE_MIN))
    return {"access_token":token,"token_type":"bearer"}
