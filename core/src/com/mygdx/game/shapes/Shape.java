package com.mygdx.game.shapes;

import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BodyBuilder;

public class Shape extends WorldBody {
   protected int weight = 2;

   public Shape(BodyBuilder builder) {
      super(builder);
   }

   public Shape setWeight(int weight) {
      this.weight = weight;
      return this;
   }

   public int getWeight() {
      return this.weight;
   }
}
