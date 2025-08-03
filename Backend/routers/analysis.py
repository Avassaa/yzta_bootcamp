from fastapi import APIRouter, Depends, HTTPException
import requests
import os
import google.generativeai as genai
import logging
from datetime import datetime
import sys
import PIL.Image
import io
from dotenv import load_dotenv
from firebase_admin import firestore, auth
from routers.dependencies import get_current_user
from firebase_config import get_db
from schemas import JournalEntryRequest

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.StreamHandler(sys.stdout),  
        logging.FileHandler('journal_api.log', encoding='utf-8')  
    ]
)
logger = logging.getLogger(__name__)

logger.info("Loading environment variables...")

load_dotenv()
api_key = os.getenv("GEMINI_API_KEY") or os.getenv("GOOGLE_API_KEY")

if not api_key:
    logger.error("ERROR: No API key found!")
    logger.error("Please add one of these to your .env file:")
    logger.error("  GEMINI_API_KEY=your_api_key")
    logger.error("  GOOGLE_API_KEY=your_api_key")
else:
    logger.info("API key found, configuring Gemini client...")
    genai.configure(api_key=api_key)
    logger.info("Gemini client configured successfully")

model = genai.GenerativeModel("gemini-1.5-flash")

router = APIRouter(
    prefix="/journal",
    tags=["Journal"],
)


from fastapi import APIRouter, Depends, HTTPException
import requests
import os
import google.generativeai as genai
import logging
from datetime import datetime
import sys
import PIL.Image
import io
from dotenv import load_dotenv
from firebase_admin import firestore, auth
from routers.dependencies import get_current_user
from firebase_config import get_db
from schemas import JournalEntryRequest

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.StreamHandler(sys.stdout), 
        logging.FileHandler('journal_api.log', encoding='utf-8') 
    ]
)
logger = logging.getLogger(__name__)

logger.info("Loading environment variables...")

load_dotenv()
api_key = os.getenv("GEMINI_API_KEY") or os.getenv("GOOGLE_API_KEY")

if not api_key:
    logger.error("ERROR: No API key found!")
    logger.error("Please add one of these to your .env file:")
    logger.error("  GEMINI_API_KEY=your_api_key")
    logger.error("  GOOGLE_API_KEY=your_api_key")
else:
    logger.info("API key found, configuring Gemini client...")
    genai.configure(api_key=api_key)
    logger.info("Gemini client configured successfully")

model = genai.GenerativeModel("gemini-1.5-flash")

router = APIRouter(
    prefix="/journal",
    tags=["Journal"],
)

from fastapi import APIRouter, Depends, HTTPException
import requests
import os
import google.generativeai as genai
import logging
from datetime import datetime
import sys
import PIL.Image
import io
from dotenv import load_dotenv
from firebase_admin import firestore, auth
from routers.dependencies import get_current_user
from firebase_config import get_db
from schemas import JournalEntryRequest

# Configure logging WITHOUT emojis and with proper encoding
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.StreamHandler(sys.stdout),
        logging.FileHandler('journal_api.log', encoding='utf-8')
    ]
)
logger = logging.getLogger(__name__)

# Load environment variables and configure Gemini
logger.info("Loading environment variables...")
load_dotenv()
api_key = os.getenv("GEMINI_API_KEY") or os.getenv("GOOGLE_API_KEY")

if not api_key:
    logger.error("ERROR: No API key found!")
    logger.error("Please add GEMINI_API_KEY=your_api_key to your .env file")
else:
    logger.info("API key found, configuring Gemini client...")
    genai.configure(api_key=api_key)
    logger.info("Gemini client configured successfully")

# Create the model instance
model = genai.GenerativeModel("gemini-1.5-flash")

# Create router
router = APIRouter(
    prefix="/journal",
    tags=["Journal"],
)


