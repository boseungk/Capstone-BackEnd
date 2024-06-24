import json
import boto3
import os
from io import BytesIO
import base64
import uuid
import time
import google.generativeai as genai

ENDPOINT_NAME = os.getenv("ENDPOINT")
GOOGLE_API_KEY = os.getenv("GOOGLE_API_KEY")
INIT_FASHION_KEYWORD = "캐주얼"
runtime= boto3.client('runtime.sagemaker')
model = genai.GenerativeModel('gemini-1.5-pro-latest')

def lambda_handler(event, context):
    genai.configure(api_key=GOOGLE_API_KEY)
    request = json.loads(event['Records'][0]['body'])
    inputs = request["inputs"]
    if(inputs == ""):
        inputs = INIT_FASHION_KEYWORD
    input_id_images = request["inputIdImages"]
    chat = model.start_chat(history=words_prompt_history())
    print(chat.send_message(inputs).candidates[0].content.parts[0].text)
    prompt = chat.send_message(inputs).candidates[0].content.parts[0].text[:-3] + " women img"
    data = change_prompt(prompt, input_id_images)
    response = runtime.invoke_endpoint(EndpointName=ENDPOINT_NAME,
                                    ContentType='application/json',
                                    Accept='application/json',
                                    Body=json.dumps(data))
    response = json.loads(response['Body'].read().decode('utf-8'))
    image = base64.b64decode(response["images"][0])
    upload_urls = upload_s3(image)
    client = boto3.client("sqs")
    client.send_message(
        QueueUrl="https://sqs.ap-northeast-2.amazonaws.com/590183743566/responseQueue",
        MessageBody=json.dumps(*upload_urls)
        )

def change_prompt(inputs, images):
    return {
        "inputs": inputs,
        "input_id_images": [img for img in images]
    }

def upload_s3(image):
    upload_urls = []
    s3 = boto3.client("s3")
    key = f'{uuid.uuid4()}.png'
    s3.put_object(Bucket='ai-styling-s3', Key=key, Body=image)
    upload_urls.append(f'https://ai-styling-s3.s3.ap-northeast-2.amazonaws.com/{key}')
    return upload_urls

def words_prompt_history():
    return [
        {
            "role": "user",
            "parts": ["You're a fashion expert vividly describing outfits for given styles in English using only 15 words. \n\nExamples: 캐주얼 패션"]
        },
        {
            "role": "model",
            "parts": ["Oversized graphic tee in faded colors, ripped mom jeans, chunky sneakers, crossbody bag, layered necklaces, messy beach waves hairstyle."]
        },
        {
            "role": "user",
            "parts": ["스트릿 패션"]
        },
        {
            "role": "model",
            "parts": ["Oversized hoodie with bold graphics, distressed skinny jeans, high-top sneakers, baseball cap, statement accessories like chunky chains and rings"]
        },
        {
            "role": "user",
            "parts": ["스트릿 패션의 스타일링, 도시적이고 쿨한 룩: 대담한 그래픽이 들어간 오버사이즈 후디, 낡은 스키니"]
        },
        {
            "role": "model",
            "parts": ["An edgy urban look: Oversized hoodie with bold graphics, distressed skinny jeans, high-top sneakers, statement accessories like chunky chains and rings, windswept hairstyle."]
        },
        {
            "role": "user",
            "parts": ["흰 블라우스, 주름 잡힌 체크무늬 치마 (파란색과 초록색 톤), 검은색 허벅지까지 오는 양말, 갈색 로퍼, 양반다리, 편안한 자세, 수줍은 표정, 얼굴을 가린 머리, 야외 설정, 콘크리트 발코니 또는 테라스, 난간, 푸른 하늘"]
        },
        {
            "role": "model",
            "parts": ["White blouse, pleated plaid skirt (blue and green tones), black thigh-high socks, brown loafers, Sitting cross-legged, relaxed posture, shy expression, hair over face, Outdoor setting, concrete balcony or terrace, railing or barrier, blue sky"]
        }
    ]