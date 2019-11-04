from django.contrib import admin

# Register your models here.
from .models import Game, Pattern

admin.site.register(Game)
admin.site.register(Pattern)
