from django.shortcuts import render
from .models import Game, Pattern
# Create your views here.
from django.http import HttpResponse
from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse
from django.utils import timezone
import json

def getGameById(request, id):
    if request.method != 'GET':
        response = {}
        response['result'] = 'error'
        response['message'] = 'url route unspecified.'
        return JsonResponse(response)
    try:
        thisGame = Game.objects.get(id = id)
        gameDict = vars(thisGame)
        patternDictList = list(thisGame.pattern_set.all().values())
        gameDict.pop('_state')
        response = {}
        response['result'] = 'ok'
        response['data'] = {"game": gameDict, "patterns": patternDictList}
        return JsonResponse(response)
    except ObjectDoesNotExist:
        response = {}
        response['result'] = 'error'
        response['message'] = 'Did not find any game with id: ' + str(id)
        return JsonResponse(response)

def getGameList(request):
    if request.method != 'GET':
        response = {}
        response['result'] = 'error'
        response['message'] = 'url route unspecified.'
        return JsonResponse(response)
    gameDictList = list(Game.objects.all().values())
    for game in gameDictList:
        date = game.pop("hscore_date")
        game['hscore_date'] = date.strftime('%m/%d/%Y')
    response = {}
    response['result'] = 'ok'
    response['data'] = gameDictList
    return JsonResponse(response)

def postGame(request):
    if request.method != 'POST':
        response = {}
        response['result'] = 'error'
        response['message'] = 'url route unspecified.'
        return JsonResponse(response)
    if request.content_type != 'application/json':
        response = {}
        response['result'] = 'error'
        response['message'] = 'data provided format error.'
        return JsonResponse(response)
    body_unicode = request.body.decode('utf-8')
    body = json.loads(body_unicode)
    userName = body["user_name"]
    highScore = body["high_score"]
    patternList = body["pattern_list"]
    thisGame = Game(high_score = highScore, user_name = userName, hscore_date = timezone.now())
    thisGame.save()
    for pattern in patternList:
        thisGame.pattern_set.create(pattern_str=pattern)
    response = {}
    response['result'] = 'ok'
    response['data'] = {}
    return JsonResponse(response)

def putNewScore(request, id):
    if request.method != 'PUT':
        response = {}
        response['result'] = 'error'
        response['message'] = 'url route unspecified.'
        return JsonResponse(response)
    if request.content_type != 'application/json':
        response = {}
        response['result'] = 'error'
        response['message'] = 'data provided format error.'
    body_unicode = request.body.decode('utf-8')
    body = json.loads(body_unicode)
    try:
        thisGame = Game.objects.get(id = id)
        thisGame.high_score = body["score"]
        thisGame.user_name = body["username"]
        thisGame.hscore_date = timezone.now();
        thisGame.save()
        response = {}
        response['result'] = 'ok'
        response['data'] = {}
        return JsonResponse(response)
    except ObjectDoesNotExist:
        response = {}
        response['result'] = 'error'
        response['message'] = 'Did not find any game with id: ' + str(id)
        return JsonResponse(response)
        
        