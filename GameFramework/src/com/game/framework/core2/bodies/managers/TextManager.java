package com.game.framework.core2.bodies.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TextManager {
   private Vector2 pos = new Vector2(0f, 0f);
   private String text = "";
   private Color color;

   public TextManager() {
      color = color.WHITE;
   }

   public void setOffset(Vector2 offset) {
      pos.x += offset.x;
      pos.y += offset.y;
   }

   public Vector2 getOffset() {
      return pos;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText() {
      return text;
   }

   public void setColor(Color color) {
      this.color = color;
   }
}
