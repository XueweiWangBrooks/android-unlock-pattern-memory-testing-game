from django.db import models

# Create your models here.
class Game(models.Model):
    def __str__(self):
        return (str(self.id) + ": high score = " + str(self.high_score) + ", user = " +  self.user_name + ", date = " + str(self.hscore_date))
    high_score = models.IntegerField(default = 0)
    user_name = models.CharField(max_length = 50)
    hscore_date = models.DateTimeField()

class Pattern(models.Model):
    def __str__(self):
        return (str(self.id) + ":" + "game_id = "+ str(self.game_id) + ", pattern_str = " + self.pattern_str)
    game = models.ForeignKey(Game, on_delete = models.CASCADE)
    pattern_str = models.CharField(max_length = 500)