package com.game.framework.core2.character;

import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BodyBuilder;
import com.game.framework.core2.character.managers.CharacterMovementManager;
import com.game.framework.core2.character.managers.DirectionInputManager;
import com.game.framework.core2.character.managers.DirectionManager;

public class Character extends WorldBody {

    public DirectionManager direction;
    public CharacterMovementManager movement;
    public DirectionInputManager input;

    public Character(WorldManager world, BodyBuilder builder) {
        super(world, builder);

        this.direction = new DirectionManager();
        this.movement = new CharacterMovementManager(this.direction);
        this.input = new DirectionInputManager(this, movement);
    }

}
