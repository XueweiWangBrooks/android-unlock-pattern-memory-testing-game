from django.urls import path
from . import views

urlpatterns = [
    path('game/<int:id>', views.getGameById),
    path('game/put/<int:id>', views.putNewScore),
    path('game', views.getGameList),
    path('game/post', views.postGame)
]
