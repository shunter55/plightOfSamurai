package com.mygdx.game.shapes;

import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BodyBuilder;

public class Shape extends WorldBody {
   protected int weight = 2;

   public Shape(WorldManager manager, BodyBuilder builder) {
      super(manager, builder);
   }

   public Shape setWeight(int weight) {
      this.weight = weight;
      return this;
   }

   public int getWeight() {
      return this.weight;
   }
}
