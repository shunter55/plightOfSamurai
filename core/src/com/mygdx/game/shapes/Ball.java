package com.mygdx.game.shapes;

import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BodyBuilder;

public class Ball extends WorldBody {
   private boolean gravity = true;
   private int weight = 1;

   public Ball(WorldManager manager, BodyBuilder builder)
   {
      super(manager, builder);

   }

   public Ball setGrav(boolean gravity) {
      this.gravity = gravity;
      return this;
   }

   public boolean getGrav() {
      return this.gravity;
   }

   public Ball setWeight(int weight) {
      this.weight = weight;
      return this;
   }

   public int getWeight() {
      return this.weight;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != Ball.class)
         return false;

      return this.id().equals(((Ball)obj).id());
   }
}