@router.post("/create")
async def create_journal_entry(
    request_data: JournalEntryRequest, user: dict = Depends(get_current_user)
):
    logger.info(f"DEBUG: Available user keys: {list(user.keys())}")
    
    try:
        db = get_db()
        uid = user.get("uid")
        
        # Get user info from Firestore
        user_doc = db.collection("users").document(uid).get()
        if user_doc.exists:
            user_data = user_doc.to_dict()
            user_name = user_data.get("displayName") or user_data.get("name") or user.get("email", "").split("@")[0] or "Anonymous"
            user_photo_url = user_data.get("photoURL") or user_data.get("picture") or ""
            logger.info(f"User found in Firestore: {user_name}")
        else:
            user_name = user.get("email", "").split("@")[0] or "Anonymous"
            user_photo_url = ""
            logger.warning(f"User not found in Firestore, using email prefix: {user_name}")
        
        # Log request
        logger.info(f"NEW JOURNAL ENTRY REQUEST - User: {uid} ({user_name})")
        logger.info(f"   Mood Rating: {request_data.moodRating}/10")
        logger.info(f"   Feeling: {request_data.feelingText}")
        logger.info(f"   Public: {request_data.isPublic}")

        # Download and process image
        logger.info("Starting image download from Firebase Storage...")
        image_response = requests.get(request_data.entryPhotoUrl)
        image_response.raise_for_status()
        image_bytes = image_response.content
        logger.info(f"Image downloaded successfully - Size: {len(image_bytes)} bytes")

        image = PIL.Image.open(io.BytesIO(image_bytes))
        logger.info(f"Image processed - Dimensions: {image.size}")
        
        # Prepare prompt
        prompt_text = f"""
        Analyze this personal journal image carefully. The user described their feeling as: '{request_data.feelingText}' and rated their mood as {request_data.moodRating}/10.

        Please provide a thoughtful analysis that includes:
        1. What you observe in the image (objects, scene, colors, mood)
        2. How the visual elements connect to the user's described feelings
        3. Insights about the user's emotional state based on both the image and their description
        4. Positive observations and potential areas of growth or reflection
        5. Encouraging words or gentle suggestions for emotional wellness
        6. Please do not use markdown syntax, as your answer will directly be displayed in the app.
        7. Approach the user sincerely, be sincere, and avoid any judgmental language.
        8. Do not forget that you are talking directly to user.
        Keep the response warm, empathetic, and supportive. Focus on understanding rather than judgment.
        Do not use markdown formatting as this will be displayed directly in the app.

        And MOST IMPORTANTLY: what would be the thing in this picture that secretly makes the user feel this way? Explain how the image reflects their emotional state and what specific elements might be influencing their feelings.
        """

        # Get Gemini analysis
        logger.info("Sending request to Gemini AI for analysis...")
        response = model.generate_content([prompt_text, image])
        gemini_analysis = response.text
        logger.info(f"Gemini analysis completed - Response length: {len(gemini_analysis)} characters")

        # Save to Firestore
        journal_entry_data = {
            "userId": uid,
            "userName": user_name,
            "userPhotoUrl": user_photo_url,
            "feelingText": request_data.feelingText,
            "moodRating": request_data.moodRating,
            "entryPhotoUrl": request_data.entryPhotoUrl,
            "geminiAnalysis": gemini_analysis,
            "isPublic": request_data.isPublic,
            "timestamp": firestore.SERVER_TIMESTAMP,
        }

        logger.info("Saving journal entry to Firestore...")
        update_time, doc_ref = db.collection("journal_entries").add(journal_entry_data)
        
        logger.info(f"Journal entry created successfully - Document ID: {doc_ref.id}")
        logger.info(f"   User: {user_name} ({uid})")
        logger.info("=" * 80)

        return {"status": "success", "documentId": doc_ref.id}

    except requests.RequestException as e:
        logger.error(f"Image download failed: {str(e)}")
        raise HTTPException(status_code=400, detail=f"Failed to download image: {str(e)}")
    
    except Exception as e:
        logger.error(f"ERROR in create_journal_entry: {str(e)}")
        logger.error(f"   User: {user.get('email', 'Unknown')} ({user.get('uid', 'Unknown')})")
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")
    logger.info(f"DEBUG: Available user keys: {list(user.keys())}")
    
    try:
        db = get_db()
        uid = user.get("uid")
        
        user_doc = db.collection("users").document(uid).get()
        if user_doc.exists:
            user_data = user_doc.to_dict()
            user_name = user_data.get("displayName") or user_data.get("name") or user.get("email", "").split("@")[0] or "Anonymous"
            user_photo_url = user_data.get("photoURL") or user_data.get("picture") or ""
            logger.info(f"User found in Firestore: {user_name}")
        else:
            user_name = user.get("email", "").split("@")[0] or "Anonymous"
            user_photo_url = ""
            logger.warning(f"User not found in Firestore, using email prefix: {user_name}")
        
        logger.info(f"NEW JOURNAL ENTRY REQUEST - User: {uid} ({user_name})")
        logger.info(f"   Mood Rating: {request_data.moodRating}/10")
        logger.info(f"   Feeling: {request_data.feelingText}")
        logger.info(f"   Public: {request_data.isPublic}")

        logger.info("Starting image download from Firebase Storage...")
        image_response = requests.get(request_data.entryPhotoUrl)
        image_response.raise_for_status()
        image_bytes = image_response.content
        logger.info(f"Image downloaded successfully - Size: {len(image_bytes)} bytes")

        image = PIL.Image.open(io.BytesIO(image_bytes))
        logger.info(f"Image processed - Dimensions: {image.size}")
        
        prompt_text = f"""
        Analyze this personal journal image carefully. The user described their feeling as: '{request_data.feelingText}' and rated their mood as {request_data.moodRating}/10.

        Please provide a thoughtful analysis that includes:
        1. What you observe in the image (objects, scene, colors, mood)
        2. How the visual elements connect to the user's described feelings
        3. Insights about the user's emotional state based on both the image and their description
        4. Positive observations and potential areas of growth or reflection
        5. Encouraging words or gentle suggestions for emotional wellness
        6. Please do not use markdown syntax, as your answer will directly be displayed in the app.
        7. Approach the user sincerely, be sincere, and avoid any judgmental language.


        Keep the response warm, empathetic, and supportive. Focus on understanding rather than judgment.
        """

        logger.info("Sending request to Gemini AI for analysis...")
        response = model.generate_content([prompt_text, image])
        gemini_analysis = response.text
        logger.info(f"Gemini analysis completed - Response length: {len(gemini_analysis)} characters")

        journal_entry_data = {
            "userId": uid,
            "userName": user_name,
            "userPhotoUrl": user_photo_url,
            "feelingText": request_data.feelingText,
            "moodRating": request_data.moodRating,
            "entryPhotoUrl": request_data.entryPhotoUrl,
            "geminiAnalysis": gemini_analysis,
            "isPublic": request_data.isPublic,
            "timestamp": firestore.SERVER_TIMESTAMP,
        }

        logger.info("Saving journal entry to Firestore...")
        update_time, doc_ref = db.collection("journal_entries").add(journal_entry_data)
        
        logger.info(f"Journal entry created successfully - Document ID: {doc_ref.id}")
        logger.info(f"   User: {user_name} ({uid})")
        logger.info(f"   Document ID: {doc_ref.id}")
        logger.info("=" * 80)

        return {"status": "success", "documentId": doc_ref.id}

    except requests.RequestException as e:
        logger.error(f"Image download failed: {str(e)}")
        raise HTTPException(status_code=400, detail=f"Failed to download image: {str(e)}")
    
    except Exception as e:
        logger.error(f"ERROR in create_journal_entry: {str(e)}")
        logger.error(f"   User: {user.get('email', 'Unknown')} ({user.get('uid', 'Unknown')})")
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")
    logger.info(f"DEBUG: Available user keys: {list(user.keys())}")
    
    try:
        db = get_db()
        uid = user.get("uid")
        
        user_doc = db.collection("users").document(uid).get()
        if user_doc.exists:
            user_data = user_doc.to_dict()
            user_name = user_data.get("displayName") or user_data.get("name") or user.get("email", "").split("@")[0] or "Anonymous"
            user_photo_url = user_data.get("photoURL") or user_data.get("picture") or ""
            logger.info(f"User found in Firestore: {user_name}")
        else:
            user_name = user.get("email", "").split("@")[0] or "Anonymous"
            user_photo_url = ""
            logger.warning(f"User not found in Firestore, using email prefix: {user_name}")
        
        logger.info(f"NEW JOURNAL ENTRY REQUEST - User: {uid} ({user_name})")
        logger.info(f"   Mood Rating: {request_data.moodRating}/10")
        logger.info(f"   Feeling: {request_data.feelingText}")
        logger.info(f"   Public: {request_data.isPublic}")

        logger.info("Starting image download from Firebase Storage...")
        image_response = requests.get(request_data.entryPhotoUrl)
        image_response.raise_for_status()
        image_bytes = image_response.content
        logger.info(f"Image downloaded successfully - Size: {len(image_bytes)} bytes")

        image = PIL.Image.open(io.BytesIO(image_bytes))
        logger.info(f"Image processed - Dimensions: {image.size}")
        
        prompt_text = f"""
        Analyze this personal journal image carefully. The user described their feeling as: '{request_data.feelingText}' and rated their mood as {request_data.moodRating}/10.

        Please provide a thoughtful analysis that includes:
        1. What you observe in the image (objects, scene, colors, mood)
        2. How the visual elements connect to the user's described feelings
        3. Insights about the user's emotional state based on both the image and their description
        4. Positive observations and potential areas of growth or reflection
        5. Encouraging words or gentle suggestions for emotional wellness
        6. Please do not use markdown syntax, as your answer will directly be displayed in the app.
        7. Approach the user sincerely, be sincere, and avoid any judgmental language.
        Keep the response warm, empathetic, and supportive. Focus on understanding rather than judgment.
        """

        logger.info("Sending request to Gemini AI for analysis...")
        response = model.generate_content([prompt_text, image])
        gemini_analysis = response.text
        logger.info(f"Gemini analysis completed - Response length: {len(gemini_analysis)} characters")

        journal_entry_data = {
            "userId": uid,
            "userName": user_name,
            "userPhotoUrl": user_photo_url,
            "feelingText": request_data.feelingText,
            "moodRating": request_data.moodRating,
            "entryPhotoUrl": request_data.entryPhotoUrl,
            "geminiAnalysis": gemini_analysis,
            "isPublic": request_data.isPublic,
            "timestamp": firestore.SERVER_TIMESTAMP,
        }

        logger.info("Saving journal entry to Firestore...")
        update_time, doc_ref = db.collection("journal_entries").add(journal_entry_data)
        
        logger.info(f"Journal entry created successfully - Document ID: {doc_ref.id}")
        logger.info(f"   User: {user_name} ({uid})")
        logger.info(f"   Document ID: {doc_ref.id}")
        logger.info("=" * 80)

        return {"status": "success", "documentId": doc_ref.id}

    except requests.RequestException as e:
        logger.error(f"Image download failed: {str(e)}")
        raise HTTPException(status_code=400, detail=f"Failed to download image: {str(e)}")
    
    except Exception as e:
        logger.error(f"ERROR in create_journal_entry: {str(e)}")
        logger.error(f"   User: {user.get('email', 'Unknown')} ({user.get('uid', 'Unknown')})")
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")
    logger.info(f"DEBUG: Full user object: {user}")
    logger.info(f"DEBUG: Available user keys: {list(user.keys())}")
    
    user_name = (
        user.get("name") or 
        user.get("display_name") or 
        user.get("displayName") or 
        user.get("email", "").split("@")[0] or  
        "Anonymous"
    )
    
    logger.info(f"NEW JOURNAL ENTRY REQUEST - User: {user.get('uid', 'Unknown')} ({user_name})")
    logger.info(f"   Mood Rating: {request_data.moodRating}/10")
    logger.info(f"   Feeling: {request_data.feelingText}")
    logger.info(f"   Public: {request_data.isPublic}")
    
    try:
        db = get_db()
        uid = user.get("uid")
        user_photo_url = user.get("picture", "")

        logger.info("Starting image download from Firebase Storage...")
        image_response = requests.get(request_data.entryPhotoUrl)
        image_response.raise_for_status()
        image_bytes = image_response.content
        logger.info(f"Image downloaded successfully - Size: {len(image_bytes)} bytes")

        image = PIL.Image.open(io.BytesIO(image_bytes))
        logger.info(f"Image processed - Dimensions: {image.size}")
        
        prompt_text = f"""
        Analyze this personal journal image carefully. The user described their feeling as: '{request_data.feelingText}' and rated their mood as {request_data.moodRating}/10.

        Please provide a thoughtful analysis that includes:
        1. What you observe in the image (objects, scene, colors, mood)
        2. How the visual elements connect to the user's described feelings
        3. Insights about the user's emotional state based on both the image and their description
        4. Positive observations and potential areas of growth or reflection
        5. Encouraging words or gentle suggestions for emotional wellness

        Keep the response warm, empathetic, and supportive. Focus on understanding rather than judgment.
        """

        logger.info("Sending request to Gemini AI for analysis...")
        response = model.generate_content([prompt_text, image])
        gemini_analysis = response.text
        logger.info(f"Gemini analysis completed - Response length: {len(gemini_analysis)} characters")

        journal_entry_data = {
            "userId": uid,
            "userName": user_name,
            "userPhotoUrl": user_photo_url,
            "feelingText": request_data.feelingText,
            "moodRating": request_data.moodRating,
            "entryPhotoUrl": request_data.entryPhotoUrl,
            "geminiAnalysis": gemini_analysis,
            "isPublic": request_data.isPublic,
            "timestamp": firestore.SERVER_TIMESTAMP,
        }

        logger.info("Saving journal entry to Firestore...")
        update_time, doc_ref = db.collection("journal_entries").add(journal_entry_data)
        
        logger.info(f"Journal entry created successfully - Document ID: {doc_ref.id}")
        logger.info(f"   User: {user_name} ({uid})")
        logger.info(f"   Document ID: {doc_ref.id}")
        logger.info("=" * 80)

        return {"status": "success", "documentId": doc_ref.id}

    except requests.RequestException as e:
        logger.error(f"Image download failed: {str(e)}")
        raise HTTPException(status_code=400, detail=f"Failed to download image: {str(e)}")
    
    except Exception as e:
        logger.error(f"ERROR in create_journal_entry: {str(e)}")
        logger.error(f"   User: {user_name} ({user.get('uid', 'Unknown')})")
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")
    logger.info(f"NEW JOURNAL ENTRY REQUEST - User: {user.get('uid', 'Unknown')} ({user.get('name', 'Anonymous')})")
    logger.info(f"   Mood Rating: {request_data.moodRating}/10")
    logger.info(f"   Feeling: {request_data.feelingText}")
    logger.info(f"   Public: {request_data.isPublic}")
    
    try:
        db = get_db()
        uid = user.get("uid")
        user_name = user.get("name", "Anonymous")
        user_photo_url = user.get("picture", "")

        logger.info("Starting image download from Firebase Storage...")
        # Download the image from the Firebase Storage URL
        image_response = requests.get(request_data.entryPhotoUrl)
        image_response.raise_for_status()
        image_bytes = image_response.content
        logger.info(f"Image downloaded successfully - Size: {len(image_bytes)} bytes")

        image = PIL.Image.open(io.BytesIO(image_bytes))
        logger.info(f"Image processed - Dimensions: {image.size}")
        
        prompt_text = f"""
        Analyze this personal journal image carefully. The user described their feeling as: '{request_data.feelingText}' and rated their mood as {request_data.moodRating}/10.

        Please provide a thoughtful analysis that includes:
        1. What you observe in the image (objects, scene, colors, mood)
        2. How the visual elements connect to the user's described feelings
        3. Insights about the user's emotional state based on both the image and their description
        4. Positive observations and potential areas of growth or reflection
        5. Encouraging words or gentle suggestions for emotional wellness

        Keep the response warm, empathetic, and supportive. Focus on understanding rather than judgment.
        """

        logger.info("Sending request to Gemini AI for analysis...")
        response = model.generate_content([prompt_text, image])
        gemini_analysis = response.text
        logger.info(f"Gemini analysis completed - Response length: {len(gemini_analysis)} characters")

        journal_entry_data = {
            "userId": uid,
            "userName": user_name,
            "userPhotoUrl": user_photo_url,
            "feelingText": request_data.feelingText,
            "moodRating": request_data.moodRating,
            "entryPhotoUrl": request_data.entryPhotoUrl,
            "geminiAnalysis": gemini_analysis,
            "isPublic": request_data.isPublic,
            "timestamp": firestore.SERVER_TIMESTAMP,
        }

        logger.info("Saving journal entry to Firestore...")
        update_time, doc_ref = db.collection("journal_entries").add(journal_entry_data)
        
        logger.info(f"Journal entry created successfully - Document ID: {doc_ref.id}")
        logger.info(f"   User: {user_name} ({uid})")
        logger.info(f"   Document ID: {doc_ref.id}")
        logger.info("=" * 80)

        return {"status": "success", "documentId": doc_ref.id}

    except requests.RequestException as e:
        logger.error(f"Image download failed: {str(e)}")
        raise HTTPException(status_code=400, detail=f"Failed to download image: {str(e)}")
    
    except Exception as e:
        logger.error(f"ERROR in create_journal_entry: {str(e)}")
        logger.error(f"   User: {user.get('name', 'Anonymous')} ({user.get('uid', 'Unknown')})")
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")