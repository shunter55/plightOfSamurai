package com.mygdx.game.screens;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shapes.Ball;

import java.util.Comparator;

public class DistanceCompare implements Comparator<Ball>{
   private Vector2 stopper = new Vector2(0f,2.7f);

   @Override
   public int compare(Ball b1, Ball b2) {
      double b1dist = Math.hypot(b1.body.body.getPosition().x-stopper.x,
                              b1.body.body.getPosition().y-stopper.y);
      double b2dist = Math.hypot(b2.body.body.getPosition().x-stopper.x,
          b2.body.body.getPosition().y-stopper.y);
         if(b1dist > b2dist){
            return 1;
         } else {
            return -1;
         }
   }

}
