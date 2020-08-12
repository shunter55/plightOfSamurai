package com.mygdx.game.shapes;

import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BodyBuilder;

public class PowerUp extends Shape {
   // 0 - Split Up
   // 1 - +1 ball
   // 2 - +1 weight
   private int type;

   public PowerUp(WorldManager manager, BodyBuilder builder) {
      super(manager, builder);
      weight = 1;
      type = randomIntRange(0, 3);
   }

   public PowerUp setType(int weight) {
      this.type = weight;
      return this;
   }

   public int getType() {
      return this.type;
   }

   private static int randomIntRange(int min, int max) {
      return (int) (Math.random() * (max - min) + min);
   }

}
